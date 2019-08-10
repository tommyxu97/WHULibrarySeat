package com.xht97.whulibraryseat.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.ui.listener.ClickListener;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private final String TAG = "RoomAdapter";

    private List<Room> rooms;
    private Context mContext;

    private ClickListener clickListener;

    public RoomAdapter(Context context, List<Room> data) {
        mContext = context;
        rooms = data;
    }

    public void updateData(List<Room> data) {
        rooms = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_room, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Room room = rooms.get(i);

        String title = room.getFloor() + "楼 " +room.getRoom();
        viewHolder.title.setText(title);
        String left = room.getFree() + "/" + room.getTotalSeat();
        viewHolder.leftSeats.setText(left);

        final int position = i;
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(position ,v);
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
        return rooms.size();
    }

    public Room getRoomByPosition(int position) {
        return rooms.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView title;
        TextView leftSeats;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            title = itemView.findViewById(R.id.tv_room_title);
            leftSeats = itemView.findViewById(R.id.tv_room_left_seat);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
