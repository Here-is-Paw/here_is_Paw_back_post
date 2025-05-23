package com.ll.hereispaw.global_msa.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class KafkaConfig {
  @Bean
  public RecordMessageConverter converter() {
    return new JsonMessageConverter();
  }
}