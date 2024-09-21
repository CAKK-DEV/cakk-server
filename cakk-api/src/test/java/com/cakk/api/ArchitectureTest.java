package com.cakk.api;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import com.cakk.api.common.annotation.TestWithDisplayName;

@DisplayName("Application Architecture Test")
class ArchitectureTest {

	private static JavaClasses javaClasses;

	@BeforeAll
	static void setUp() {
		javaClasses = new ClassFileImporter()
			.withImportOption(new ImportOption.DoNotIncludeTests())
			.importPackages("com.cakk");
	}

	@TestWithDisplayName("request 패키지 안에 있는 클래스들은 Request로 끝나야 한다.")
	void request() {
		ArchRule rule = classes()
			.that().resideInAPackage("..request..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Request");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("response 패키지 안에 있는 클래스들은 Response로 끝나야 한다.")
	void response() {
		ArchRule rule = classes()
			.that().resideInAPackage("..response..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Response");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("param 패키지 안에 있는 클래스들은 Param으로 끝나야 한다.")
	void param() {
		ArchRule rule = classes()
			.that().resideInAPackage("..param..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Param");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("event 패키지 안에 있는 클래스들은 Event로 끝나야 한다.")
	void event() {
		ArchRule rule = classes()
			.that().resideInAPackage("..event..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Event");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("config 패키지 안에 있는 클래스들은 Config로 끝나야 한다.")
	void config() {
		ArchRule rule = classes()
			.that().resideInAPackage("..config..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Config");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("factory 패키지 안에 있는 클래스들은 Factory 끝나야 한다.")
	void factory() {
		ArchRule rule = classes()
			.that().resideInAPackage("..factory..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Factory");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("filter 패키지 안에 있는 클래스들은 Filter 끝나야 한다.")
	void filter() {
		ArchRule rule = classes()
			.that().resideInAPackage("..filter..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Filter");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("listener 패키지 안에 있는 클래스들은 Listener 끝나야 한다.")
	void listener() {
		ArchRule rule = classes()
			.that().resideInAPackage("..listener..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Listener");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("provider 패키지 안에 있는 클래스들은 Provider 끝나야 한다.")
	void provider() {
		ArchRule rule = classes()
			.that().resideInAPackage("..provider..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Provider");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("resolver 패키지 안에 있는 클래스들은 Resolver 끝나야 한다.")
	void resolver() {
		ArchRule rule = classes()
			.that().resideInAPackage("..resolver..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Resolver");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("validator 패키지 안에 있는 클래스들은 Validator 끝나야 한다.")
	void validator() {
		ArchRule rule = classes()
			.that().resideInAPackage("..validator..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Validator");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("service 패키지 안에 있는 클래스들은 Service로 끝나야 한다.")
	void service() {
		ArchRule rule = classes()
			.that().resideInAPackage("..service..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Service");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("controller 패키지 안에 있는 클래스들은 Controller로 끝나야 한다.")
	void controller() {
		ArchRule rule = classes()
			.that().resideInAPackage("..controller..")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Controller")
			.orShould().haveSimpleNameEndingWith("ControllerAdvice");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("Provider 클래스는 Controller 클래스에 의존하지 않아야 한다.")
	void providerNotController() {
		ArchRule rule = noClasses()
			.that().resideInAnyPackage("..controller..")
			.should().onlyDependOnClassesThat().resideInAnyPackage("..provider..");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("Factory 클래스는 Service 클래스에만 의존해야 한다.")
	void factoryDependency() {
		ArchRule rule = classes()
			.that().haveNameMatching(".*Factory")
			.should().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Service");

		rule.check(javaClasses);
	}

	@DisplayName("Controller 는 Service, Repository 에 의존하지 않아야 한다.")
	@Test
	void serviceNotController() {
		ArchRule rule = noClasses()
			.that().resideInAnyPackage("..service..", "..repository..")
			.should().dependOnClassesThat()
			.resideInAnyPackage("..controller..");

		rule.check(javaClasses);
	}

	@TestWithDisplayName("Entity는 아무것도 의존하지 않는다.")
	void entityNotDependency() {
		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..entity..")
			.should()
			.onlyDependOnClassesThat()
			.resideInAnyPackage("..model..", "java..", "jakarta..")
			.orShould().notHaveSimpleName("AuditingEntityListener");

		rule.check(javaClasses);
	}
}
