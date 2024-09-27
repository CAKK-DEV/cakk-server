package com.cakk.api.common.base

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

import com.cakk.domain.mysql.config.JpaConfig


@Import(JpaConfig::class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension::class)
abstract class MockitoTest {
}
