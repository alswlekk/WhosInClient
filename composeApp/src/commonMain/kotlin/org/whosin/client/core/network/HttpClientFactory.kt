package org.whosin.client.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.whosin.client.BuildKonfig
import org.whosin.client.data.dto.request.ReissueTokenRequestDto
import org.whosin.client.data.dto.response.TokenDto

object HttpClientFactory {
    val BASE_URL = BuildKonfig.BASE_URL
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Auth){
                bearer {
                    loadTokens {
                        // TODO: 이후에 datastore를 통해 토큰 가져우는 로직 추가
                        BearerTokens("accessToken", "refreshToken")
                    }
                    refreshTokens {
                        // TODO: 이후에 refreshtoken을 통해 access token 재발급하는 로직 추가
                        val response = client.post("member/reissue"){
                            setBody {
                                ReissueTokenRequestDto(
                                    refreshToken = "refreshToken"
                                )
                            }
                            markAsRefreshTokenRequest()
                        }.body<TokenDto>()
                        // TODO: 이후에 datastore를 통해 토큰 저장하는 로직 추가
                        val accessToken = response.accessToken
                        val refreshToken = response.refreshToken
                        BearerTokens(accessToken,refreshToken)
                    }
                }
            }
            install(Logging){
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.BODY
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(BASE_URL)
            }
        }
    }
}