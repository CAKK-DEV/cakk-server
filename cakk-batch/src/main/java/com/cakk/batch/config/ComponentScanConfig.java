package com.cakk.batch.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cakk.domain.mysql.config.DataSourceConfig;
import com.cakk.domain.mysql.config.JpaConfig;

@Configuration
@ComponentScan(basePackages = {
	"com.cakk.domain.redis",
	"com.cakk.batch"
}, basePackageClasses = {
	DataSourceConfig.class,
	JpaConfig.class
})
public class ComponentScanConfig {
}
