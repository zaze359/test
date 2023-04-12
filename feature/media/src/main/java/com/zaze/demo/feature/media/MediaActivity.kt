package com.zaze.demo.feature.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.feature.media.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMediaBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar(binding.toolbar) {
            title = "media"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        val navController = findNavController() ?: return
        // 关联 appbar
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
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
}