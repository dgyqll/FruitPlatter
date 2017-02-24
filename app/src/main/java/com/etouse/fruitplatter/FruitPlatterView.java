package com.etouse.fruitplatter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/24.
 */

public class FruitPlatterView extends View {
    private int mRadius = 300; //外圆半径
    private RectF oval; //矩形
    private Paint mPaint; //扇形画笔
    private int sum = 0;  //数据和
    private Paint textPaint; //文字画笔
    private Float animatedValue = 0.0f;
    private String title;
    Map<String,Integer> mMap = new HashMap<>(); //数据集合
    List<Integer> colors = new ArrayList<>();  //颜色集合
    public FruitPlatterView(Context context) {
        this(context,null);
    }

    public FruitPlatterView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FruitPlatterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = mRadius * 2 + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = mRadius * 2 + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width,height);
        mRadius = Math.min(width - getPaddingLeft() - getPaddingRight(),height - getPaddingTop() - getPaddingBottom()) / 2;
        oval = new RectF( -mRadius ,- mRadius ,mRadius ,mRadius  );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((getWidth() - getPaddingLeft() - getPaddingRight())/2,(getHeight() - getPaddingTop() - getPaddingBottom())/2);
        paintFruitPlatter(canvas);
    }

    //画水果拼盘
    private void paintFruitPlatter(Canvas canvas) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        sum = getSum();
        float currentAngle = 0.0f;
        Set<Map.Entry<String, Integer>> entries = mMap.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String  rate;
            String name = entry.getKey();
            Integer num = entry.getValue();
            float needAngle = 0.0f;
            needAngle =  (num * 1.0f / sum ) * 360 ;
            rate = name + ":" + decimalFormat.format(needAngle / 360 * 100)  + "%";
            mPaint.setColor(colors.get(i));
            if (Math.min(needAngle, animatedValue - currentAngle) >= 0) {
                canvas.drawArc(oval,currentAngle,Math.min(needAngle - 1,animatedValue - currentAngle ),true,mPaint);
                drawText(canvas,rate,needAngle,currentAngle);
            }
            currentAngle += needAngle;
            i++;
        }
        drawCircle(canvas);
        drawCircleText(canvas);


    }

    private void drawCircleText(Canvas canvas) {
        if (!TextUtils.isEmpty(title) && title != null) {
            Rect rect = new Rect();
            textPaint.setTextSize(sp2px(12));
            textPaint.getTextBounds(title,0,title.length(),rect);
            canvas.drawText(title,0,0,textPaint);
        }

    }


    private void drawText(Canvas canvas,String text,float needAngle,float currentAngle) {
        Rect rect = new Rect();
        textPaint.setTextSize(sp2px(10));
        textPaint.getTextBounds(text,0,text.length(),rect);

        if ((currentAngle + needAngle / 2) <= 90) {
            canvas.drawText(text, (float) (mRadius * 0.8 * Math.cos(Math.toRadians(currentAngle + needAngle / 2))), (float) (mRadius * 0.8 * Math.sin(Math.toRadians(currentAngle + needAngle / 2))), textPaint);
        }

        if ((currentAngle + needAngle / 2) > 90 && (currentAngle + needAngle / 2) <= 180) {
            canvas.drawText(text, - (float) (mRadius * 0.8 * Math.cos(Math.toRadians(180 - currentAngle - needAngle / 2))), (float) (mRadius * 0.8 * Math.sin(Math.toRadians(currentAngle + needAngle / 2))), textPaint);
        }

        if ((currentAngle + needAngle / 2) > 180 &&(currentAngle + needAngle / 2) <= 270) {
            canvas.drawText(text, -(float) (mRadius * 0.8 * Math.cos(Math.toRadians(currentAngle + needAngle / 2 - 180))), -(float) (mRadius * 0.8 * Math.sin(Math.toRadians(currentAngle + needAngle / 2 - 180))), textPaint);
        }

        if ((currentAngle + needAngle / 2) > 270 && (currentAngle + needAngle / 2) <= 360) {
            canvas.drawText(text, (float) (mRadius * 0.8 * Math.cos(Math.toRadians(360 - currentAngle - needAngle / 2))), - (float) (mRadius * 0.8 * Math.sin(Math.toRadians(360 - currentAngle - needAngle / 2))), textPaint);
        }

    }


    //画中心圆
    private void drawCircle(Canvas canvas) {
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setAlpha(255);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(0,0,mRadius / 2 - 10,circlePaint);
    }


    //求数据总和
    public int getSum() {
        int sum = 0;
        Set<Map.Entry<String, Integer>> entries = mMap.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            sum += entry.getValue();
        }
        return sum;
    }



    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }



    public void startDraw(){
        ValueAnimator animator = ValueAnimator.ofFloat(0,360);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    //设置集合数据
    public void setData(Map map){
        mMap = map;
    }


    //设置颜色集合
    public void setColor(List<Integer> colors) {
        this.colors = colors;
    }


    public void setTitle(String title) {
        this.title = title;
    }


}
