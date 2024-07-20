package io.factorialsystems.msscpirateparrotcommunication.service;

import io.factorialsystems.msscpirateparrotcommunication.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotcommunication.model.Email;
import io.factorialsystems.msscpirateparrotcommunication.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final EmailRepository emailRepository;

    public PagedDTO<Email> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Email> emails = emailRepository.findAll(pageable);

        return createDTO(emails);
    }

    public PagedDTO<Email> findBySentBy(String sentBy, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Email> emails = emailRepository.findBySentBy(sentBy, pageable);

        return createDTO(emails);
    }

    public PagedDTO<Email> findBySentTo(String sentTo, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Email> emails = emailRepository.findByTo(sentTo, pageable);

        return createDTO(emails);
    }

    public Optional<Email> findById(String id) {
        return Optional.of(emailRepository.findById(id)).orElse(Optional.empty());
    }

    private PagedDTO<Email> createDTO(Page<Email> emails) {
        PagedDTO<Email> pagedDto = new PagedDTO<>();

        pagedDto.setList(emails.stream().toList());
        pagedDto.setPages(emails.getTotalPages());
        pagedDto.setPageNumber(emails.getNumber());
        pagedDto.setPageSize(emails.getSize());
        pagedDto.setTotalSize((int) emails.getTotalElements());

        return pagedDto;
    }
}
