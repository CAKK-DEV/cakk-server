package com.cakk.batch.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cakk.infrastructure.persistence.config.DataSourceConfig;
import com.cakk.infrastructure.persistence.config.JpaConfig;

@Configuration
@ComponentScan(basePackages = {
	"com.cakk.infrastructure.cache",
	"com.cakk.batch"
}, basePackageClasses = {
	DataSourceConfig.class,
	JpaConfig.class
})
public class ComponentScanConfig {
}
