package com.cakk.api;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

@DisplayName("Application Architecture Test")
class ArchitectureTest {

	private static JavaClasses javaClasses;

	@BeforeAll
	static void setUp() {
		javaClasses = new ClassFileImporter()
			.withImportOption(new ImportOption.DoNotIncludeTests())
			.importPackages("com.cakk");
	}

	@TestWithDisplayName("response 패키지 안에 있는 클래스들은 Response로 끝나야 한다.")
	void response() {
		ArchRule rule = classes()
			.that().resideInAPackage("..response")
			.and().areTopLevelClasses()
			.should().haveSimpleNameEndingWith("Response");

		rule.check(javaClasses);
	}

	@Disabled
	@TestWithDisplayName("Provider 클래스는 Service, Filter, Provider 클래스에만 의존해야 한다.")
	void providerDependency() {
		ArchRule rule = classes()
			.that().haveNameMatching(".*Provider")
			.and().areNotEnums()
			.should().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Service")
			.orShould().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Filter")
			.orShould().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Provider");

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
