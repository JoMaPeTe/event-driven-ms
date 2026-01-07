package com.jomapete.orderservice.service;

import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.dto.OrderEvent;

public interface OrderService {
    void processOrbitResult(OrbitResult result);
    OrderEvent createOrder(OrderEvent orderEvent);

}
