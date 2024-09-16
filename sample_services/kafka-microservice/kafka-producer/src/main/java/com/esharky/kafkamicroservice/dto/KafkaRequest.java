package com.esharky.kafkamicroservice.dto;

import lombok.Getter;

@Getter
public class KafkaRequest {
    String topic;
    String message;
}
