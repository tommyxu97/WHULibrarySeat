package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;

import java.util.List;

public class DateAdapter extends BaseAdapter {

    private List<String> dates;
    private Context mContext;

    public DateAdapter(Context context, List<String> data) {
        mContext = context;
        dates = data;
    }

    @Override
    public int getCount() {
        return dates == null ? 0 : dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates == null ? null :dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatesViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new DatesViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_date, null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DatesViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(dates.get(position));
        return convertView;
    }

    public void updateData(List<String> data) {
        dates = data;
        notifyDataSetChanged();
    }

    public class DatesViewHolder {
        TextView textView;
    }
}
