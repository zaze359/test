package com.zaze.demo.component.socket.client.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.zaze.common.base.ZBaseFragment;
import com.zaze.demo.R;
import com.zaze.demo.component.socket.SocketClient;
import com.zaze.demo.component.socket.SocketMessage;
import com.zaze.demo.component.socket.UDPSocketClient;
import com.zaze.demo.component.socket.adapter.SocketAdapter;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZJsonUtil;
import com.zaze.utils.ZOnClickHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-29 - 11:28
 */
public class ClientFragment extends ZBaseFragment {
    private SocketAdapter adapter;
    private SocketClient clientSocket;
    private List<SocketMessage> messageList = new ArrayList<>();
    private List<SocketMessage> inviteList;
    private RecyclerView clientMessageRecyclerView;

    public static ClientFragment newInstance() {
        Bundle args = new Bundle();
        ClientFragment fragment = new ClientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isNeedHead() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_client;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientSocket = new UDPSocketClient("", 8004, new SocketClient.SocketFace() {
            @Override
            public void onReceiver(SocketMessage socketMessage) {
                messageList.add(socketMessage);
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showReceiverMsg(messageList);
                    }
                });
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        clientSocket.close();
    }

    @Override
    protected void init(View view) {
        super.init(view);
        clientMessageRecyclerView = findView(R.id.client_message_recycler_view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZOnClickHelper.setOnClickListener(findView(R.id.client_join_bt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSocket.receive();
            }
        });
        ZOnClickHelper.setOnClickListener(findView(R.id.client_send_broadcast_bt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (inviteList != null && !inviteList.isEmpty()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("content", "客户端回执");
                        jsonObject.put("time", System.currentTimeMillis());
                        for (SocketMessage socketMessage : inviteList) {
                            clientSocket.send(socketMessage.getAddress(), socketMessage.getPort(), jsonObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        clientSocket.receive();
    }

    private void showReceiverMsg(List<SocketMessage> list) {
        if (adapter == null) {
            adapter = new SocketAdapter(getContext(), list);
            clientMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            clientMessageRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketEvent(String event) {
        inviteList = ZJsonUtil.parseJsonToList(event, new TypeToken<List<SocketMessage>>() {
        }.getType());
    }
}