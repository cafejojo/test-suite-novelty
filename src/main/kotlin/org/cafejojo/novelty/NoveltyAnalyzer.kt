package org.cafejojo.novelty

import mu.KLogging
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.cafejojo.schaapi.models.project.MavenProject
import java.io.File

/**
 * Determines the novelty of a test suite compared to another test suite.
 *
 * Expects a standard Maven project with the following folder setup:
 *
 * src
 * - main
 * - test.old
 * - test.new
 *
 * The tests in `test.new` are compared for novelty against the tests in `test.old`.
 *
 * @property mavenProject The MavenProject instance to analyze.
 */
class NoveltyAnalyzer(private val mavenProject: MavenProject) {
    companion object : KLogging() {
        fun <T> getNumNovelSets(oldSets: Set<Set<T>>, newSets: Set<Set<T>>) = newSets.count { newSet ->
            oldSets.none { oldSet -> oldSet.containsAll(newSet) }
        }
    }

    fun analyzeNovelty(): Int {
        assert(File(mavenProject.projectDir, "src/test.old").exists()) {
            "Missing old test suite at location 'src/test.old'"
        }
        assert(File(mavenProject.projectDir, "src/test.new").exists()) {
            "Missing new test suite at location 'src/test.new'"
        }

        File(mavenProject.projectDir, "src/test.old").copyRecursively(File(mavenProject.projectDir, "src/test"))
        logger.info { "Running coverage analysis for the old tests" }
        val oldTestSuiteCoverageSets = CoverageRunner(mavenProject).recordCoveragePerTest()
        File(mavenProject.projectDir, "src/test").deleteRecursively()

        File(mavenProject.projectDir, "src/test.new").copyRecursively(File(mavenProject.projectDir, "src/test"))
        logger.info { "Running coverage analysis for the new tests" }
        val newTestSuiteCoverageSets = CoverageRunner(mavenProject).recordCoveragePerTest()
        File(mavenProject.projectDir, "src/test").deleteRecursively()

        return getNumNovelSets(oldTestSuiteCoverageSets, newTestSuiteCoverageSets)
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("First argument missing: location of the Java Maven project to be analyzed")
        return
    }

    val result = NoveltyAnalyzer(JavaMavenProject(File(args[0]))).analyzeNovelty()
    println("Novel tests: $result")
}
