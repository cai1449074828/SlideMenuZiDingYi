package com.zzc.fangslidemenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ContentFrameLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2015/10/23.
 */
public class FangSlideMenu extends RelativeLayout{
    public FrameLayout frameLayout1;
    public FrameLayout frameLayout2;
    public FrameLayout frameLayout3;
    public FrameLayout maskframe;

    private Context context;
    private Scroller scroller;
    public static final int f1=0xaabbcc;
    public static final int f2=0xaaccbb;
    public static final int f3=0xccbbaa;
    public FangSlideMenu(Context context) {
        super(context);
        this.context=context;
        initView(context);
    }
    private double r_width=0.5;
    private double l_width=0.5;
    public void setLayoutWidth(double l_width,double r_width){
        this.l_width=l_width;
        this.r_width=r_width;
    }
    public void initView(Context context){
        frameLayout1=new FrameLayout(context);
        frameLayout2=new FrameLayout(context);
        frameLayout3=new FrameLayout(context);
        maskframe=new FrameLayout(context);
        frameLayout1.setBackgroundColor(Color.WHITE);
        frameLayout2.setBackgroundColor(Color.WHITE);
        frameLayout3.setBackgroundColor(Color.WHITE);
        maskframe.setBackgroundColor(0x88000000);
        maskframe.setAlpha(0);
//        frameLayout1= (FrameLayout) LayoutInflater.from(context).inflate(R.layout.c,null);
        frameLayout1.setId(f1);
        frameLayout2.setId(f2);
        frameLayout3.setId(f3);
        addView(frameLayout1);
        addView(frameLayout2);
        addView(frameLayout3);
        addView(maskframe);
        scroller=new Scroller(context,new DecelerateInterpolator());
    }
    private int realwidth;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        frameLayout1.measure(widthMeasureSpec, heightMeasureSpec);
        realwidth=MeasureSpec.getSize(widthMeasureSpec);
        frameLayout2.measure(MeasureSpec.makeMeasureSpec((int) (realwidth*l_width),MeasureSpec.EXACTLY), heightMeasureSpec);
        frameLayout3.measure(MeasureSpec.makeMeasureSpec((int) (realwidth*r_width),MeasureSpec.EXACTLY), heightMeasureSpec);
        maskframe.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        frameLayout1.layout(l, t, r, b);
        frameLayout2.layout(l-frameLayout2.getMeasuredWidth(),0, l, b);
        frameLayout3.layout(r, t,r+frameLayout3.getMeasuredWidth(), b);
        maskframe.layout(l, t, r, b);
    }
    private Point point=new Point();
    private int mov=0;
    private boolean CANCEL=false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_MOVE:
                mov+=-((int)ev.getX()-point.x);
                System.out.println((int)ev.getX()-point.x);
                point.x = (int) ev.getX();
                if(Math.abs(mov)>realwidth*0.1f) {
                    CANCEL = true;
                    if (mov > frameLayout3.getMeasuredWidth())
                        mov = frameLayout3.getMeasuredWidth();
                    else if (mov < -frameLayout2.getMeasuredWidth())
                        mov = -frameLayout2.getMeasuredWidth();
                    move();
                }
                MotionEvent motionEventMove = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis() + 100,
                        MotionEvent.ACTION_MOVE,
                        ev.getX(),
                        ev.getY(),
                        0
                );
                super.dispatchTouchEvent(motionEventMove);
                break;
            case MotionEvent.ACTION_DOWN:{
                point.x=(int)ev.getX();
                point.y=(int)ev.getY();
                MotionEvent motionEventDown = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis() + 100,
                        MotionEvent.ACTION_DOWN,
                        ev.getX(),
                        ev.getY(),
                        0
                );
                super.dispatchTouchEvent(motionEventDown);
                break;
            }
            case MotionEvent.ACTION_UP:{
                if (CANCEL==false){
                           MotionEvent motionEventUp = MotionEvent.obtain(
                                   SystemClock.uptimeMillis(),
                                   SystemClock.uptimeMillis() + 100,
                                   MotionEvent.ACTION_UP,
                                   ev.getX(),
                                   ev.getY(),
                                   0
                    );
                        super.dispatchTouchEvent(motionEventUp);
                }
                CANCEL=false;
                mov=getScrollX();
                if((mov>=-frameLayout2.getMeasuredWidth()*0.5&&mov<0)||(mov<=frameLayout3.getMeasuredWidth()*0.5&&mov>0)){scroller.startScroll(mov, 0, -mov, 0, 500);}
                if(mov<-frameLayout2.getMeasuredWidth()*0.5&&mov>-frameLayout2.getMeasuredWidth()){scroller.startScroll(mov, 0, -frameLayout2.getMeasuredWidth() - mov, 0, 500);}
                if(mov>frameLayout3.getMeasuredWidth()*0.5&&mov<frameLayout3.getMeasuredWidth()){scroller.startScroll(mov, 0, frameLayout3.getMeasuredWidth() - mov, 0, 500);}
                invalidate();
                break;
            }
            default:break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
//        maskframe.setAlpha(Math.abs((float)x/frameLayout3.getMeasuredWidth()));
    }

    private void move(){
        scrollTo( mov, 0);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(!scroller.computeScrollOffset())return;
        scrollTo(scroller.getCurrX(),0);
        mov=scroller.getCurrX();
        invalidate();
    }
}
