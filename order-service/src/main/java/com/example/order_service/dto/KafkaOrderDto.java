package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaOrderDto {
    private Schema schema;
    private Payload payload;

}
