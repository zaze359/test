package com.zaze.demo.compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaze.demo.compose.data.Sample
import com.zaze.demo.compose.ui.components.snackbar.Message
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.model.entity.TableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-12 01:25
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val demoRepository: DemoRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState.map(HomeViewModelState::toUiState).stateIn(
        viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
    )

    init {
        refreshSamples()
    }

    fun refreshSamples() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val samples = listOf<Sample>(
//                Sample("Home") {
//                    it.navigateToHome()
//                },
                Sample("Scaffold") {
                    it.navigateToScaffold()
                },
            )
            val activities = demoRepository.loadDemos()
            viewModelState.update {
                it.copy(samples = samples, activities = activities, isLoading = false)
            }
        }
    }


    fun onTest() {
        val message = Message.WithString(
            id = UUID.randomUUID().mostSignificantBits, "Error"
        )
//        SnackbarManager.showMessage(message)
        showError(message)
    }

    private fun showError(errorMessage: Message) {
        viewModelScope.launch {
            viewModelState.update {
                val errorMessages = it.errorMessages + errorMessage
                it.copy(errorMessages = errorMessages)
            }
        }
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}


private data class HomeViewModelState(
    val samples: List<Sample> = emptyList(),
    val activities: List<TableEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<Message> = emptyList(),
) {
    fun toUiState(): HomeUiState {
        return HomeUiState.HasSamples(
            samples = samples,
            activities = activities,
            isLoading = isLoading,
            errorMessages = errorMessages
        )
    }
}


sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<Message>

    data class NoPermission(
        val permission: String,
        override val isLoading: Boolean,
        override val errorMessages: List<Message>,
    ) : HomeUiState

    data class HasSamples(
        val samples: List<Sample>,
        val activities: List<TableEntity>,
        override val isLoading: Boolean,
        override val errorMessages: List<Message>,
    ) : HomeUiState
}
