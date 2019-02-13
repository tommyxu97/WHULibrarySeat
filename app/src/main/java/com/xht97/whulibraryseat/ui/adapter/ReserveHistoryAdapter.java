package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;

import java.util.List;

public class ReserveHistoryAdapter extends RecyclerView.Adapter<ReserveHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<ReserveHistory> reserveHistories;

    private View.OnClickListener clickListener;

    public ReserveHistoryAdapter(Context context, List<ReserveHistory> data) {
        mContext = context;
        reserveHistories = data;
    }

    public void updateData(List<ReserveHistory> data) {
        reserveHistories = data;
        notifyDataSetChanged();
    }

    public void setOnButtonClickListener (View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_reserve, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ReserveHistory history = reserveHistories.get(i);

        viewHolder.locationView.setText(history.getLoc());
        viewHolder.dateView.setText(history.getDate());
        String time = history.getBegin() + "-" + history.getEnd();
        viewHolder.timeView.setText(time);
        if ((history.getAwayBegin() != null) && (history.getAwayEnd() == null)) {
            viewHolder.actualView.setText("实际离开时间为：" + history.getAwayBegin());
        } else if ((history.getAwayBegin() == null) && (history.getAwayEnd() != null)) {
            viewHolder.actualView.setText("实际暂离返回时间为：" + history.getAwayEnd());
        } else if ((history.getAwayBegin() != null) && (history.getAwayEnd() != null)) {
            viewHolder.actualView.setText("实际暂离开始和结束时间为：" + history.getAwayBegin() + "-" +
            history.getAwayEnd());
        } else {
            viewHolder.actualLayout.setVisibility(View.GONE);
        }
        if (history.getStat().equals("CANCEL")) {
            viewHolder.button.setEnabled(false);
            viewHolder.button.setText("已取消");
        } else if (history.getStat().equals("INCOMPLETE")) {
            viewHolder.button.setEnabled(false);
            viewHolder.button.setText("违约");
        } else if (history.getStat().equals("COMPLETE")) {
            viewHolder.button.setEnabled(false);
            viewHolder.button.setText("已完成");
        } else {
            viewHolder.button.setEnabled(true);
            viewHolder.button.setText("取消预约");
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(history);
                    clickListener.onClick(v);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return reserveHistories == null ? 0 : reserveHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView locationView;
        TextView dateView;
        TextView timeView;
        LinearLayout actualLayout;
        TextView actualView;
        MaterialButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            locationView = itemView.findViewById(R.id.tv_item_reserve_location);
            dateView = itemView.findViewById(R.id.tv_item_reserve_date);
            timeView = itemView.findViewById(R.id.tv_item_reserve_time);
            actualLayout = itemView.findViewById(R.id.ll_item_reserve_actual_time);
            actualView = itemView.findViewById(R.id.tv_item_reserve_actual_time);
            button = itemView.findViewById(R.id.bt_item_reserve_button);

        }
    }
}
