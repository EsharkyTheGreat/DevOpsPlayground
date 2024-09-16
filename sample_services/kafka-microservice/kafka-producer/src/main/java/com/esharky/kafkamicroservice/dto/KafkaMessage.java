package com.esharky.kafkamicroservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class KafkaMessage {
    String message;
    String uuid;
    Map<String, String> tracePropogation;
}
