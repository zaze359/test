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
import com.zaze.common.base.ext.ViewModelFactoryKt;
import com.zaze.demo.component.socket.AlarmService;
import com.zaze.demo.component.socket.BaseSocketClient;
import com.zaze.demo.component.socket.MessageType;
import com.zaze.demo.component.socket.SocketMessage;
import com.zaze.demo.component.socket.UDPSocketClient;
import com.zaze.demo.component.socket.adapter.SocketAdapter;
import com.zaze.demo.feature.communication.R;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZOnClickHelper;
import com.zaze.utils.ZStringUtil;

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
    private RecyclerView clientMessageRecyclerView;

    private ClientViewModel viewModel;

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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelFactoryKt.obtainViewModel(this, ClientViewModel.class);
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
                viewModel.startClient();
                getContext().startService(new Intent(getContext(), AlarmService.class));
            }
        });
        ZOnClickHelper.setOnClickListener(view.findViewById(R.id.client_send_broadcast_bt),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.send();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AlarmService.runAlarm = false;
        viewModel.stopClient();
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSocketEvent(String event) {
//        SocketMessage message = JsonUtil.parseJson(event, SocketMessage.class);
//        inviteSet.add(ZStringUtil.format("%s:%s", message.getAddress(), message.getPort()));
//    }
}
