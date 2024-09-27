package com.cakk.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

import com.cakk.api.common.annotation.TestWithDisplayName

@DisplayName("Application Architecture Test")
internal class ArchitectureTest {

	private lateinit var javaClasses: JavaClasses

	@BeforeEach
	fun setUp() {
		javaClasses = ClassFileImporter()
			.withImportOption(DoNotIncludeTests())
			.importPackages("com.cakk")
	}

    @TestWithDisplayName("request 패키지 안에 있는 클래스들은 Request로 끝나야 한다.")
    fun request() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..request..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Request")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("response 패키지 안에 있는 클래스들은 Response로 끝나야 한다.")
    fun response() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..response..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Response")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("param 패키지 안에 있는 클래스들은 Param으로 끝나야 한다.")
    fun param() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..param..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Param")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("event 패키지 안에 있는 클래스들은 Event로 끝나야 한다.")
    fun event() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..event..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Event")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("config 패키지 안에 있는 클래스들은 Config로 끝나야 한다.")
    fun config() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..config..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Config")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("dispatcher 패키지 안에 있는 클래스들은 Dispatcher 끝나야 한다.")
    fun dispatcher() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..dispatcher..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameContaining("Dispatcher")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("filter 패키지 안에 있는 클래스들은 Filter 끝나야 한다.")
    fun filter() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..filter..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Filter")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("listener 패키지 안에 있는 클래스들은 Listener 끝나야 한다.")
    fun listener() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..listener..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Listener")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("provider 패키지 안에 있는 클래스들은 Provider 끝나야 한다.")
    fun provider() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..provider..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameContaining("Provider")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("resolver 패키지 안에 있는 클래스들은 Resolver 끝나야 한다.")
    fun resolver() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..resolver..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Resolver")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("validator 패키지 안에 있는 클래스들은 Validator 끝나야 한다.")
    fun validator() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..validator..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Validator")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("service 패키지 안에 있는 클래스들은 Service로 끝나야 한다.")
    fun service() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..service..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Service")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("controller 패키지 안에 있는 클래스들은 Controller로 끝나야 한다.")
    fun controller() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("Controller")
            .orShould().haveSimpleNameEndingWith("ControllerAdvice")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("Provider 클래스는 Controller 클래스에 의존하지 않아야 한다.")
    fun providerNotController() {
        val rule: ArchRule = ArchRuleDefinition.noClasses()
            .that().resideInAnyPackage("..controller..")
            .should().onlyDependOnClassesThat().resideInAnyPackage("..provider..")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("Dispatcher 클래스는 Service 클래스에만 의존해야 한다.")
    fun dispatcherDependency() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that().haveSimpleNameContaining("DispatcherImpl")
            .should().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Service")
            .orShould().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Dispatcher")

        rule.check(javaClasses)
    }

    @DisplayName("Controller 는 Service, Repository 에 의존하지 않아야 한다.")
    @Test
    fun serviceNotController() {
        val rule: ArchRule = ArchRuleDefinition.noClasses()
            .that().resideInAnyPackage("..service..", "..repository..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..controller..")

        rule.check(javaClasses)
    }

    @TestWithDisplayName("Entity는 아무것도 의존하지 않는다.")
    fun entityNotDependency() {
        val rule: ArchRule = ArchRuleDefinition.classes()
            .that()
            .resideInAnyPackage("..entity..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage("..model..", "java..", "jakarta..")
            .orShould().notHaveSimpleName("AuditingEntityListener")

        rule.check(javaClasses)
    }
}
