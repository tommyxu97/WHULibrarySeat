package com.xht97.whulibraryseat.ui.weight;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.xht97.whulibraryseat.model.bean.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatLayoutView extends View {

    private static final String TAG = "SeatLayoutView";

    private static final String FREE_SEAT_FLAG = "FREE";

    /**
     * 存储当前场馆的位置信息
     */
    private List<Seat> seatList = new ArrayList<>();
    private Seat[][] seatLayout;
    private int rows = 0;
    private int columns = 0;

    private OnClickListener onClickListener;

    private String greenColorString = "#34A853";
    private String grayColorString = "#BDBDBD";

    private int viewWidth;
    private int viewHeight;

    private int seatWidth = (int) dpToPx(32);
    private int seatHeight = (int) dpToPx(32);
    private int space = (int) dpToPx(8);
    private int seatRadius = 15;

    private int textSize = 42;

    private Paint paint = new Paint();
    private Paint fontPaint = new Paint();

    private RectF rectF = new RectF();
    private Matrix matrix = new Matrix();
    private float scale = 1.0f;
    float[] m = new float[9];

    private boolean isScrolling = false;
    private boolean isScaling = false;

    private int downX;
    private int downY;
    private int lastX;
    private int lastY;

    private int scaleFocusX;
    private int scaleFocusY;

    public SeatLayoutView(Context context) {
        super(context);
        init();
    }

    public SeatLayoutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {

        // 设置绘制字体的画笔
        fontPaint.setAntiAlias(true);
        fontPaint.setStrokeWidth(5);
        fontPaint.setColor(Color.WHITE);
        fontPaint.setTextSize(textSize);
        fontPaint.setTextAlign(Paint.Align.CENTER);

        matrix.postTranslate(0, 0);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSeats(canvas);
    }

    @Override
    public boolean performClick() {
        return super.performClick();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
//                if (!isScaling) {
//                    int dx = x - lastX;
//                    int dy = y - lastY;
//                    matrix.postTranslate(dx, dy);
//                    invalidate();
//                }
                break;
            case MotionEvent.ACTION_UP:
                // 当用户抬起手指时，布局可以自动回弹
                autoScale();
                if (!isScaling) {
                    autoScroll();
                }
                break;
        }

        lastX = x;
        lastY = y;

        return true;
    }

    /**
     * 设置View的数据，并且重置view的变换矩阵
     */
    public void setDataAndRefresh(List<Seat> seatList, int[] layoutInfo) {
        this.seatList = seatList;
        if (layoutInfo.length != 2) {
            Log.d(TAG, "SeatLayoutView layout does not contain the correct data!");
        }
        rows = layoutInfo[0];
        columns = layoutInfo[1];
        seatLayout = new Seat[rows][columns];
        for (Seat seat: seatList) {
            seatLayout[seat.getSeatRow()][seat.getSeatColumn()] = seat;
        }

        matrix = new Matrix();
        matrix.postTranslate(0, 0);

        viewWidth = getWidth();
        viewHeight = getHeight();

        invalidate();
    }

    /**
     * 设置点击事件监听器
     */
    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void drawSeats(Canvas canvas) {

        // 将数据集中的每个座位画在画布上
        for (Seat seat: seatList) {
            int row = seat.getSeatRow();
            int col = seat.getSeatColumn();
            rectF.top = space * getMatrixScaleY() * row + seatHeight * getMatrixScaleY() * (row - 1) + getTranslateY();
            rectF.bottom = rectF.top + seatHeight * getMatrixScaleY();
            rectF.left = space * getMatrixScaleX() * col + seatWidth * getMatrixScaleX() * (col - 1) + getTranslateX();
            rectF.right = rectF.left + seatWidth * getMatrixScaleX();

            paint.setColor(Color.parseColor((seat.getStatus().equals(FREE_SEAT_FLAG)) ? greenColorString : grayColorString));
            canvas.drawRoundRect(rectF, seatRadius * getMatrixScaleX(), seatRadius * getMatrixScaleY(), paint);

            fontPaint.setTextSize(textSize * getMatrixScaleX());
            int baselineX = (int) (rectF.left + seatWidth * getMatrixScaleX() / 2);
            int baselineY = (int) (rectF.top + seatHeight * getMatrixScaleY() * 0.4f);
            canvas.drawText(seat.getName(), baselineX, baselineY, fontPaint);
        }
    }

    public interface OnClickListener{
        void onSeatClick(Seat seat);
        void onSeatLongClick(Seat seat);
    }

    private float dpToPx(float value) {
        return getResources().getDisplayMetrics().density * value;
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(),
            new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    matrix.postTranslate(-distanceX, -distanceY);
                    invalidate();
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Seat seat = findSeatByMotionEvent(e);
                    if (seat != null) {
                        onClickListener.onSeatClick(seat);
                    }

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    Seat seat = findSeatByMotionEvent(e);
                    if (seat != null) {
                        onClickListener.onSeatLongClick(seat);
                    }

                    super.onLongPress(e);
                }
            });

    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener(){
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scaleFactor = detector.getScaleFactor();

                    // 最大最小缩放倍数分别为5和0.2
                    if (getMatrixScaleX() * scaleFactor > 5) {
                        scaleFactor = 5/getMatrixScaleX();
                    }
                    if (getMatrixScaleX() * scaleFactor < 0.2) {
                        scaleFactor = 0.2f/getMatrixScaleX();
                    }

                    matrix.postScale(scaleFactor, scaleFactor, scaleFocusX, scaleFocusY);
                    invalidate();
                    return true;
                }

                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    isScaling = true;
                    scaleFocusX = (int) detector.getFocusX();
                    scaleFocusY = (int) detector.getFocusY();
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetector detector) {
                    isScaling = false;
                }
            });

    private float getTranslateX() {
        matrix.getValues(m);
        return m[Matrix.MTRANS_X];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[Matrix.MTRANS_Y];
    }

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_Y];
    }

    private void autoScroll() {

        float moveXLength = 0;
        float moveYLength = 0;

        int seatViewWidth = (int) ((columns * seatWidth + (columns + 1) * space) * getMatrixScaleX());
        int seatViewHeight = (int) ((rows * seatHeight + (rows + 1) * space) * getMatrixScaleY());

        Log.d(TAG, "ViewActualWidth:" + seatViewWidth);
        Log.d(TAG, "ViewActualHeight" + seatViewHeight);

        if (seatViewWidth < viewWidth) {
            moveXLength = - getTranslateX();
        } else {
            if (getTranslateX() > 0) {
                moveXLength = - getTranslateX();
            } else if (getTranslateX() < 0 && (getTranslateX() + seatViewWidth < viewWidth)) {
                moveXLength = viewWidth - (seatViewWidth + getTranslateX());
            }
        }
        if (seatViewHeight < viewHeight) {
            moveYLength = - getTranslateY();
        } else {
            if (getTranslateY() > 0 ) {
                moveYLength = - getTranslateY();
            } else if (getTranslateY() < 0 && (getTranslateY() + seatViewHeight < viewHeight)) {
                moveYLength = viewHeight - (seatViewHeight + getTranslateY());
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        startMoveAnimate(start, end);
    }

    private void autoScale() {
        if (getMatrixScaleX() > 2.0) {
            startScaleAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < 0.5) {
            startScaleAnimate(getMatrixScaleX(), 0.5f);
        }
    }

    private void startMoveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation moveAnimation = new MoveAnimation();
        valueAnimator.addUpdateListener(moveAnimation);
        valueAnimator.setDuration(350);
        valueAnimator.start();
    }

    private void startScaleAnimate(float currentScaleFactor, float targetScaleFactor) {
        isScaling = true;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentScaleFactor, targetScaleFactor);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        ScaleAnimation zoomAnim = new ScaleAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(350);
        valueAnimator.start();
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    private void scale(float scale) {
        float zoomFactor = scale / getMatrixScaleX();
        matrix.postScale(zoomFactor, zoomFactor, scaleFocusX, scaleFocusY);
        invalidate();
    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();

            move(p);
        }
    }

    class MoveEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class ScaleAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float temp = (Float) animation.getAnimatedValue();
            scale(temp);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isScaling = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }

    private Seat findSeatByMotionEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        // 首先转换为原始坐标下的位置
        float rawX = (x - getTranslateX()) / getMatrixScaleX();
        float rawY = (y - getTranslateY()) / getMatrixScaleY();

        Log.d(TAG, "----------------------DEBUG INFO ---------------------");
        Log.d(TAG, "Scale: "+ getMatrixScaleX());
        Log.d(TAG, "TranslateX: " + getTranslateX());
        Log.d(TAG, "TranslateY: " + getTranslateY());
        Log.d(TAG, "Click x, y: " + x + ", " + y);
        Log.d(TAG, "Click Raw x, y: " + rawX + ", " +rawY);


        // 算出对应的行列位置
        int seatColumn = (int) Math.ceil(rawX / (space + seatWidth));
        int seatRow = (int) Math.ceil(rawY / (space + seatHeight));

        if (seatRow > rows || seatColumn > columns) return null;
        return seatLayout[seatRow][seatColumn];
    }
}
