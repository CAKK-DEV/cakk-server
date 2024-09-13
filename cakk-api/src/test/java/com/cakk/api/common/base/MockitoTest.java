package com.cakk.api.common.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import com.cakk.domain.mysql.config.JpaConfig;
import com.cakk.external.vo.key.OidcPublicKey;

@Import(JpaConfig.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class MockitoTest {

	protected final FixtureMonkey getConstructorMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected OidcPublicKey getOidcPublicKeyFixture() {
		return getConstructorMonkey().giveMeBuilder(OidcPublicKey.class)
			.set("kid", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.set("kty", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.set("alg", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.set("use", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.set("n", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.set("e", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(10))
			.sample();
	}
}
