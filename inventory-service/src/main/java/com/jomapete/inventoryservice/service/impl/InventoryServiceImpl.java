package com.jomapete.inventoryservice.service.impl;

import com.jomapete.inventoryservice.dto.OrbitResult;
import com.jomapete.inventoryservice.dto.OrderEvent;
import com.jomapete.inventoryservice.entity.ProcessedEvent;
import com.jomapete.inventoryservice.repository.ProcessedEventRepository;
import com.jomapete.inventoryservice.service.InventoryService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // IOC (inversion of control): inyect DB connection
    @Autowired
    private ProcessedEventRepository repository;

    @Override
    public void handleOrbitProcess(OrderEvent event) {

        //  1. IDEMPOTENCY WITH MONGO DB
        if (repository.existsById(event.getId())) {
            System.out.println("DUPLICATION IGNORE: Event " + event.getId() + " is already processed");
            return;
        }
        //SAGA pattern: kind of transactional
        if ("ROLLBACK_ORBIT".equals(event.getAction())) {
            System.out.println("ROLLBACK ORDER RECEIVED for ID: " + event.getId());
            undoOperation(event.getId());
            return;
        }
        //Processing sheet
        ProcessedEvent ficha = new ProcessedEvent(
                event.getId(),
                event.getSatelliteName(),
                LocalDateTime.now().toString()
        );
        repository.save(ficha);
        System.out.println("Saved event on Mongo: " + event.getId());


        // 2. Business logic simulation
        System.out.println("Calculating data for: " + event.getSatelliteName());
        try { Thread.sleep(2000); } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        //  3. Response (Saga) It returns a FAILED status to indicate others services to rollback in their own DB---
        String status = "COMPLETED";
        String details = "Calculation successful.";
        //  4. FAILURE SIMULATION (Trigger)
        if ("ERROR".equalsIgnoreCase(event.getSatelliteName())){
            status = "FAILED";
            details = "Critical Error: Not enough fuel for maneuver.";
        }
        //  5. PRODUCER PATTERN (Feedback Loop)
        // The Worker acts as a Producer here, sending the result back to the results-queue
        // to complete the Saga transaction.
        OrbitResult result = new OrbitResult(event.getId(), status, details);
        rabbitTemplate.convertAndSend("orbit-results-queue", result);
        System.out.println("Result sent: " + status);
    }

    @Override
    public void undoOperation(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id); // Borramos de Mongo (o del Set)
            System.out.println(" Undo operation " + id + "Released resources.");
        } else {
            System.out.println("Not found ID to undo: " + id);
        }
    }
}

