package org.acme.lc4j.chat

import dev.langchain4j.model.chat.response.StreamingChatResponseHandler
import dev.langchain4j.model.ollama.OllamaStreamingChatModel
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.jboss.logging.Logger

private val logger = Logger.getLogger(ChatModelResource::class.java)

// TODO: THIS EXAMPLE IS NOT READY YET

@Path("/lc4j/streaming")
open class StreamingLc4jChatResource {
    private val model =
        OllamaStreamingChatModel
            .builder()
            .baseUrl("http://localhost:11434")
            .modelName("deepseek-r1")
            .logRequests(true) // not for production!
            .logResponses(true) // not for production!
            .temperature(0.8)
            .build()

//    @Channel("prices")
//    lateinit var emitter: Emitter<String>

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    fun hello(request: ChatApiRequest): Multi<String> {
        logger.info("Received request: ${request.userPrompt}")
//        val multi: Multi<String> =
        val emitter: Emitter<String> = TODO()
        model.chat(
            request.userPrompt,
            object : StreamingChatResponseHandler {
                override fun onPartialResponse(partialResponse: String) {
                    logger.info("Received partial response: $partialResponse")
                    emitter.send(partialResponse)
                }

                override fun onCompleteResponse(
                    completeResponse: dev.langchain4j.model.chat.response.ChatResponse?,
                ) {
                    logger.info("Received complete response: $completeResponse")
                    emitter.send(completeResponse.toString())
                }

                override fun onError(error: Throwable) {
                    logger.error("Received error: ${error.message}", error)
                    emitter.error(RuntimeException(error))
                }
            },
        )
        return TODO()
    }
}
