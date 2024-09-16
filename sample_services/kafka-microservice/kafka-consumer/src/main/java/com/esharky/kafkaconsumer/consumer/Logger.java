package com.esharky.kafkaconsumer.consumer;

import com.esharky.kafkaconsumer.model.KafkaMessage;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.util.Map;

@Slf4j
@Service
public class Logger {

    private final Tracer tracer;

    @Autowired
    public Logger(OpenTelemetry openTelemetry) {
        this.tracer = openTelemetry.getTracer("kafka-microservice");
    }

    @KafkaListener(topics = "test_topic",groupId = "example-group-id")
    public void log(@Payload String message, @Header("X-Trace-ID") String traceID) {
        Span span = tracer.spanBuilder("send").startSpan();
        span.addEvent("Received Message");
        span.setAttribute("X-Trace-ID", traceID);
        log.info("Got message: {}", message);
        log.info("Trace ID: {}", traceID);
        span.end();
    }

    @KafkaListener(topics = "test_topic2",groupId = "example-group-id2",containerFactory = "kafkaMessageContainerFactory")
    public void log2(@Payload KafkaMessage message, @Header("X-Trace-ID") String traceID) {
        log.info("Got message: {}", message);
        log.info("Trace ID: {}", traceID);
        Map<String,String> spanContextMap = message.getTracePropogation();

        TextMapGetter<Map<String,String>> getter = new TextMapGetter<Map<String, String>>() {
            @Override
            public Iterable<String> keys(Map<String, String> stringStringMap) {
                return stringStringMap.keySet();
            }

            @Override
            public String get(Map<String, String> stringStringMap, String s) {
                if (stringStringMap.containsKey(s)) {
                    return stringStringMap.get(s);
                }
                return null;
            }
        };
        Context extractedContext = GlobalOpenTelemetry.getPropagators().getTextMapPropagator().extract(Context.current(),spanContextMap,getter);
        try (Scope scope = extractedContext.makeCurrent()) {
            Span span = tracer.spanBuilder("consumer").startSpan();
            span.addEvent("message-received");
            span.setAttribute("X-Trace-ID", traceID);
            log.info("Got message: {}", message);
            log.info("Trace ID: {}", traceID);
            span.end();
            Span.fromContext(Context.current()).addEvent("EndingParent");
            Span.fromContext(Context.current()).end();
        }
    }
}
