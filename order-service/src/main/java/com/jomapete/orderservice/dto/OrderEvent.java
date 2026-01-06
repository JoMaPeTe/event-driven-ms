package com.jomapete.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * Just an entity with very few attributes to send with our event orders
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent implements Serializable {
    private String id;
    private String satelliteName;
    private String action; // Ej: "CALCULATE MANEUVERING PARAMETERS"
}