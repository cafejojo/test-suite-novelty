package org.cafejojo.novelty

import org.assertj.core.api.Assertions.assertThat
import org.cafejojo.schaapi.models.project.JavaMavenProject
import org.junit.jupiter.api.Test
import java.io.File

internal class NoveltyAnalyzerTest {
    @Test
    internal fun testGetNovelSets() {
        val oldSets = mapOf(
            Pair("a", setOf(1, 2, 3)),
            Pair("b", setOf(2, 3, 4))
        )
        val newSets = mapOf(
            Pair("c", setOf(1, 2, 3, 4)),
            Pair("d", setOf(1, 2)),
            Pair("e", setOf(2, 3, 4)),
            Pair("f", setOf(0, 5))
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
