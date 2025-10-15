package org.whosin.client.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailValidationRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("authCode")
    val authCode: String
)