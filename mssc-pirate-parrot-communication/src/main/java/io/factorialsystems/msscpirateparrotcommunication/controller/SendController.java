package io.factorialsystems.msscpirateparrotcommunication.controller;

import io.factorialsystems.msscpirateparrotcommunication.dto.EmailDTO;
import io.factorialsystems.msscpirateparrotcommunication.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotcommunication.service.SendMailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/send")
public class SendController {
    private final SendMailService sendMailService;

    @PostMapping
    public ResponseEntity<?> sendMailWithoutAttachment(@Valid @RequestBody EmailDTO dto)  {
        CompletableFuture.runAsync(() -> sendMailService.sendMail(dto));

        return ResponseEntity.ok(
                MessageDTO.builder()
                        .id("pending-" + UUID.randomUUID())
                        .message("Email sent successfully")
                        .build()
        );
    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @PostMapping( value = "/attachment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @PostMapping("/attachment")
    public ResponseEntity<?> sendMailWithAttachment(@RequestPart(value = "message") @Valid EmailDTO dto, @RequestPart(value = "file") MultipartFile file) {
        CompletableFuture.runAsync(() -> sendMailService.sendMailWithAttachment(dto, file));

        return ResponseEntity.ok(
                MessageDTO.builder()
                        .id("pending-" + UUID.randomUUID())
                        .message("Email sent successfully with attachment")
                        .build()
        );
    }
}
