package com.jomapete.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *   Persistence entity representing the lifecycle state of an order.
 *   It´s  mapped to a <b>document</b> within the "orders" <b>collection</b>.
 *   <p>
 *   This DB is the local source of truth for the service. It is crucial for the <b>Saga Pattern</b>,
 *   allowing the system to track whether a distributed transaction is {@code PENDING},
 *   {@code COMPLETED}, or has been rolled back to {@code CANCELLED} (Compensating Transaction).
 *   </p>
 */
@Document(value = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderState {
    @Id
    private String id;
    private String status; // PENDING, COMPLETED, CANCELLED
}