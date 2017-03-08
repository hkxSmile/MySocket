package com.hkxlegend.app.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkxlegend.app.R;
import com.hkxlegend.app.model.MsgBean;

import java.util.List;

/**
 * @author huangkexiang
 * @since 17/3/3
 */

public class MsgAdapter extends BaseAdapter {

    private Context context;
    private List<MsgBean> data;

    public MsgAdapter(Context context, List<MsgBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_msg, null);
            holder.msgTextView = (TextView) convertView.findViewById(R.id.msg);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MsgBean bean = data.get(position);
        String entity = bean.getMsg();
        boolean isMsgFromMe = bean.isMsgFromMe();

        if (isMsgFromMe) {
            holder.layout.setGravity(Gravity.END);
            holder.msgTextView.setBackgroundResource(R.drawable.my_msg_bg);
        } else {
            holder.layout.setGravity(Gravity.START);
            holder.msgTextView.setBackgroundResource(R.drawable.other_msg_bg);
        }

        holder.msgTextView.setText(entity);

        return convertView;
    }

    private class ViewHolder {
        LinearLayout layout;
        TextView msgTextView;
    }
}
