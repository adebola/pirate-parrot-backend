package io.factorialsystems.msscpirateparrotauthorization.repository;

import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationRegisteredClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationRegisteredClientRepository {
    List<ApplicationRegisteredClient> findAll();
    ApplicationRegisteredClient findByClientId(String id);
    ApplicationRegisteredClient findById(String id);
    void save(ApplicationRegisteredClient client);
    void update(ApplicationRegisteredClient client);
}