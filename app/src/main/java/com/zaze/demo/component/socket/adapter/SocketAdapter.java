package com.zaze.demo.component.socket.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.demo.component.socket.SocketMessage;

import java.util.Collection;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-09 - 13:10
 */
public class SocketAdapter extends BaseRecyclerAdapter<SocketMessage, SocketAdapter.SocketHolder> {

    public SocketAdapter(Context context, Collection<SocketMessage> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_socket;
    }

    @Override
    public SocketHolder createViewHolder(View convertView) {
        return new SocketHolder(convertView);
    }

    @Override
    public void onBindView(SocketHolder holder, SocketMessage value, int position) {
//        String message = "";
//        try {
//            message = value.getString("message");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        holder.itemSocketMsgTv.setText(position + ". " + value.toString());
    }

    class SocketHolder extends RecyclerView.ViewHolder {
        TextView itemSocketMsgTv;

        SocketHolder(View itemView) {
            super(itemView);
            itemSocketMsgTv = findView(itemView, R.id.item_socket_msg_tv);
        }
    }
}
