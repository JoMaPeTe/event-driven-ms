package com.jomapete.inventoryservice.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * <b>Input DTO</b> (Data Transfer Object) representing the message payload received via RabbitMQ.
 *  * <p>
 *  * This class defines the contract for incoming tasks. It is used to deserialize
 *  * the JSON message from the {@code orbit-tasks-queue} into a Java object
 *  * that the listener can process.
 *  * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent implements Serializable {
    private String id;
    private String satelliteName;
    private String action; // Ej: "CALCULATE_ORBIT"
}
