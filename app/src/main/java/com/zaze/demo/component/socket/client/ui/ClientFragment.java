package com.zaze.demo.component.socket.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.base.BaseFragment;
import com.zaze.demo.R;
import com.zaze.demo.component.socket.AlarmService;
import com.zaze.demo.component.socket.BaseSocketClient;
import com.zaze.demo.component.socket.MessageType;
import com.zaze.demo.component.socket.SocketMessage;
import com.zaze.demo.component.socket.UDPSocketClient;
import com.zaze.demo.component.socket.adapter.SocketAdapter;
import com.zaze.utils.JsonUtil;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZOnClickHelper;
import com.zaze.utils.ZStringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Description .
 *
 * @author : ZAZE
 * @version : 2017-11-29 - 11:28
 */
public class ClientFragment extends BaseFragment {
    private SocketAdapter adapter;
    public static BaseSocketClient clientSocket;
    private List<SocketMessage> messageList = new ArrayList<>();
    private static final HashSet<String> inviteSet = new HashSet<>();

    private RecyclerView clientMessageRecyclerView;
    private static final long fromId = 32142;
    private static final long toId = 666L;

    /**
     * newInstance.
     *
     * @return ClientFragment
     */
    public static ClientFragment newInstance() {
        Bundle args = new Bundle();
        ClientFragment fragment = new ClientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientSocket = new UDPSocketClient(8004, new BaseSocketClient.BaseSocketFace() {
            @Override
            public void onReceiver(SocketMessage socketMessage) {
                super.onReceiver(socketMessage);
                //
//                Intent serviceIntent = new Intent(getContext(), NotificationService.class);
//                getContext().startService(serviceIntent);
                //
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
        clientSocket = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.client_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        clientMessageRecyclerView = (RecyclerView) view.findViewById(R.id.client_message_recycler_view);
        AlarmService.runAlarm = true;
        super.onViewCreated(view, savedInstanceState);
        ZOnClickHelper.setOnClickListener(view.findViewById(R.id.client_join_bt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSocket.receive();
                getContext().startService(new Intent(getContext(), AlarmService.class));
            }
        });
        ZOnClickHelper.setOnClickListener(view.findViewById(R.id.client_send_broadcast_bt),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AlarmService.runAlarm = false;
    }

    public static void send() {
        if (clientSocket != null) {
            try {
                if (!inviteSet.isEmpty()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", "客户端回执");
                    for (String addressStr : inviteSet) {
                        String[] ipHost = addressStr.split(":");
                        if (ipHost.length == 2) {
                            clientSocket.send(ipHost[0], ZStringUtil.parseInt(ipHost[1]),
                                    new SocketMessage(fromId, toId, jsonObject.toString(), MessageType.CHAT));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        SocketMessage message = JsonUtil.parseJson(event, SocketMessage.class);
        inviteSet.add(ZStringUtil.format("%s:%s", message.getAddress(), message.getPort()));
    }
}
