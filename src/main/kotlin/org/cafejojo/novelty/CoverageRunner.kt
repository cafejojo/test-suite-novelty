package org.cafejojo.novelty

import com.github.javaparser.JavaParser
import org.apache.maven.shared.invoker.DefaultInvocationRequest
import org.apache.maven.shared.invoker.DefaultInvoker
import org.cafejojo.schaapi.maveninstaller.MavenInstaller
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.cafejojo.schaapi.models.project.MavenProject
import org.jacoco.core.analysis.Analyzer
import org.jacoco.core.analysis.CoverageBuilder
import org.jacoco.core.analysis.ICounter
import org.jacoco.core.tools.ExecFileLoader
import java.io.File

/**
 * Class representing a partly or fully covered line.
 */
data class CoveredLine(val fileName: String, val line: Int)

/**
 * Runner for per-test coverage recording for Maven projects.
 *
 * Assumes that the Maven project in question has the JaCoCo plugin configured (as part of the `test` step).
 */
class CoverageRunner(private val mavenProject: MavenProject) {
    /**
     * Records the coverage of each test in the test configuration of the Maven project and outputs the coverage sets.
     */
    fun recordCoveragePerTest() = runMavenForAllTests(findTestMethods())

    private fun findTestMethods() = File(mavenProject.projectDir, "src/test").walkTopDown()
        .filter { it.extension == "java" }
        .map { JavaParser.parse(it) }
        .map { file ->
            file.types.map { type ->
                type.methods
                    .filter { method -> method.isAnnotationPresent("Test") }
                    .map { method ->
                        file.packageDeclaration.get().name.asString() + "." + type.name.asString() +
                            "#" + method.name.asString()
                    }
            }.flatten()
        }.flatten().toList()

    private fun runMavenForAllTests(testMethodDescriptors: List<String>) =
        testMethodDescriptors.mapIndexed { index, method ->
            runJacocoForSingleTest(method, index == 0)

            val execFileLoader = ExecFileLoader()
            execFileLoader.load(File(mavenProject.projectDir, "target/coverage-reports/jacoco-ut.exec"))
            val coverageBuilder = CoverageBuilder()
            val analyzer = Analyzer(execFileLoader.executionDataStore, coverageBuilder)
            analyzer.analyzeAll(File(mavenProject.projectDir, "target/classes"))

            getCoverageSetFromClassCoverages(coverageBuilder)
        }.toSet()

    private fun getCoverageSetFromClassCoverages(coverageBuilder: CoverageBuilder) = coverageBuilder.classes
        .map { classCoverage ->
            (classCoverage.firstLine..classCoverage.lastLine).filter { line ->
                classCoverage.getLine(line).status == ICounter.FULLY_COVERED ||
                    classCoverage.getLine(line).status == ICounter.PARTLY_COVERED
            }.map { line ->
                CoveredLine(classCoverage.packageName + "/" + classCoverage.sourceFileName, line)
            }
        }.flatten().toSet()

    private fun runJacocoForSingleTest(testMethod: String, clean: Boolean) {
        MavenInstaller().installMaven(JavaMavenProject.DEFAULT_MAVEN_HOME)

        val request = DefaultInvocationRequest().apply {
            baseDirectory = mavenProject.projectDir
            goals = if (clean) listOf("clean", "test") else listOf("test")
            isBatchMode = true
            javaHome = File(System.getProperty("java.home"))
            mavenOpts = "-Dtest=$testMethod,$testMethod[*]"
            pomFile = mavenProject.pomFile
        }

        val invoker = DefaultInvoker().also {
            it.mavenHome = mavenProject.mavenDir
            it.workingDirectory = mavenProject.projectDir
        }

        val result = invoker.execute(request)
        if (result.exitCode != 0)
            throw CoverageRunnerException(
                "Recording coverage of ${mavenProject.projectDir} failed: " +
                    (result.executionException?.message ?: "Cause unknown.")
            )
    }
}

/**
 * An exception occurring during a test coverage run.
 */
class CoverageRunnerException(message: String?) : RuntimeException(message)
