package io.factorialsystems.msscpirateparrotcommunication.controller;

import io.factorialsystems.msscpirateparrotcommunication.service.MailService;
import io.factorialsystems.msscpirateparrotcommunication.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communication/mail")
public class MailController {
    private final MailService mailService;

    @GetMapping
    public ResponseEntity<?> getEmails(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return ResponseEntity.ok(mailService.findAll(pageNumber, pageSize));
    }

    @GetMapping("/sentby")
    public ResponseEntity<?> findBySentBy(@RequestParam(value = "sentBy") String sentBy,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return ResponseEntity.ok(mailService.findBySentBy(sentBy, pageNumber, pageSize));
    }

    @GetMapping("/sentto")
    public ResponseEntity<?> findBySentTo(@RequestParam(value = "sentTo") String sentTo,
                                          @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return ResponseEntity.ok(mailService.findBySentTo(sentTo, pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id") String id) {
        mailService.findById(id);
        return ResponseEntity.ok(mailService.findById(id));
    }
}
