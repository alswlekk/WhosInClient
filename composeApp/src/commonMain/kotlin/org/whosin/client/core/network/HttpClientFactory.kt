package org.whosin.client.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
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
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.whosin.client.BuildKonfig
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.data.dto.request.ReissueTokenRequestDto
import org.whosin.client.data.dto.response.TokenDto

object HttpClientFactory {
    val BASE_URL = BuildKonfig.BASE_URL
    fun create(
        engine: HttpClientEngine,
        tokenManager: TokenManager
    ): HttpClient {
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
                        val accessToken = tokenManager.getAccessToken() ?: "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJhY2Nlc3MiLCJ1c2VySWQiOjUsInByb3ZpZGVySWQiOiJsb2NhbGhvc3QiLCJuYW1lIjoi7Iug7KKF7JykIiwicm9sZSI6IlJPTEVfTUVNQkVSIiwiaWF0IjoxNzU5MzgyMzg3LCJleHAiOjE3NTk5ODcxODd9.kT9IH60aCA-6ByEITb-_qPAJY0Oik1bbPKqcBWXzHIk"
                        val refreshToken = tokenManager.getRefreshToken() ?: "no_token"
                        BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                    }
                    sendWithoutRequest { request ->
                        val requestHost = request.url.host
                        val baseHost = try {
                            Url(BASE_URL).host
                        } catch (e: Exception) {
                            // BASE_URL 형식이 잘못되었을 경우를 대비한 예외 처리
                            null
                        }

                        // 요청하는 API의 host가 우리 서버의 host와 다르면 외부 API로 간주하여 토큰을 보내지 않음
                        if (requestHost != baseHost) {
                            println("External API - No Auth")
                            false
                        } else {
                            // 우리 서버로 요청하는 경우, 인증이 필요 없는 경로인지 확인
                            val path = request.url.encodedPath
                            val pathWithNoAuth = listOf(
                                "jokes",
                                "users/signup",
                                "users/find-password",
                                "auth/login",
                                "auth/email",
                                "auth/email/validation",
                                "member/reissue" // 토큰 재발급 요청 자체에는 만료된 액세스 토큰을 보내면 안 됨
                            )

                            val isNoAuthPath = pathWithNoAuth.any { noAuthPath ->
                                path.contains(noAuthPath)
                            }

                            // isNoAuthPath가 true이면 인증이 필요 없는 경로 -> 헤더를 보내지 않음 (false 반환)
                            // isNoAuthPath가 false이면 인증이 필요한 경로 -> 헤더를 보냄 (true 반환)
                            !isNoAuthPath
                        }
                    }
                    refreshTokens {
                        val rt = tokenManager.getRefreshToken() ?: "no_token"
                        val response = client.post("member/reissue"){
                            setBody {
                                ReissueTokenRequestDto(
                                    refreshToken = rt
                                )
                            }
                            markAsRefreshTokenRequest()
                        }.body<TokenDto>()
                        tokenManager.saveTokens(
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )
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
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(BASE_URL)
            }
        }
    }
}