package org.cafejojo.novelty

import org.assertj.core.api.Assertions.assertThat
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.junit.jupiter.api.Test

import java.io.File

internal class CoverageRunnerTest {
    @Test
    fun testFindTestMethodsInSimpleProject() {
        val coverageRunner = CoverageRunner(
            JavaMavenProject(
                File(javaClass.getResource("/simple-maven-project-with-jacoco/").toURI())
            )
        )
        val coverageSets = coverageRunner.recordCoveragePerTest()

        assertThat(coverageSets).hasSize(2)
        assertThat(coverageSets).allMatch { it.size == 2 }
        assertThat(coverageSets).anySatisfy { it.contains(CoveredLine("org/cafejojo/schaapi/test/MyClass.java", 5)) }
        assertThat(coverageSets).anySatisfy { it.contains(CoveredLine("org/cafejojo/schaapi/test/MyClass.java", 9)) }
        assertThat(coverageSets).allSatisfy { it.contains(CoveredLine("org/cafejojo/schaapi/test/MyClass.java", 3)) }
    }
}
