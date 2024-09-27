package com.cakk.core.annotation

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(

    val key: String = "",
    val timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    val waitTime: Long = 5000L,
    val leaseTime: Long = 3000L
)
