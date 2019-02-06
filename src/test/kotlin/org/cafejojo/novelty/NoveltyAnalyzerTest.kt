package org.cafejojo.novelty

import org.assertj.core.api.Assertions.assertThat
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.junit.jupiter.api.Test
import java.io.File

internal class NoveltyAnalyzerTest {
    @Test
    internal fun testGetNovelSets() {
        val oldSets = mapOf(
            "a" to setOf(1, 2, 3),
            "b" to setOf(2, 3, 4)
        )
        val newSets = mapOf(
            "c" to setOf(1, 2, 3, 4),
            "d" to setOf(1, 2),
            "e" to setOf(2, 3, 4),
            "f" to setOf(0, 5)
        )

        assertThat(NoveltyAnalyzer.getNovelSets(oldSets, newSets).size).isEqualTo(2)
    }

    @Test
    internal fun testAnalyzeNovelty() {
        assertThat(
            NoveltyAnalyzer(
                JavaMavenProject(
                    File(javaClass.getResource("/novelty-project/").toURI())
                )
            ).analyzeNovelty().size
        ).isEqualTo(3)
    }
}
