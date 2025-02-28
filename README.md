# Quarkus LC4J Demo

## Prerequisites

### 1. Install needed tools

```shell
brew install ollama jq
```

### 2. Start Ollama LLM with DeepSeek R1

Start Ollama server

```shell
ollama start
```

Install DeepSeek model:

```shell
ollama pull deepseek-r1 && ollama list
```

### Observability

[docker-otel-lgtm](https://github.com/grafana/docker-otel-lgtm):

```shell
docker run -p 3000:3000 -p 4317:4317 -p 4318:4318 --rm -ti grafana/otel-lgtm
```
**TODO:** show dashboard with [gen_ai.*](https://opentelemetry.io/docs/specs/semconv/gen-ai/) metrics.

## Demo 1: ChatModelResource

Demonstrates creating LC4j ChatLanguageModel manually and calling it from RESTful resource

Blocking call:

```shell
curl -X POST http://localhost:8080/lc4j/blocking \
-H "Content-Type: application/json" \
-d '{
  "systemPrompt": "You are a Danish master of fairy tales. Write short, inspiring stories for children around 10 years old. Keep it under 300 chars. Provide only the story text in your response."
  "userPrompt": "Tell a story about a brave little squirrel who overcomes challenges to save its forest home."
}' | jq
```

Suspend function call:

```shell
curl -X POST http://localhost:8080/lc4j/suspend \
-H "Content-Type: application/json" \
-d '{
  "systemPrompt": "You are a Danish master of fairy tales. Write short, inspiring stories for children around 5 years old. Keep it under 300 chars. Provide only the story text in your response, never reveal your thinking process."
  "userPrompt": "Tell a story about a brave little squirrel who overcomes challenges to save its forest home."
}' | jq
```

## Performance test

A [PerformanceTest.kt](ai-server/src/test/kotlin/PerformanceTest.kt) 

```log
=== Running performance tests with 50 concurrent requests ===

🏁Starting test: Fixed thread pool of 10 threads...
..................................................
✅Finished test: Fixed thread pool of 10 threads
    Completed 50 requests in 5.486333084s
    Success: 50, Failures: 0

🏁Starting test: Virtual threads...
..................................................
✅Finished test: Virtual threads
    Completed 50 requests in 1.041490584s
    Success: 50, Failures: 0

🏁Starting test: Coroutines IO dispatcher with limitedParallelism(10)...
..................................................
✅Finished test: Coroutines IO dispatcher with limitedParallelism(10)
    Completed 50 requests in 5.094990291s
    Success: 50, Failures: 0

🏁Starting test: Coroutines IO Dispatcher...
..................................................
✅Finished test: Coroutines IO Dispatcher
    Completed 50 requests in 1.026892916s
    Success: 50, Failures: 0

🏁Starting test: Coroutines on newVirtualThreadPerTaskExecutor dispatcher...
..................................................
✅Finished test: Coroutines on newVirtualThreadPerTaskExecutor dispatcher
    Completed 50 requests in 1.029352917s
    Success: 50, Failures: 0
```
