package org.debugroom.sample.continuous.integration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.debugroom.sample.continuous.integration.domain.service")
@Configuration
public class DomainConfig {
}
