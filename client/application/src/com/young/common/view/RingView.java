package com.young.common.view;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;  
import android.view.WindowManager;
  
public class RingView extends View {  
  
    private final  Paint paint;  
    private final Context context;  
      
    public RingView(Context context) {  
          
        // TODO Auto-generated constructor stub  
        this(context, null);  
    }  
  
    public RingView(Context context, AttributeSet attrs) {  
        super(context, attrs); 
        // TODO Auto-generated constructor stub  
        setWillNotDraw(false);
        this.context = context;  
        this.paint = new Paint();  
        this.paint.setAntiAlias(true); //消除锯齿  
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆   
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
		
        int center = canvas.getWidth()/2;//width/2;  
        int radius = center*4/5;
        int top = center*15/14;
        int innerCircle = radius;//dip2px(context, radius); //设置内圆半径  
        int ringWidth = 2;//dip2px(context, 1); //设置圆环宽度  
          
        //绘制圆环  
        this.paint.setARGB(120, 255 ,255, 255);  
        this.paint.setStrokeWidth(ringWidth);  
        canvas.drawCircle(center,top, innerCircle+1+ringWidth/2, this.paint);   
          
        super.onDraw(canvas);  
    }  
      
      
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
}  
