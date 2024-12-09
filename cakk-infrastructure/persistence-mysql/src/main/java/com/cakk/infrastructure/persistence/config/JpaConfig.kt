package com.cakk.infrastructure.persistence.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan("com.cakk.infrastructure.persistence.entity.**")
@EnableJpaRepositories("com.cakk.infrastructure.persistence.repository.**")
class JpaConfig
