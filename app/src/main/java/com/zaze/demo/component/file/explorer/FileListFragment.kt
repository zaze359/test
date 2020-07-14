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
import com.zaze.demo.R
import com.zaze.demo.component.file.explorer.adapter.FileAdapter
import kotlinx.android.synthetic.main.file_list_frag.*
import java.io.File

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.file_list_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fileListData.observe(viewLifecycleOwner, Observer {
            showFileList(it)
        })
        viewModel.curFileData.observe(viewLifecycleOwner, Observer {
            fileListBackTv.text = it.absolutePath
        })
        fileListBackTv.setOnClickListener {
            viewModel.back()
        }
        viewModel.init()
    }

    fun showFileList(fileList: List<File>?) {
        activity?.let { context ->
            adapter?.setDataList(fileList) ?: let {
                adapter = FileAdapter(context, fileList).also {
                    it.setViewMode(viewModel)
                }
                fileListRecycler.setLayoutManager(LinearLayoutManager(context))
                fileListRecycler.setAdapter(adapter)
            }
        }
    }
}