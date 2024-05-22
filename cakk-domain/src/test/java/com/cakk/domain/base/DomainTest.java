package com.cakk.domain.base;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

public abstract class DomainTest {

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}
}
