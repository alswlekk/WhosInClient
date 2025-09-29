package org.whosin.client.presentation.dummy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TokenTestScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: TokenTestViewModel = koinViewModel()
    val accessToken by viewModel.accessToken.collectAsStateWithLifecycle()
    val refreshToken by viewModel.refreshToken.collectAsStateWithLifecycle()

    var accessTokenInput by remember { mutableStateOf("") }
    var refreshTokenInput by remember { mutableStateOf("") }

    val hasTokens = !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "Token Manager Test",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 현재 토큰 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "현재 토큰 상태",
                )
                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "상태: ${if (hasTokens) "토큰 저장됨" else "토큰 없음"}",
                )

                if (hasTokens) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Access Token:",
                    )
                    Text(
                        text = accessToken ?: "null",
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Refresh Token:",
                    )
                    Text(
                        text = refreshToken ?: "null",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // 빠른 테스트 버튼들
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "빠른 테스트",
                )
                Spacer(modifier = Modifier.size(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveTestTokens() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("테스트 토큰 저장")
                    }

                    Button(
                        onClick = { viewModel.clearTokens() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("토큰 삭제")
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { viewModel.loadTokens() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("토큰 정보 새로고침")
                }
            }
        }

        // 커스텀 토큰 입력
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "커스텀 토큰 저장",
                )
                Spacer(modifier = Modifier.size(12.dp))

                OutlinedTextField(
                    value = accessTokenInput,
                    onValueChange = { accessTokenInput = it },
                    label = { Text("Access Token") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.size(8.dp))

                OutlinedTextField(
                    value = refreshTokenInput,
                    onValueChange = { refreshTokenInput = it },
                    label = { Text("Refresh Token") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.size(12.dp))

                Button(
                    onClick = {
                        if (accessTokenInput.isNotBlank() && refreshTokenInput.isNotBlank()) {
                            viewModel.saveCustomTokens(accessTokenInput, refreshTokenInput)
                            accessTokenInput = ""
                            refreshTokenInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = accessTokenInput.isNotBlank() && refreshTokenInput.isNotBlank()
                ) {
                    Text("커스텀 토큰 저장")
                }
            }
        }
    }
}
