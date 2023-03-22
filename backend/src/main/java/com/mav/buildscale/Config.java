package com.mav.buildscale;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.mav.buildscale.model")
@EnableJpaRepositories(basePackages = "com.mav.buildscale.repository")
@EnableJpaAuditing
public class Config {
}
