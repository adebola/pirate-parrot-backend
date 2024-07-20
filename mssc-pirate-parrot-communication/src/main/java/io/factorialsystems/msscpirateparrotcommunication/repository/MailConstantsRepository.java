package io.factorialsystems.msscpirateparrotcommunication.repository;

import io.factorialsystems.msscpirateparrotcommunication.model.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MailConstantsRepository {
    private final MongoTemplate mongoTemplate;

    public Constant getMailFooter() {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("footer")), Constant.class);
    }
}
