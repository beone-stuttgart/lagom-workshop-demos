package com.beone.workshop.lagom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration modify jackson to support jdk8 datatypes.
 */
@Configuration
public class JacksonConfig {

  @Bean
  @Primary
  public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {
    final ObjectMapper objectMapper = builder.createXmlMapper(false).build();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

}
