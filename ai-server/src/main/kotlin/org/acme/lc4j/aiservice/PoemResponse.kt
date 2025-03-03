package org.acme.lc4j.aiservice

import kotlinx.serialization.Serializable

@Serializable
data class PoemResponse(
    val text: String,
)
