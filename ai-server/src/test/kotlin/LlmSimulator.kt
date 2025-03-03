import io.ktor.http.HttpStatusCode
import me.kpavlov.aimocks.openai.MockOpenai
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

fun main() {
    val openai = MockOpenai(port = 8089, verbose = true)

    openai.completion {
        userMessageContains("Mermaid")
    } responds {
        assistantContent =
            """
            In azure depths where mortal eyes see naught,
            A maiden fair with scales of emerald hue,
            Whose songs the raging tempests have oft fought,
            Doth yearn for realms where love might prove most true.
            Her siren call, both sweet and filled with rue.
            """.trimIndent()
    }

    openai.completion {
        userMessageContains("Boom")
    } respondsError {
        httpStatus = HttpStatusCode.InternalServerError
        body = null
    }

    openai.completion {
        userMessageContains("Slow")
    } respondsError {
        delay = 8.seconds
        body =
            """
            Languid pink creature, dreaming by the shore,
            His thoughts arrive like tides—a day too late, yet cherished all the more.
            """.trimIndent()
    }

    Thread.sleep(1.hours.inWholeMilliseconds)
}
