package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.model.bean.Building;

import java.util.List;

public class BuildingAdapter extends BaseAdapter {

    private List<Building> buildings;
    private Context mContext;

    public BuildingAdapter(Context context, List<Building> data) {
        mContext = context;
        buildings = data;
    }

    public void updateData(List<Building> data) {
        buildings = data;
    }

    @Override
    public int getCount() {
        return buildings == null ? 0 : buildings.size();
    }

    @Override
    public Object getItem(int position) {
        return buildings == null ? null : buildings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BuildingViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new BuildingViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_building, null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BuildingViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(buildings.get(position).getName());
        return convertView;
    }

    public class BuildingViewHolder {

        TextView textView;

    }
}
