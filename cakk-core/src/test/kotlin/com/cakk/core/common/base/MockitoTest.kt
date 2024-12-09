package com.cakk.core.common.base

import com.cakk.common.enums.Provider
import com.cakk.common.enums.Role
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

import net.jqwik.api.Arbitraries

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin

import com.cakk.infrastructure.persistence.entity.user.User
import com.cakk.external.vo.key.OidcPublicKey
import com.cakk.infrastructure.persistence.config.JpaConfig
import java.time.LocalDate

@Import(JpaConfig::class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension::class)
abstract class MockitoTest {

	protected fun getConstructorMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getUserFixture(role: Role = Role.USER): User {
		return fixtureMonkey.giveMeBuilder(User::class.java)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider::class.java))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("birthday", LocalDate.now())
			.set("role", role)
			.sample()
	}

    protected fun oidcPublicKeyFixture(): OidcPublicKey {
            return getConstructorMonkey().giveMeBuilder(OidcPublicKey::class.java)
                .set("kid", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .set("kty", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .set("alg", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .set("use", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .set("n", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .set("e", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
                .sample()
        }
}
