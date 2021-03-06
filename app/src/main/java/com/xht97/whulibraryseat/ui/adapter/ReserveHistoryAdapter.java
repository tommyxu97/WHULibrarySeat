package com.xht97.whulibraryseat.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.impl.ReserveModelImpl;
import com.xht97.whulibraryseat.model.impl.SeatActionModelImpl;
import com.xht97.whulibraryseat.ui.fragment.ReserveHistoryFragment;

import java.util.List;

public class ReserveHistoryAdapter extends RecyclerView.Adapter<ReserveHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<ReserveHistory> reserveHistories;

    private ReserveModelImpl reserveModel = new ReserveModelImpl();
    private SeatActionModelImpl seatActionModel = new SeatActionModelImpl();
    private ReserveHistoryFragment fragment;

    public ReserveHistoryAdapter(Context context, List<ReserveHistory> data) {
        mContext = context;
        reserveHistories = data;
    }

    public void updateData(List<ReserveHistory> data) {
        reserveHistories = data;
        notifyDataSetChanged();
    }

    public void bindFragment(ReserveHistoryFragment fragment) {
        this.fragment = fragment;
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
            viewHolder.actualView.setText("实际离开：" + history.getAwayBegin());
            viewHolder.actualLayout.setVisibility(View.VISIBLE);
            viewHolder.itemView.invalidate();
        } else if ((history.getAwayBegin() == null) && (history.getAwayEnd() != null)) {
            viewHolder.actualView.setText("暂离返回：" + history.getAwayEnd());
            viewHolder.actualLayout.setVisibility(View.VISIBLE);
            viewHolder.itemView.invalidate();
        } else if ((history.getAwayBegin() != null) && (history.getAwayEnd() != null)) {
            viewHolder.actualView.setText("暂离开始和结束：" + history.getAwayBegin() + "-" +
                    history.getAwayEnd());
            viewHolder.actualLayout.setVisibility(View.VISIBLE);
            viewHolder.itemView.invalidate();
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
        } else if (history.getStat().equals("RESERVE")){
            viewHolder.button.setEnabled(true);
            viewHolder.button.setText("取消预约");
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(fragment.getActivity())
                            .setTitle("提示")
                            .setMessage("确认取消该座位的预约吗")
                            .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reserveModel.cancelSeat(history.getId(), new BasePresenter.BaseRequestCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            super.onSuccess(data);
                                            fragment.showMessage("取消预约成功");
                                            fragment.initData();

                                            fragment.getMainActivity().getReserveFragment().updateCurrentReserve();
                                        }

                                        @Override
                                        public void onError(String message) {
                                            super.onError(message);
                                            fragment.showMessage(message);
                                        }
                                    });
                                }
                            })
                            .create();
                    dialog.show();
                }
            });
        } else if (history.getStat().equals("CHECK_IN") || history.getStat().equals("AWAY")) {
            viewHolder.button.setEnabled(true);
            viewHolder.button.setText("停止使用");
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(fragment.getActivity())
                            .setTitle("提示")
                            .setMessage("确认释放当前所使用的座位吗？")
                            .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    seatActionModel.stop(new BasePresenter.BaseRequestCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            super.onSuccess(data);
                                            fragment.showMessage("停止使用座位成功");
                                            fragment.initData();

                                            fragment.getMainActivity().getReserveFragment().updateCurrentReserve();
                                        }

                                        @Override
                                        public void onError(String message) {
                                            super.onError(message);
                                            fragment.showMessage(message);
                                        }
                                    });
                                }
                            })
                            .create();
                    dialog.show();
                }
            });
        } else if (history.getStat().equals("MISS")){
            viewHolder.button.setEnabled(false);
            viewHolder.button.setText("失约");
        } else {
            viewHolder.button.setEnabled(false);
            viewHolder.button.setText("已完成");
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
