package com.esharky.kafkaconsumer.model;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {
    String message;
    String uuid;
    Map<String, String> tracePropogation;
}
