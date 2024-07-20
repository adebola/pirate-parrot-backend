package io.factorialsystems.msscpirateparrotcommunication.repository;

import io.factorialsystems.msscpirateparrotcommunication.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String> {
    Page<Email> findBySentBy(String sentBy, Pageable pageable);
    Page<Email> findByTo(String to, Pageable pageable);
}
