package com.zaze.demo.component.file.explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsFragment
import com.zaze.demo.component.file.explorer.adapter.FileAdapter
import com.zaze.demo.databinding.FileListFragBinding

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-05-06 - 11:00
 */
class FileListFragment : AbsFragment() {

    private var adapter: FileAdapter? = null

    private val viewModel: FileListViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FileListViewModel() as T
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FileListFragBinding.inflate(inflater, container)
        viewModel.fileListData.observe(viewLifecycleOwner) { fileList ->
            activity?.let { context ->
                adapter?.setDataList(fileList) ?: let {
                    adapter = FileAdapter(context, fileList).also {
                        it.setViewMode(viewModel)
                    }
                    binding.fileListRecycler.layoutManager = LinearLayoutManager(context)
                    binding.fileListRecycler.adapter = adapter
                }
            }
        }
        viewModel.curFileData.observe(viewLifecycleOwner, Observer {
            binding.fileListBackTv.text = it.absolutePath
        })
        binding.fileListBackTv.setOnClickListener {
            viewModel.back()
        }
        viewModel.init()
        return binding.root
    }

}