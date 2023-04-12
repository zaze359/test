package com.zaze.demo.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StateViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : AbsViewModel() {

    companion object {
        const val KEY = "StateViewModelState_Key"
    }

    private val viewModelState =
        MutableStateFlow(StateViewModelState(savedStateHandle.get<String>(KEY) ?: "default"))
    val uiState = viewModelState.map(StateViewModelState::toUiState).stateIn(
        viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
    )

    fun saveState(stateInfo: String) {
        viewModelScope.launch {
            viewModelState.update {
//                savedStateHandle.set(KEY, stateInfo)
                it.copy(stateInfo = stateInfo)
            }
        }
    }
}

private data class StateViewModelState(val stateInfo: String) {

    fun toUiState(): StateUiState {
        return StateUiState.HasState(stateInfo)
    }
}

sealed interface StateUiState {

    object None : StateUiState
    data class HasState(
        val stateInfo: String,
    ) : StateUiState
}
