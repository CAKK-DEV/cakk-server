package com.cakk.admin.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
	"com.cakk.domain",
	"com.cakk.admin"
])
class ComponentScanConfig
