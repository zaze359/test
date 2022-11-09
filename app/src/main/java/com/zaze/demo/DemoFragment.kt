package com.zaze.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsFragment
import com.zaze.demo.databinding.TableFragmentBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = TableFragmentBinding.inflate(inflater, container, false)
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
        binding.demoRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        binding.demoRefreshLayout.post {
            binding.demoRefreshLayout.isRefreshing = true
            viewModel.refresh()
        }
        return binding.root
    }


    companion object {
        fun newInstance(title: String?): DemoFragment {
            val args = Bundle()
            args.putString("TITLE", title)
            val fragment = DemoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}