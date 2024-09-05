package io.factorialsystems.msscpirateparrotcommunication.service;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

@CommonsLog
@SpringBootTest
class SendMailServiceTest {

    @Autowired
    SendMailService emailService;

    @Test
    void getEmails() {
    }

    @Test
    void sendMail() {
        CompletableFuture.runAsync(() -> {
            EmailDTO emailDTO = EmailDTO.builder()
                    .to("aomoboya@icloud.com")
                    .subject("test")
                    .sentBy("Adebola")
                    .secret("secret")
                    .htmlBody("<h1>HTML Message Body</h1>")
                    .textBody("TEXT Message Body")
                    .build();

             // emailService.sendMail(emailDTO);
        });

        log.info("Email sent successfully");
    }
}