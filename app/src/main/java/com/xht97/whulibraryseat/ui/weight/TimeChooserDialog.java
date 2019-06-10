package com.xht97.whulibraryseat.ui.weight;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.xht97.whulibraryseat.R;

public class TimeChooserDialog extends Dialog {

    private Spinner startTime;
    private Spinner endTime;
    private Button button;

    public TimeChooserDialog(@NonNull Context context) {
        super(context, R.style.SeatChooserDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_time_chooser, null);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        startTime = view.findViewById(R.id.sp_start_time);
        endTime = view.findViewById(R.id.sp_end_time);
        button = view.findViewById(R.id.bt_reserve_action);

        setContentView(view);
        setCanceledOnTouchOutside(true);


    }

    public Spinner getStartTime() {
        return startTime;
    }

    public Spinner getEndTime() {
        return endTime;
    }

    public Button getButton() {
        return button;
    }
}
