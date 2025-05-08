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
        val poem = """
                In shadowed woods where silence grows,
                A single horn in moonlight glows.
                Not beast, not dream, but something more—
                A step on leaves, a myth restored.
                Then gone, like mist from forest floor.
            """.trimIndent()

        mockOpenai.completion {
            systemMessageContains("You are a professional poet.")
            userMessageContains("Write a poem about Unicorn")
            userMessageContains("The poem should be 5 lines long")
        } responds {
            assistantContent = poem
        }

        given()
            .body(
                PoemRequest(
                    topic = "Unicorn",
                    lines = 5
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

