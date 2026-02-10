package com.openclassrooms.mddapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS (Cross-Origin Resource Sharing) configuration. Configures which origins are allowed to
 * access the API.
 */
@Configuration
public class CorsConfig {

  /**
   * Allowed origins for CORS requests. Configured via cors.allowed-origins property.
   */
  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  /**
   * Creates and configures the CORS filter. Allows credentials, all headers, and all HTTP methods
   * from the configured origin.
   *
   * @return configured CorsFilter instance
   */
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.addAllowedOrigin(allowedOrigins);
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}