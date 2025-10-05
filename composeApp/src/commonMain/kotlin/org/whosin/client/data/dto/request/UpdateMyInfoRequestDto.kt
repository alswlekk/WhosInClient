package org.whosin.client.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMyInfoRequestDto(
    @SerialName("nickName")
    val nickName: String,
    @SerialName("clubList")
    val clubList: List<Int>?
)
