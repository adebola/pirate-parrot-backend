package io.factorialsystems.msscpirateparrotcommunication.service;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;

@CommonsLog
//@SpringBootTest
class SendMailServiceTest {

    //@Autowired
    SendMailService sendMailService;

    @Test
    void getEmails() {
    }

    @Test
    void sendMail() {
        EmailDTO emailDTO = EmailDTO.builder()
                .to("aomoboya@icloud.com")
                .subject("test")
                .sentBy("Adebola")
                .secret("secret")
                .htmlBody("<h1>HTML Message Body</h1>")
                .textBody("TEXT Message Body")
                .build();

        //final Optional<String> id = emailService.sendMail(emailDTO);
        //assertThat(id).isPresent();
    }
}