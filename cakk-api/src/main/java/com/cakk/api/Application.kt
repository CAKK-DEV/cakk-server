package com.cakk.api

import java.util.*

import jakarta.annotation.PostConstruct

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

@PostConstruct
fun started() {
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}

fun main(args: Array<String>) {
	started()
	runApplication<Application>(*args)
}

