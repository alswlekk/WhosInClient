package org.whosin.client.presentation.dummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.whosin.client.data.repository.DummyRepository
import org.whosin.client.core.network.ApiResult

sealed interface DummyUiState {
    data object Loading: DummyUiState
    data class Success(val joke: JokeUi): DummyUiState
    data class Error(val message: String?): DummyUiState
}

// 화면에서 실제 사용할 값들만 존재
data class JokeUi(
    val setup: String,
    val punchline: String
)

class DummyViewModel(
    private val repository: DummyRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<DummyUiState> = MutableStateFlow(DummyUiState.Loading)
    val uiState: StateFlow<DummyUiState> = _uiState

    init {
        getJoke()
    }

    fun getJoke() {
        _uiState.value = DummyUiState.Loading
        viewModelScope.launch {
            when (val result = repository.getRandomJoke()) {
                is ApiResult.Success -> {
                    val ui = JokeUi(
                        setup = result.data.setup,
                        punchline = result.data.punchline
                    )
                    _uiState.value = DummyUiState.Success(ui)
                }
                is ApiResult.Error -> {
                    val message = result.message ?: result.cause?.message
                    _uiState.value = DummyUiState.Error(message)
                }
            }
        }
    }
}