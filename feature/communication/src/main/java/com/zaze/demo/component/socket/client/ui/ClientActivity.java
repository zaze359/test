package com.zaze.demo.component.socket.client.ui;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.component.socket.BaseSocketClient;
import com.zaze.demo.component.socket.SocketMessage;
import com.zaze.demo.component.socket.UDPSocketClient;
import com.zaze.demo.component.socket.adapter.SocketAdapter;
import com.zaze.demo.feature.communication.R;
import com.zaze.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-29 - 15:03
 */
public class ClientActivity extends BaseActivity {
    private SocketAdapter adapter;
    private BaseSocketClient inviteSocket;
    private List<SocketMessage> list = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RecyclerView clientInviteRecyclerView;
    private DrawerLayout clientDrawerLayout;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        toolbar = findViewById(R.id.client_toolbar);
        clientInviteRecyclerView = findViewById(R.id.client_invite_recycler_view);
        clientDrawerLayout = findViewById(R.id.client_drawer_layout);
        setupToolbar();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.client_content_frame, ClientFragment.newInstance())
                .commit();
        // --------------------------------------------------
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        wakeLock.acquire();
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

        mDrawerToggle = new ActionBarDrawerToggle(this, clientDrawerLayout, toolbar, R.string.communication, R.string.communication) {
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
        clientDrawerLayout.setDrawerListener(mDrawerToggle);
//        clientDrawerLayout.addDrawerListener(mDrawerToggle);
        // --------------------------------------------------
        inviteSocket = new UDPSocketClient("224.0.0.1", 8003, new BaseSocketClient.BaseSocketFace() {
            @Override
            protected void onPresence(SocketMessage socketMessage) {
                super.onPresence(socketMessage);
                list.add(socketMessage);
//                EventBus.getDefault().post(JsonUtil.objToJson(socketMessage));
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showServerInviteList(list);
                    }
                });
            }
        });
        inviteSocket.receive();
    }

    @Override
    protected void onDestroy() {
        inviteSocket.close();
        wakeLock.release();
        super.onDestroy();
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
//        inflater.inflate(R.menu.menu_main, menu);
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
