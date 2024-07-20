package io.factorialsystems.msscpirateparrotcommunication.service;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import io.factorialsystems.msscpirateparrotcommunication.dto.SmsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitListenerService {
    private final SendMailService sendMailService;

    @RabbitListener(queues = "${rabbitmq.queue.email.name}")
    public void listenEmailQueue(EmailDTO emailDTO) {
        log.info("Sending Email Request to Queue: {}", emailDTO);
        sendMailService.sendMail(emailDTO);
    }

    @RabbitListener (queues = "${rabbitmq.queue.sms.name}")
    public void listenSmsQueue(SmsDTO smsDTO) {
        log.info("SMS Queue: {}", smsDTO);
    }

}
