package org.whosin.client.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.whosin.client.BuildKonfig
import org.whosin.client.core.auth.TokenExpiredManager
import org.whosin.client.core.datastore.TokenManager
import org.whosin.client.data.dto.request.ReissueTokenRequestDto
import org.whosin.client.data.dto.response.ReissueTokenResponseDto

object HttpClientFactory {
    val BASE_URL = BuildKonfig.BASE_URL
    
    fun create(
        engine: HttpClientEngine,
        tokenManager: TokenManager
    ): HttpClient {
        val httpClient = HttpClient(engine) {
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
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getAccessToken()
                        val refreshToken = tokenManager.getRefreshToken()
                        println("loadTokens 실행, Access: $accessToken, Refresh: $refreshToken")
                        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                            null
                        } else {
                            BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                        }
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
//                                "auth/reissue" // 토큰 재발급 요청
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
                        try {
                            val rt = tokenManager.getRefreshToken()
                            if (rt.isNullOrEmpty()) {
                                tokenManager.clearToken()
                                TokenExpiredManager.setTokenExpired()
                                return@refreshTokens null
                            }

                            // 토큰 재발급 요청 시에는 현재 access token을 헤더에 수동으로 추가
                            val currentAccessToken = tokenManager.getAccessToken()
                            val response = client.post("auth/reissue") {
                                setBody(ReissueTokenRequestDto(refreshToken = rt))
//                                markAsRefreshTokenRequest() // 헤더에 access token이 추가될 필요가 없는 경우에 사용
                                // 현재 access token을 헤더에 추가
                                if (!currentAccessToken.isNullOrEmpty()) {
                                    header("Authorization", "Bearer $currentAccessToken")
                                }
                            }.body<ReissueTokenResponseDto>()

                            if (response.success && response.data != null) {
                                tokenManager.saveTokens(
                                    accessToken = response.data.accessToken,
                                    refreshToken = response.data.refreshToken
                                )
                                BearerTokens(
                                    accessToken = response.data.accessToken,
                                    refreshToken = response.data.refreshToken
                                )
                            } else {
                                tokenManager.clearToken()
                                TokenExpiredManager.setTokenExpired()
                                null
                            }
                        } catch (e: Exception) {
                            tokenManager.clearToken()
                            TokenExpiredManager.setTokenExpired()
                            null
                        }
                    }
                }
            }
            install(Logging) {
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
        
        // TokenManager에 HttpClient 참조 설정
        tokenManager.setHttpClient(httpClient)
        
        return httpClient
    }
}

/**
 * HttpClient의 BearerAuthProvider에서 캐시된 토큰을 클리어하여
 * loadTokens 블록이 다시 실행되도록 합니다.
 */
fun HttpClient.invalidateAuthTokens() {
    try {
        val authProvider = this.authProvider<BearerAuthProvider>()
        requireNotNull(authProvider)
        authProvider.clearToken()
        println("BearerAuthProvider 토큰이 성공적으로 클리어되었습니다.")
    } catch (e: Exception) {
        println("토큰 클리어 중 오류 발생: ${e.message}")
    }
}
