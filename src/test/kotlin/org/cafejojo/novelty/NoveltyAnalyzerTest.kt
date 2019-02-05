package org.cafejojo.novelty

import org.assertj.core.api.Assertions.assertThat
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.junit.jupiter.api.Test
import java.io.File

internal class NoveltyAnalyzerTest {
    @Test
    internal fun testGetNovelSets() {
        val oldSets = setOf(setOf(1, 2, 3), setOf(2, 3, 4))
        val newSets = setOf(setOf(1, 2, 3, 4), setOf(1, 2), setOf(2, 3, 4), setOf(0, 5))

        assertThat(NoveltyAnalyzer.getNumNovelSets(oldSets, newSets)).isEqualTo(2)
    }

    @Test
    internal fun testAnalyzeNovelty() {
        assertThat(
            NoveltyAnalyzer(
                JavaMavenProject(
                    File(javaClass.getResource("/novelty-project/").toURI())
                )
            ).analyzeNovelty()
        ).isEqualTo(3)
    }
}
