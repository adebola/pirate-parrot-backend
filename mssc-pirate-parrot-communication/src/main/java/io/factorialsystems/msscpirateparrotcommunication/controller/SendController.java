package io.factorialsystems.msscpirateparrotcommunication.controller;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import io.factorialsystems.msscpirateparrotcommunication.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotcommunication.service.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/send")
public class SendController {
    private final SendMailService sendMailService;

    @PostMapping
    public ResponseEntity<?> sendMailWithoutAttachment(@Valid @RequestBody EmailDTO dto)  {
        return sendMailService.sendMail(dto).map(s -> ResponseEntity.ok(
                MessageDTO.builder()
                        .id(s)
                        .message(String.format("Email sent successfully without attachment id %s", s))
                        .build()
        )).orElseGet(() -> ResponseEntity.badRequest().body(
                MessageDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .message("Email send error")
                        .build()
        ));
    }

    @CrossOrigin(origins = "http://localhost:4200")
//    @PostMapping( value = "/attachment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @PostMapping("/attachment")
    public ResponseEntity<?> sendMailWithAttachment(@RequestPart(value = "message") @Valid EmailDTO dto, @RequestPart(value = "file") MultipartFile file) throws MessagingException, IOException {
        return sendMailService.sendMailWithAttachment(dto, file).map(s -> ResponseEntity.ok(
                MessageDTO.builder()
                        .id(s)
                        .message(String.format("Email sent successfully with attachment id %s", s))
                        .build()
        )).orElseGet(() -> ResponseEntity.badRequest().body(
                MessageDTO.builder()
                        .id("Error")
                        .message("Email send with attachment error")
                        .build()
        ));
    }
}
