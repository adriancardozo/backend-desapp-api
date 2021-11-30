package ar.edu.unq.desapp.grupoL.backenddesappapi.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test


class ArchTests {
    @Test
    fun webServicesNameEnding() {
        val importedClasses: JavaClasses = ClassFileImporter().importPackages("ar.edu.unq.desapp.grupoL.backenddesappapi");

        val rule: ArchRule = classes()
            .that().resideInAPackage("..webservices..")
            .should().haveSimpleNameEndingWith("RestService");

        rule.check(importedClasses);
    }

    @Test
    fun repositoriesNameEnding() {
        val importedClasses: JavaClasses = ClassFileImporter().importPackages("ar.edu.unq.desapp.grupoL.backenddesappapi");

        val rule: ArchRule = classes()
            .that().resideInAPackage("..repositories..")
            .should().haveSimpleNameEndingWith("Repository");

        rule.check(importedClasses);
    }
}