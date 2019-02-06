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
 * @property mavenProject the MavenProject instance to analyze
 */
class NoveltyAnalyzer(private val mavenProject: MavenProject) {
    companion object : KLogging() {
        /**
         * Compares two sets and returns the number of new sets which are not subsets of any old set.
         *
         * @param oldSets the old sets
         * @param newSets the new sets
         */
        fun <T> getNovelSets(oldSets: Map<String, Set<T>>, newSets: Map<String, Set<T>>) = newSets.filter { newSet ->
            oldSets.none { oldSet -> oldSet.value.containsAll(newSet.value) }
        }
    }

    /**
     * Compares the two test suites expected in the [mavenProject] on novelty.
     */
    fun analyzeNovelty(): Map<String, Set<CoveredLine>> {
        assert(File(mavenProject.projectDir, "src/test.old").exists()) {
            "Missing old test suite at location 'src/test.old'"
        }
        assert(File(mavenProject.projectDir, "src/test.new").exists()) {
            "Missing new test suite at location 'src/test.new'"
        }

        File(mavenProject.projectDir, "src/test").deleteRecursively()

        File(mavenProject.projectDir, "src/test.old").copyRecursively(File(mavenProject.projectDir, "src/test"))
        logger.info { "Running coverage analysis for the old tests" }
        val oldTestSuiteCoverageSets = CoverageRunner(mavenProject).recordCoveragePerTest()
        File(mavenProject.projectDir, "src/test").deleteRecursively()

        File(mavenProject.projectDir, "src/test.new").copyRecursively(File(mavenProject.projectDir, "src/test"))
        logger.info { "Running coverage analysis for the new tests" }
        val newTestSuiteCoverageSets = CoverageRunner(mavenProject).recordCoveragePerTest()
        File(mavenProject.projectDir, "src/test").deleteRecursively()

        logger.info { oldTestSuiteCoverageSets }
        logger.info { newTestSuiteCoverageSets }

        return getNovelSets(oldTestSuiteCoverageSets, newTestSuiteCoverageSets)
    }
}

/**
 * Entry point for test suite novelty analysis.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("First argument missing: location of the Java Maven project to be analyzed")
        return
    }

    val result = NoveltyAnalyzer(JavaMavenProject(File(args[0]))).analyzeNovelty()
    println("Novel tests: ${result.keys}")
    println("Number of novel tests: ${result.size}")
}
