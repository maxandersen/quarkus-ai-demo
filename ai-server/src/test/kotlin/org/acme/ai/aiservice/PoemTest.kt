package org.acme.ai.aiservice

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.QuarkusTestProfile
import io.quarkus.test.junit.TestProfile
import io.restassured.RestAssured.given
import org.acme.ai.aiservice.TestEnvironment.mockOpenai
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Tag
import kotlin.test.Test

@QuarkusTest
@TestProfile(PoemTest.MockLlmProfile::class)
class PoemTest {

    class MockLlmProfile : QuarkusTestProfile {
        override fun getConfigOverrides(): Map<String, String> {
            return mapOf("quarkus.langchain4j.openai.base-url" to mockOpenai.baseUrl())
        }
    }

    @Test
        @Tag("LLM")
        @Tag("integration")
        @Tag("rest")
    fun `LLM should generate a poem`() {
        val lines = 3
        val topic = "Unicorn"
        val poem = """
                A quiet step through morning mist,
                Horn aglow with starlight's twist,
                It vanishes where dreams persist.
            """.trimIndent()

        mockOpenai.completion {
            systemMessageContains("You are a professional poet.")
            userMessageContains("Write a poem about $topic.")
            userMessageContains("The poem should be $lines lines long")
        } responds {
            assistantContent = poem
        }

        given()
            .body(
                PoemRequest(
                    topic = topic,
                    lines = lines
                )
            )
            .contentType("application/json")
            .`when`().post("/lc4j/poem")
            .then()
            .statusCode(200)
            .body(
                "text",
                `is`("$poem\n© AI-Server, ${java.time.Year.now()}")
            )
    }
}

