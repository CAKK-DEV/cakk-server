package com.cakk.api.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
	basePackages = [
		"com.cakk.infrastructure",
		"com.cakk.external",
		"com.cakk.core",
		"com.cakk.api"
	]
)
class ComponentScanConfig
