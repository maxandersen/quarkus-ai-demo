package org.acme.lc4j.aiservice

import kotlinx.serialization.Serializable

@Serializable
data class PoemRequest(
    val topic: String,
    val lines: Int = 10,
)
