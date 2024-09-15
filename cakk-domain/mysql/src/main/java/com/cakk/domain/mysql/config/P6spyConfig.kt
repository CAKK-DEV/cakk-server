package com.cakk.domain.mysql.config

import com.p6spy.engine.spy.P6SpyOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class P6spyConfig {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6spySqlFormatterConfig::class.java.name
    }
}
