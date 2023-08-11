package com.zaze.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.databinding.ActivityMainBinding
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

    override val showLifecycle: Boolean
        get() = true

    init {
        addOnContextAvailableListener {

        }
    }

    override fun getPermissionsToRequest(): Array<String> {
        R.id.home
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
//
//        return PermissionHelper.createExternalStoragePermission(arrayOf(
////            Manifest.permission.READ_EXTERNAL_STORAGE,
////            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.INTERNET,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ))
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        setContent {
//            TestApp()
//        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupActionBar(binding.mainToolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        // ------------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawerLayout,
            R.string.app_name,
            R.string.app_name
        ).apply {
            syncState()
        }
        binding.mainDrawerLayout.addDrawerListener(drawerToggle)
        // --------------------------------------------------
        binding.mainLeftNav.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_github_menu_item -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zaze359/test.git")
                    startActivity(intent)
                }
            }
            true
        }

        // --------------------------------------------------
        binding.mainViewpager.setOnHoverListener { _, _ ->
            ZLog.i(ZTag.TAG_DEBUG, "main_viewpager on hover")
            true
        }
        // --------------------------------------------------
        viewModel.fragmentListData.observe(this, Observer {
            binding.mainViewpager.adapter = MyPagerAdapter(supportFragmentManager, it)
        })
    }

    override fun beforePermissionGranted() {
        super.beforePermissionGranted()
        binding.mainTestBtn.setOnClickListener {
            setupPermission()
        }
    }

    override fun afterPermissionGranted() {
        binding.mainTestBtn.setOnClickListener {
            viewModel.test(this)
        }
        viewModel.loadFragments()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
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

    override fun onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(binding.mainLeftNav)) {
            binding.mainDrawerLayout.closeDrawer(binding.mainLeftNav)
        } else {
            super.onBackPressed()
        }
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