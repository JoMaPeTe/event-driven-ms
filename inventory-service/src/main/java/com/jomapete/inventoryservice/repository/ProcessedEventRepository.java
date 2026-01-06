package com.jomapete.inventoryservice.repository;

import com.jomapete.inventoryservice.entity.ProcessedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends MongoRepository<ProcessedEvent, String> {

}