package com.jomapete.orderservice.repository;

import com.jomapete.orderservice.entity.OrderState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderState, String> {
}