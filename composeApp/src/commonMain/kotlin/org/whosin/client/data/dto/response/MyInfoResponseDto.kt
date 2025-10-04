package org.whosin.client.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: MyInfoData
)

@Serializable
data class MyInfoData(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("clubList")
    val clubList: List<MyClubData>
)

@Serializable
data class MyClubData(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
