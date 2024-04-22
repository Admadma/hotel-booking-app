package com.application.hotelbooking.repositories;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan("com.application.hotelbooking.domain")
@EnableJpaRepositories("com.application.hotelbooking.repositories")
public class DomainTestConfiguration {
}
