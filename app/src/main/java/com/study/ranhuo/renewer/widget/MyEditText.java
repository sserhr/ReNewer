/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by ranhuo on 15/12/5.
 */
public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("hr123", "MyEditOnDraw");
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        if(this.isFocused() == true){
            Log.d("hr123", "MyEditOnDrawFocus");
            paint.setColor(Color.parseColor("#F0F0F0"));
        } else{
            Log.d("hr123", "MyEditOnDrawUnFocus");
            paint.setColor(Color.parseColor("#11cd6e"));
        }

        canvas.drawRoundRect(new RectF(2 + this.getScrollX(), 2 + this.getScrollY(), this.getWidth() - 3 + this.getScrollX(), this.getHeight() + this.getScrollY() - 1), 3, 3, paint);
        super.onDraw(canvas);
    }

}
