package com.esharky.kafkamicroservice.controller;

import com.esharky.kafkamicroservice.dto.KafkaMessage;
import com.esharky.kafkamicroservice.dto.KafkaRequest;
import com.esharky.kafkamicroservice.service.Kafka;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/test")
public class Test {

    private final Kafka kafka;
    private final Tracer tracer;

    @Autowired
    public Test(Kafka kafka, OpenTelemetry openTelemetry) {
        this.kafka = kafka;
        this.tracer = openTelemetry.getTracer("kafka-microservice");
    }

    @GetMapping("/test")
    String test(@RequestParam String message) {
        Span span = tracer.spanBuilder("test").startSpan();
        span.setAttribute("message", message);
        span.addEvent("Got Message");
        log.info("Got message: {}", message);
        span.end();
        return message;
    }

    @PostMapping("/send")
    void send(@RequestBody String message) {
        Span parentSpan = tracer.spanBuilder("etl")
                .startSpan();
        try (Scope scope = parentSpan.makeCurrent()) {
            Span span = tracer.spanBuilder("producer")
//                    .setParent(Context.current().with(parentSpan))
                    .startSpan();
            span.setAttribute("message", message);
            UUID traceId = UUID.randomUUID();
            span.setAttribute("X-Trace-ID", traceId.toString());
            Map<String,String> spanContextMap = new HashMap<>();
            TextMapSetter<Map<String,String>> setter = (stringStringMap, s, s1) -> stringStringMap.put(s, s1);
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(),spanContextMap,setter);
            KafkaMessage queueMessage = KafkaMessage.builder()
                    .uuid(traceId.toString())
                    .tracePropogation(spanContextMap)
                    .message(message)
                    .build();
            log.info("Send message: {}", message);
            span.addEvent("Kafka Message Production");
            kafka.sendMessage("test_topic2",queueMessage);
            log.info("Message sent");
            parentSpan.end();
            span.end();
        }
    }

    @PostMapping("/send-topic")
    void sendTopic(@RequestBody KafkaRequest kafkaRequest) {
        kafka.sendMessage(kafkaRequest.getTopic(),kafkaRequest.getMessage());
    }
}
