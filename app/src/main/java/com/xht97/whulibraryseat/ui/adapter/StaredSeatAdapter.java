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
import com.xht97.whulibraryseat.model.bean.StaredSeat;
import com.xht97.whulibraryseat.ui.listener.ClickListener;

import java.util.List;

public class StaredSeatAdapter extends RecyclerView.Adapter<StaredSeatAdapter.ViewHolder> {

    private Context context;
    private List<StaredSeat> staredSeats;

    private ClickListener clickListener;

    public StaredSeatAdapter(Context context, List<StaredSeat> staredSeats) {
        this.context = context;
        this.staredSeats = staredSeats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_stared_seat, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StaredSeat seat = staredSeats.get(i);

        // BugFixed: 解决RecyclerView的ViewHolder复用带来的bug
        viewHolder.logoView.setImageResource(R.drawable.ic_menu_seat_free);

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

        viewHolder.locationView.setText(seat.getDetailLocation());

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
        return staredSeats == null? 0: staredSeats.size();
    }

    public StaredSeat getStaredSeatByPosition(int position) {
        return staredSeats == null ? null : staredSeats.get(position);
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
        private TextView locationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            logoView = itemView.findViewById(R.id.iv_item_stared_seat_logo);
            seatIdView = itemView.findViewById(R.id.tv_item_stared_seat_id);
            windowView = itemView.findViewById(R.id.iv_item_stared_seat_window);
            chargeView = itemView.findViewById(R.id.iv_item_stared_seat_charge);
            locationView = itemView.findViewById(R.id.tv_item_stared_seat_location);
        }
    }
}
