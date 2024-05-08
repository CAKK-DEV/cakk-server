package com.cakk.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
	"com.cakk.client",
	"com.cakk.domain",
	"com.cakk.external",
	"com.cakk.api"
})
public class ComponentScanConfig {
}
