package com.jomapete.inventoryservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <b>Output DTO</b> used to send the calculation outcome back to the Order Service.
 *  * <p>
 *  * This object is serialized to JSON and published to the {@code orbit-results-queue}.
 *  * It carries the final status (COMPLETED/FAILED) required to close the Saga transaction.
 *  * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrbitResult implements Serializable {
    private String originalOrderId;
    private String status;   // "COMPLETED" o "FAILED"
    private String details;  // Mensaje de error o éxito
}