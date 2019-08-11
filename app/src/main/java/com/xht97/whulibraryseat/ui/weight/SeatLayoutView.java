package com.xht97.whulibraryseat.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.ui.listener.ClickListener;

import java.util.List;

public class SeatLayoutView extends View {

    private final String TAG = "SeatLayoutView";

    /**
     * 存储当前场馆的位置信息
     */
    private List<Seat> mSeatList;
    private int rows;
    private int colomns;

    public SeatLayoutView(Context context) {
        super(context);
    }

    public SeatLayoutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }



    /**
     * 设置View的数据
     */
    public void setData(List<Seat> seatList, int[] layoutInfo) {
        mSeatList = seatList;
        if (layoutInfo.length != 2) {
            Log.d(TAG, "SeatLayoutView layout does not contain the correct data!");
        }
        rows = layoutInfo[0];
        colomns = layoutInfo[1];

        invalidate();
    }

    /**
     * 设置点击事件监听器
     */
    public void setClickListener(ClickListener clickListener) {
        
    }

    private void drawSeats(Canvas canvas) {

    }
}
