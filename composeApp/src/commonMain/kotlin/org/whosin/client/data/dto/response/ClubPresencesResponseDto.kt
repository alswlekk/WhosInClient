package org.whosin.client.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubPresencesResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: ClubPresencesData
)

@Serializable
data class ClubPresencesData(
    @SerialName("clubName")
    val clubName: String,
    @SerialName("presentMembers")
    val presentMembers: List<PresentMembers>
)

@Serializable
data class PresentMembers(
    @SerialName("userName")
    val userName: String,
    @SerialName("isMe")
    val isMe: Boolean
)