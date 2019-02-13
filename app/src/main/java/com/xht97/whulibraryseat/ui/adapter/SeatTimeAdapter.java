package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.model.bean.SeatTime;

import java.util.List;

public class SeatTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<SeatTime> seatTimes;

    public SeatTimeAdapter(Context context, List<SeatTime> data) {
        mContext = context;
        seatTimes = data;
    }

    public void updateData(List<SeatTime> data) {
        seatTimes = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return seatTimes == null ? 0 : seatTimes.size();
    }

    @Override
    public Object getItem(int position) {
        return seatTimes == null ? null : seatTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SeatTime getSeatTimeByPosition(int position) {
        return seatTimes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_time, null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(seatTimes.get(position).getValue());
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
