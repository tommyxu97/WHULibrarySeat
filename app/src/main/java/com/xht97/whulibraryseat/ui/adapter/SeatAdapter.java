package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.ui.listener.ClickListener;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.ViewHolder> {

    private Context mContext;
    private List<Seat> seats;

    private ClickListener clickListener;


    public SeatAdapter(Context context, List<Seat> data) {
        mContext = context;
        seats = data;
    }

    public void updateData(List<Seat> data) {
        seats = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_seat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Seat seat = seats.get(i);

        // BugFixed: 解决RecyclerView的ViewHolder复用带来的bug
        if (seat.getStatus().equals("FREE")) {
            viewHolder.logoView.setImageResource(R.drawable.ic_menu_seat_free);
        } else {
            viewHolder.logoView.setImageResource(R.drawable.ic_menu_seat_in_use);
        }
        viewHolder.seatIdView.setText(seat.getName());
        if (!seat.isWindow()) {
            viewHolder.windowView.setVisibility(View.GONE);
        } else {
            viewHolder.windowView.setVisibility(View.VISIBLE);
        }
        if (!seat.isPower()) {
            viewHolder.chargeView.setVisibility(View.GONE);
        } else {
            viewHolder.chargeView.setVisibility(View.VISIBLE);
        }

        final int position = i;
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(position, v);
                }
            }
        });
        viewHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemLongClick(position, v);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return seats == null ? 0 : seats.size();
    }

    public Seat getSeatByPosition(int position) {
        return seats == null ? null : seats.get(position);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View rootView;
        private ImageView logoView;
        private TextView seatIdView;
        private ImageView windowView;
        private ImageView chargeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            logoView = itemView.findViewById(R.id.iv_item_seat_logo);
            seatIdView = itemView.findViewById(R.id.tv_item_seat_id);
            windowView = itemView.findViewById(R.id.iv_item_seat_window);
            chargeView = itemView.findViewById(R.id.iv_item_seat_charge);
        }
    }
}
