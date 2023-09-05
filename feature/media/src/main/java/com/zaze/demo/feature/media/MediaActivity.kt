package com.zaze.demo.feature.media

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.contains
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.setupActionBar
import com.zaze.core.datastore.AppPreferencesDataStore
import com.zaze.core.model.data.AppNavigation
import com.zaze.demo.feature.media.databinding.ActivityMediaBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@Route(path = AppNavigation.mediaRoute)
@AndroidEntryPoint
class MediaActivity : AbsActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.appbarLayout.toolbar) {
            title = "media"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        val navController = findNavController() ?: return
//        val navGraph = navController.navInflater.inflate(R.navigation.media_nav_graph)
//        navController.graph = navGraph
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        // 需要替换默认 导航图标时，可以在 setup 后添加监听处理。
        // setup 内部也是通过 监听来替换图标的。所有我们只要在它后面就能替换掉。
        navController.addOnDestinationChangedListener(this@MediaActivity)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController() ?: return super.onSupportNavigateUp()
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun findNavController(): NavController? {
        //        // 从 fragment 获取 navController
//        return findNavController(R.id.nav_host_fragment_media)
        // 从 FragmentContainerView 获取 navController
        return (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_media) as NavHostFragment?)?.navController
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        binding.appbarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        if (controller.currentDestination?.id == controller.graph.startDestinationId) {
            binding.appbarLayout.toolbar.setNavigationOnClickListener {
                finish()
            }
        } else {
            binding.appbarLayout.toolbar.setNavigationOnClickListener {
                controller.navigateUp()
            }
        }
    }
}