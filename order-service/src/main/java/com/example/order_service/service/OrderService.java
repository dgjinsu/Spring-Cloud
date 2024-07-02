package com.example.order_service.service;


import com.example.order_service.dto.OrderDto;
import com.example.order_service.jpa.OrderEntity;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    List<OrderEntity> getOrdersByUserId(String userId);
}