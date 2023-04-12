package com.zaze.demo

import androidx.lifecycle.*
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.data.entity.TableEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val s = ""

@HiltViewModel
class DemoViewModel @Inject constructor(private val demoRepository: DemoRepository) :
    AbsViewModel() {

    private val demoList = MutableLiveData<List<TableEntity>>()
    val demosData: LiveData<List<TableEntity>> = demoList

    fun refresh() {
        var a = ""
        viewModelScope.launch(Dispatchers.IO) {
            demoList.postValue(demoRepository.loadDemos())
        }
    }
}

//object DemoViewModelFactory {
//    fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return DemoViewModel(DemoRepository(Dispatchers.IO)) as T
//        }
//    }
//}
