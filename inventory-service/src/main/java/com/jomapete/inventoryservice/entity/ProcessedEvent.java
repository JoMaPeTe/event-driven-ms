package com.jomapete.inventoryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB entity used to enforce <b>Idempotency</b>.
 *  <p>
 *  Mapped to the "processed_events" collection, this document records the IDs
 *  of tasks that have already been executed. This prevents the worker from
 *  re-processing the same message in case of RabbitMQ redeliveries or system restarts.
 *  </p>
 */
@Document(value = "processed_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {

    @Id // Unique key
    private String messageId;

    private String satelliteName;
    private String processingTime;
}