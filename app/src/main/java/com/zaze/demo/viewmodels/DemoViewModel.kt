package com.zaze.demo.viewmodels

import androidx.lifecycle.*
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.model.entity.TableEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DemoViewModel(private val demoRepository: DemoRepository) : AbsViewModel() {

    private val demoList = MutableLiveData<List<TableEntity>>()
    val demosData: LiveData<List<TableEntity>> = demoList

    fun refresh() {
        viewModelScope.launch {
            demoList.value = demoRepository.loadDemos()
        }
    }
}

object DemoViewModelFactory {
    fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DemoViewModel(DemoRepository(Dispatchers.IO)) as T
        }
    }
}
