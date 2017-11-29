package com.zaze.demo.component.socket.client.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.socket.SocketClient;
import com.zaze.demo.component.socket.SocketMessage;
import com.zaze.demo.component.socket.UDPSocketClient;
import com.zaze.demo.component.socket.adapter.SocketAdapter;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZJsonUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-29 - 15:03
 */
public class ClientActivity extends ZBaseActivity {
    private SocketAdapter adapter;
    private SocketClient inviteSocket;
    private List<SocketMessage> list = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RecyclerView clientInviteRecyclerView;
    private DrawerLayout clientDrawerLayout;

    @Override
    protected boolean isNeedHead() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        toolbar = findView(R.id.client_toolbar);
        clientInviteRecyclerView = findView(R.id.client_invite_recycler_view);
        clientDrawerLayout = findView(R.id.client_drawer_layout);
        setupToolbar();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.client_content_frame, ClientFragment.newInstance())
                .commit();
//            headWidget.setBackClickListener(this)
//                .setIcon(android.R.drawable.ic_menu_add, ZOrientation.RIGHT)
//                .setOnClickListener({
//                    val drawerOpen = client_drawer_layout.isDrawerOpen(client_invite_recycler_view)
//                    if (drawerOpen) {
//                        client_drawer_layout.closeDrawers()
//                    } else {
//                        client_drawer_layout.openDrawer(client_invite_recycler_view)
//                    }
//                }, ZOrientation.RIGHT)
//        client_drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
//            override fun onDrawerStateChanged(newState: Int) {
//            }
//
//            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
//            }
//
//            override fun onDrawerClosed(drawerView: View?) {
//            }
//
//            override fun onDrawerOpened(drawerView: View?) {
//                showServerInviteList(list)
//            }
//        })

        mDrawerToggle = new ActionBarDrawerToggle(this, clientDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                showServerInviteList(list);
//                mAnimationDrawable.stop();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                mAnimationDrawable.start();
            }
        };
        mDrawerToggle.syncState();
        clientDrawerLayout.addDrawerListener(mDrawerToggle);
        // --------------------------------------------------
        inviteSocket = new UDPSocketClient("224.0.0.1", 8003, new SocketClient.SocketFace() {
            @Override
            public void onReceiver(SocketMessage socketMessage) {
                list.add(socketMessage);
                EventBus.getDefault().post(ZJsonUtil.objToJson(list));
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showServerInviteList(list);
                    }
                });
                ZLog.i(ZTag.TAG_DEBUG, "收到邀请 ： " + socketMessage);
            }
        });
        inviteSocket.receive();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inviteSocket.close();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // --------------------------------------------------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = clientDrawerLayout.isDrawerOpen(clientInviteRecyclerView);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
//            case R.id.action_websearch:
//                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // --------------------------------------------------
    private void showServerInviteList(List<SocketMessage> list) {
        if (adapter == null) {
            adapter = new SocketAdapter(this, list);
            clientInviteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            clientInviteRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }
}