package org.whosin.client.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueTokenRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String
)
