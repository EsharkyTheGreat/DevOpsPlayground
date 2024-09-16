package com.esharky.kafkaconsumer;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerApplication.class, args);
	}
	@Bean
	public OpenTelemetry openTelemetry() {
		return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
	}
}
