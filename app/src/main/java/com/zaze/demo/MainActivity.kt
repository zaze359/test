package com.zaze.demo

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.databinding.ActivityMainBinding
import com.zaze.demo.feature.settings.SettingsActivity
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.AndroidEntryPoint


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */
@AndroidEntryPoint
class MainActivity : AbsActivity() {
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.drawerLayout.isDrawerOpen(binding.leftNavView)) {
                binding.drawerLayout.closeDrawer(binding.leftNavView)
            } else {
                finishAffinity()
            }
        }
    }

    override val showLifecycle: Boolean
        get() = true

//    override fun getPermissionsToRequest(): Array<String> {
//        return arrayOf(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.INTERNET,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.appbarLayout.toolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        setupDrawerLayout()
        // ------------------------------------------------------
        viewModel.fragmentListData.observe(this, Observer {
            binding.viewpager.adapter = MyPagerAdapter(supportFragmentManager, it)
        })
        binding.viewpager.setOnHoverListener { _, _ ->
            ZLog.i(ZTag.TAG_DEBUG, "main_viewpager on hover")
            true
        }
        viewModel.loadFragments()
    }

    private fun setupDrawerLayout() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.app_name,
            R.string.app_name
        ).apply {
            syncState()
        }
        binding.drawerLayout.addDrawerListener(drawerToggle)
        // --------------------------------------------------
        binding.leftNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_github_menu_item -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zaze359/test.git")
                    startActivity(intent)
                }
            }
            true
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.action_settings)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }

            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_PAGE_DOWN -> {
                ToastUtil.toast(this, "onKeyDown >> 向下翻页键 :$keyCode")
            }

            KeyEvent.KEYCODE_PAGE_UP -> {
                ToastUtil.toast(this, "onKeyDown >> 向上翻页键 :$keyCode")
            }

            else -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    // --------------------------------------------------
    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<AbsFragment>?) :
        FragmentStatePagerAdapter(fm) {
        private val fragmentList = ArrayList<AbsFragment>()

        init {
            fragmentList.clear()
            if (list != null) {
                fragmentList.addAll(list)
            }
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {
//            return POSITION_NONE
            return super.getItemPosition(`object`)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return super.instantiateItem(container, position)
        }
    }
}