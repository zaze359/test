package com.zaze.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.zaze.common.base.AbsFragment
import com.zaze.demo.debug.DividerItemDecoration
import com.zaze.demo.model.entity.TableEntity
import com.zaze.demo.viewmodels.DemoViewModel
import com.zaze.demo.viewmodels.DemoViewModelFactory
import kotlinx.android.synthetic.main.table_fragment.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-03 - 10:36
 */
class DemoFragment : AbsFragment() {
    private var adapter: DemoAdapter? = null

    private val mainViewModel: DemoViewModel by viewModels {
        DemoViewModelFactory.provideFactory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.table_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.demosData.observe(viewLifecycleOwner, Observer { tableEntities -> showAppList(tableEntities) })
        demoRefreshLayout.setOnRefreshListener { mainViewModel.refresh() }
        demoRefreshLayout.post {
            demoRefreshLayout.isRefreshing = true
            mainViewModel.refresh()
        }
    }

    fun showAppList(list: List<TableEntity?>) {

        adapter?.setDataList(list) ?: let {
            adapter = DemoAdapter(activity, list)
            val manager = GridLayoutManager(context, 4)
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position == 0) {
                        return 2
                    } else if (position == 1) {
                        return 1
                    }
                    return 1
                }
            }
            demoRecyclerView.layoutManager = manager
            demoRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            demoRecyclerView.adapter = adapter
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