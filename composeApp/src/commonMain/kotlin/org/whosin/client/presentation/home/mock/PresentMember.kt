package org.whosin.client.presentation.home.mock

data class PresentMember(
    val nickname: String,
    val isMe: Boolean = false,
)

val sampleUsers = listOf(
    PresentMember("김나은", isMe = true),
    PresentMember("김윤서"),
    PresentMember("신종윤"),
    PresentMember("조규빈"),
    PresentMember("조익성"),
    PresentMember("채민지"),
    PresentMember("현재우"),
    PresentMember("김나은1", isMe = true),
    PresentMember("김윤서"),
    PresentMember("신종윤2"),
    PresentMember("조규빈12"),
    PresentMember("조익성123"),
    PresentMember("채민지"),
    PresentMember("현재우1"),
)
