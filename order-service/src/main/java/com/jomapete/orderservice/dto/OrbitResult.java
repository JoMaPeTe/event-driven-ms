package com.jomapete.orderservice.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrbitResult implements Serializable {
    private String originalOrderId;
    private String status;   // "COMPLETED" o "FAILED"
    private String details;  // Mensaje de error o éxito
}