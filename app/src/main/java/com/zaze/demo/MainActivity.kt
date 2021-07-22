package com.zaze.demo

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.*
import android.view.KeyEvent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.viewmodels.MainViewModel
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */
@AndroidEntryPoint
class MainActivity : AbsActivity() {
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val viewModel: MainViewModel by viewModels()

    override fun showLifeCycle(): Boolean {
        return true
    }

    override fun getPermissionsToRequest(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar(findViewById(R.id.mainToolbar)) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        // ------------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(
            this,
            mainDrawerLayout,
            R.string.app_name,
            R.string.app_name
        ).apply {
            syncState()
        }
        mainDrawerLayout.addDrawerListener(drawerToggle)
        // --------------------------------------------------
        mainLeftNav.setNavigationItemSelectedListener { menuItem ->
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
        mainViewpager.setOnHoverListener { _, _ ->
            ZLog.i(ZTag.TAG_DEBUG, "main_viewpager on hover")
            true
        }
        // --------------------------------------------------
        viewModel.fragmentListData.observe(this, Observer {
            mainViewpager.adapter = MyPagerAdapter(supportFragmentManager, it)
        })
    }

    override fun beforePermissionGranted() {
        super.beforePermissionGranted()
        mainTestBtn.setOnClickListener {
            setupPermission()
        }
    }

    override fun afterPermissionGranted() {
        mainTestBtn.setOnClickListener {
            viewModel.test(this)
        }
        viewModel.loadFragments()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mainDrawerLayout.openDrawer(GravityCompat.START)
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
        if (mainDrawerLayout.isDrawerOpen(mainLeftNav)) {
            mainDrawerLayout.closeDrawer(mainLeftNav)
        } else {
            super.onBackPressed()
        }
    }

    // --------------------------------------------------
    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<AbsFragment>?) :
        FragmentPagerAdapter(fm) {
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
    }
}