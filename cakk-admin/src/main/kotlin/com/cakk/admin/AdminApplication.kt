package com.cakk.admin

import java.util.TimeZone

import jakarta.annotation.PostConstruct

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class AdminApplication

@PostConstruct
fun started() {
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}

fun main(args: Array<String>) {
	started()

	runApplication<AdminApplication>(*args)
}
