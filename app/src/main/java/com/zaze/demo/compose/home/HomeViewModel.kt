package com.zaze.demo.compose.home

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaze.demo.compose.navigation.TopLevelDestination
import com.zaze.core.designsystem.components.snackbar.SnackbarMessage
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.data.entity.TableEntity
import com.zaze.demo.debug.test.TestFile
import com.zaze.utils.log.ZLog
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
    application: Application,
    private val demoRepository: DemoRepository
) : AndroidViewModel(application) {
    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState.map(HomeViewModelState::toUiState).stateIn(
        viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
    )

    init {
        refreshSamples()
    }

    private fun refreshSamples() {
        ZLog.i("HomeViewModel", "refreshSamples")
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val activities = demoRepository.loadDemos()
            viewModelState.update {
                it.copy(
                    destinations = TopLevelDestination.values().asList(),
                    activities = activities,
                    isLoading = false
                )
            }
        }
    }


    fun onTest() {
        listOf(
//            TestByJava(),
//            TestBsdiff(),
//            TestBattery(),
            TestFile(),
//            TestUserHandle(),
//            TestCommand(),
        ).forEach {
            it.doTest(getApplication())
        }

        val message = SnackbarMessage.WithString(
            id = UUID.randomUUID().mostSignificantBits, "Error"
        )
//        SnackbarManager.showMessage(message)
        showError(message)
    }

    private fun showError(errorMessage: SnackbarMessage) {
        ZLog.i("HomeViewModel", "showError")
        viewModelState.update {
            val errorMessages = it.errorMessages + errorMessage
            it.copy(errorMessages = errorMessages)
        }
    }

    fun errorShown(errorId: Long) {
        ZLog.i("HomeViewModel", "errorShown")
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}


private data class HomeViewModelState(
    val destinations: List<TopLevelDestination> = emptyList(),
    val activities: List<TableEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<SnackbarMessage> = emptyList(),
) {
    fun toUiState(): HomeUiState {
        return HomeUiState.HasSamples(
            destinations = destinations,
            activities = activities,
            isLoading = isLoading,
            errorMessages = errorMessages
        )
    }
}


sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<SnackbarMessage>

    data class NoPermission(
        val permission: String,
        override val isLoading: Boolean,
        override val errorMessages: List<SnackbarMessage>,
    ) : HomeUiState

    data class HasSamples(
        val destinations: List<TopLevelDestination>,
        val activities: List<TableEntity>,
        override val isLoading: Boolean,
        override val errorMessages: List<SnackbarMessage>,
    ) : HomeUiState
}
