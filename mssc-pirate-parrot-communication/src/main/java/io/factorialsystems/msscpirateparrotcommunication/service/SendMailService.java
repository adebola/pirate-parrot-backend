package io.factorialsystems.msscpirateparrotcommunication.service;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import io.factorialsystems.msscpirateparrotcommunication.model.Constant;
import io.factorialsystems.msscpirateparrotcommunication.model.Email;
import io.factorialsystems.msscpirateparrotcommunication.repository.EmailRepository;
import io.factorialsystems.msscpirateparrotcommunication.repository.MailConstantsRepository;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailService {
    private final SesV2Client sesV2Client;
    private final EmailRepository emailRepository;
    private final MailConstantsRepository mailConstantsRepository;

    @Value("${email.secret}")
    private String mailSecret;

    @Value("${email.from}")
    private String fromAddress;

    private String mailFooter;

    // Send Mail without attachment, using AWS SES V2
    public void sendMail(EmailDTO dto) {
        if (!checkEmail(dto)) {
            return;
        }

        final Body body;
        final String footer = getMailFooter();

        if (dto.getHtmlBody() == null) {
            final String bodyText = dto.getTextBody() + "\n\n\n\n\n\n\n\n\n\n\n" + footer;

            body = Body.builder()
                    .text(
                            Content.builder()
                            .data(bodyText)
                            .build()
                    )
                    .build();
        } else {
            final String bodyHtml = dto.getHtmlBody() + "<br><br><br><br><br><br><br><small>" + footer + "</small>";
            body = Body.builder()
                    .html(
                            Content.builder()
                                    .data(bodyHtml)
                                    .build()
                    )
                    .build();
        }

        Message msg = Message.builder()
                .subject(Content.builder()
                        .data(dto.getSubject())
                        .build())
                .body(body)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();

        Destination destination = Destination.builder()
                .toAddresses(dto.getTo())
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(fromAddress)
                .build();

        try {
            SendEmailResponse response = sesV2Client.sendEmail(emailRequest);
            log.info("Email sent without attachment message id {}", response.messageId());

            Email email = dto.toEmail();
            email.setExternalId(response.messageId());
            email.setFrom(fromAddress);

            final Email save = emailRepository.save(email);

        } catch (SesV2Exception e) {
            log.error("Error sending email without attachment {}", e.getMessage());
        }
    }

    // Send Mail with attachment, using AWS SES V2
    public void sendMailWithAttachment(EmailDTO dto, MultipartFile file) {
        if (!checkEmail(dto)) {
            return;
        }

        try {
            SendEmailRequest emailRequest = createRawEmailRequest(dto, file);
            SendEmailResponse response = sesV2Client.sendEmail(emailRequest);
            log.info("Email sent with attachment message id {}", response.messageId());

            Email email = dto.toEmail();
            email.setExternalId(response.messageId());
            email.setFrom(fromAddress);

            if (file.getOriginalFilename() != null) {
                email.setAttachmentFileName(file.getOriginalFilename());
            }

            final Email save = emailRepository.save(email);

        } catch (Exception e) {
            log.error("Error sending email with attachment {}", e.getMessage());
        }
    }

    // Handle the creation of the Raw Email Request
    private SendEmailRequest createRawEmailRequest(EmailDTO emailDTO, MultipartFile multipartFile) throws MessagingException, IOException {
        // Create a new Session object
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        // Create a new MimeMessage object
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(emailDTO.getTo()));
        message.setSubject(emailDTO.getSubject());

        // Create a multipart/alternative child container
        final MimeBodyPart wrap = getMimeBodyPart(emailDTO);

        // Create a multipart/mixed parent container
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the wrapper object to the message
        message.setContent(msg);

        // Add the parent container to the message
        msg.addBodyPart(wrap);

        // Define the attachment
        MimeBodyPart att = new MimeBodyPart();
        att.setDataHandler(new DataHandler(multipartFile.getBytes(), multipartFile.getContentType()));
        att.setFileName(multipartFile.getOriginalFilename());

        // Add the attachment to the message
        msg.addBodyPart(att);

        // Convert the message to bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        byte[] rawMessageBytes = outputStream.toByteArray();
        SdkBytes data = SdkBytes.fromByteArray(rawMessageBytes);

        // Create RawMessage object
        RawMessage rawMessage = RawMessage.builder()
                .data(data)
                .build();

        return SendEmailRequest.builder()
                .content(EmailContent.builder().raw(rawMessage).build())
                .build();
    }

    private MimeBodyPart getMimeBodyPart(EmailDTO emailDTO) throws MessagingException {
        MimeMultipart msgBody = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts
        MimeBodyPart wrap = new MimeBodyPart();

        final MimeBodyPart textPart = getBodyPart(emailDTO);

        // Add the text and HTML parts to the child container
        msgBody.addBodyPart(textPart);

        // Add the child container to the wrapper object
        wrap.setContent(msgBody);
        return wrap;
    }

    private MimeBodyPart getBodyPart(EmailDTO emailDTO) throws MessagingException {
        final String footer = getMailFooter();
        final MimeBodyPart textPart = new MimeBodyPart();

        // Define the text part
        if (emailDTO.getHtmlBody() == null) {
            final String bodyText = emailDTO.getTextBody() + "\n\n\n\n\n\n\n\n\n\n\n" + footer;
            textPart.setContent(bodyText, "text/plain");
        } else {
            final String bodyHtml = emailDTO.getHtmlBody() + "<br><br><br><br><br><br><br><small>" + footer + "</small>";
            textPart.setContent(bodyHtml, "text/html");
        }
        return textPart;
    }

    // Sending mails are Authentication free since they will be sent asynchronously
    // The user context will not be loaded, hence we protect the endpoint with a secret
    // Mail Requests are typically sent via RabbitMQ messages
    private Boolean checkEmail(EmailDTO dto) {
        if (!mailSecret.equals(dto.getSecret())) {
            log.error("Invalid Secret {} submitted, sending Mail", dto.getSecret());
            return false;
        }

        if (dto.getHtmlBody() == null && dto.getTextBody() == null) {
            log.error("Email Text and HTML body cannot be empty");
            return false;
        }

        return true;
    }

    private String getMailFooter() {
        if (this.mailFooter == null) {
            Constant constant = mailConstantsRepository.getMailFooter();

            if (constant != null && constant.getValue() != null) {
                this.mailFooter = constant.getValue();
            }
        }

        return this.mailFooter;
    }
}
