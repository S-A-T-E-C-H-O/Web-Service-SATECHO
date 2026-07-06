package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA and database configuration for AgroSafe.
 * Enables repository scanning, auditing, and transaction management.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.satecho.agrosafe")
@EntityScan(basePackages = "com.satecho.agrosafe")
@EnableTransactionManagement
@EnableJpaAuditing
public class DataSourceConfig {

}
