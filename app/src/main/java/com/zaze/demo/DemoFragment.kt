package com.zaze.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsFragment
import com.zaze.demo.debug.DividerItemDecoration
import com.zaze.demo.model.entity.TableEntity
import com.zaze.demo.viewmodels.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.table_fragment.*

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
    ): View? {
        return inflater.inflate(R.layout.table_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.demosData.observe(
            viewLifecycleOwner,
            Observer { tableEntities -> showAppList(tableEntities) })
        demoRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        demoRefreshLayout.post {
            demoRefreshLayout.isRefreshing = true
            viewModel.refresh()
        }
    }

    fun showAppList(list: List<TableEntity?>) {
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
            demoRecyclerView.layoutManager = manager
            demoRecyclerView.adapter = adapter
            demoRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        demoRefreshLayout.isRefreshing = false
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