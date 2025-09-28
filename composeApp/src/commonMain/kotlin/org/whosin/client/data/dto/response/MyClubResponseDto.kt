package org.whosin.client.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyClubResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: UserClubs
)

@Serializable
data class UserClubs(
    @SerialName("userClubs")
    val userClubs: List<ClubData>
)

@Serializable
data class ClubData(
    @SerialName("clubId")
    val clubId: Int,
    @SerialName("clubName")
    val clubName: String
)
