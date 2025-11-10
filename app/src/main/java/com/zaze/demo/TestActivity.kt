package com.zaze.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.zaze.common.base.AbsActivity
import com.zaze.demo.databinding.TestActBinding
import com.zaze.demo.debug.ReArrangeableColumnDemo
import com.zaze.demo.debug.ReArrangeableRowDemo

class TestActivity : AbsActivity() {
    private lateinit var binding: TestActBinding
    val viewModel by viewModels<DemoViewModel>()

    private var adapter: DemoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val items =
                remember { mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }
            Column {
                ReArrangeableRowDemo()
                ReArrangeableColumnDemo()
              
            }
//            ReorderableHorizontalGridDemo()
//            ReorderableVerticalGridDemo()
        }

//        binding = TestActBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        viewModel.demosData.observe(this) { list ->
//            adapter?.setDataList(list) ?: let {
//                adapter = DemoAdapter(this, list)
//                val manager = LinearLayoutManager(this)
//                binding.testRecyclerView.layoutManager = manager
//                binding.testRecyclerView.adapter = adapter
//                binding.testRecyclerView.addItemDecoration(
//                    DividerItemDecoration(
//                        this,
//                        DividerItemDecoration.VERTICAL
//                    )
//                )
//            }
//        }
//        viewModel.refresh()
    }
}