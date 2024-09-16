package com.esharky.kafkamicroservice.service;

import com.esharky.kafkamicroservice.dto.KafkaMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Kafka {
    @Autowired
    private KafkaTemplate<String,String > kafkaTemplate;
    @Autowired
    KafkaTemplate<String,KafkaMessage> kafkaMessageTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
    public void sendMessage(String topic, String message, UUID uuid) {
        ProducerRecord<String,String> record = new ProducerRecord<>(topic, message);
        Header uuidHeader = new RecordHeader("X-Trace-ID",uuid.toString().getBytes());
        record.headers().add(uuidHeader);
        kafkaTemplate.send(record);
    }
    public void sendMessage(String topic, KafkaMessage message) {
        ProducerRecord<String,KafkaMessage> record = new ProducerRecord<>(topic, message);
        Header uuidHeader = new RecordHeader("X-Trace-ID", message.getUuid().getBytes());
        record.headers().add(uuidHeader);
        kafkaMessageTemplate.send(record);
    }
}
