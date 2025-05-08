package org.acme.ai.aiservice

import me.kpavlov.aimocks.openai.MockOpenai

object TestEnvironment {
    val mockOpenai = MockOpenai(verbose = true)
}
