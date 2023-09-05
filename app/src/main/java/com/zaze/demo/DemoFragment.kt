package com.zaze.demo

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.base.AbsFragment
import com.zaze.demo.databinding.DemoFragmentBinding
import com.zaze.demo.debug.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-03 - 10:36
 */
@AndroidEntryPoint
class DemoFragment : AbsFragment() {
    private var adapter: DemoAdapter? = null
    private val viewModel: DemoViewModel by viewModels()

    override fun getPermissionsToRequest(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DemoFragmentBinding.inflate(inflater, container, false)
        viewModel.demosData.observe(viewLifecycleOwner) { list ->
            adapter?.setDataList(list) ?: let {
                adapter = DemoAdapter(activity, list)
                val manager = LinearLayoutManager(context)
//            manager.spanSizeLookup = object : SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    if (position == 0) {
//                        return 2
//                    } else if (position == 1) {
//                        return 1
//                    }
//                    return 1
//                }
//            }
                binding.demoRecyclerView.layoutManager = manager
                binding.demoRecyclerView.adapter = adapter
                binding.demoRecyclerView.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            binding.demoRefreshLayout.isRefreshing = false
        }
        binding.demoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.testButton.hide()
                } else if (dy < 0) {
                    binding.testButton.show()
                }
            }
        })

        binding.demoRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        binding.demoRefreshLayout.post {
            binding.demoRefreshLayout.isRefreshing = true
            viewModel.refresh()
        }
        binding.testButton.setOnClickListener {
            viewModel.test(requireActivity())
        }
        return binding.root
    }
}