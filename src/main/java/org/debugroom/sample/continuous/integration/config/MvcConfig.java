package org.debugroom.sample.continuous.integration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("org.debugroom.sample.continuous.integration.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {
}
