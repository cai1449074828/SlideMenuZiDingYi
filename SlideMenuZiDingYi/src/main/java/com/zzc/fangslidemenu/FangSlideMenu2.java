package com.zzc.fangslidemenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2015/10/23.
 */
public class FangSlideMenu2 extends RelativeLayout{
    public FrameLayout frameLayout1;
    public FrameLayout frameLayout2;
    public FrameLayout frameLayout3;
    public FrameLayout maskframe;

    private Context context;
    private Scroller scroller;
    public static final int f1=0xaabbcc;
    public static final int f2=0xaaccbb;
    public static final int f3=0xccbbaa;
    public static final float frameLayout2_length_baifenbi=0.7f;//左边的长度
    public static final float frameLayout3_length_baifenbi=0.5f;
    public static final float frameLayout2_xiamian_baifenbi=frameLayout2_length_baifenbi*5/7;//左边被中间所盖住的长度
    public static final float frameLayout3_xiamian_baifenbi= frameLayout3_length_baifenbi*2*(0.5f-1f/7);//右边边被中间所盖住的长度
    //为了使移动frameLayout2与frameLayout3的速度一致bili=3.5
    public static final float bili=frameLayout2_length_baifenbi/(frameLayout2_length_baifenbi-frameLayout2_xiamian_baifenbi);
    public FangSlideMenu2(Context context) {
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
        addView(frameLayout2);
        addView(frameLayout3);
        addView(frameLayout1);
        addView(maskframe);
        scroller=new Scroller(context,new DecelerateInterpolator());
    }
    private int realwidth;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        frameLayout1.measure(widthMeasureSpec, heightMeasureSpec);
        realwidth=MeasureSpec.getSize(widthMeasureSpec);
        frameLayout2.measure(MeasureSpec.makeMeasureSpec((int) (realwidth*frameLayout2_length_baifenbi),MeasureSpec.EXACTLY), heightMeasureSpec);
        frameLayout3.measure(MeasureSpec.makeMeasureSpec((int) (realwidth*frameLayout3_length_baifenbi),MeasureSpec.EXACTLY), heightMeasureSpec);
        left_bianjie=-(frameLayout2.getMeasuredWidth()-realwidth*frameLayout2_xiamian_baifenbi);//左边界
        right_bianjie=frameLayout3.getMeasuredWidth()-realwidth*frameLayout3_xiamian_baifenbi;
        maskframe.measure(widthMeasureSpec, heightMeasureSpec);
    }
    private int l,t,r,b;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.l=l;
        this.t=t;
        this.r=r;
        this.b=b;
        super.onLayout(changed, l, t, r, b);
        frameLayout1.layout(l, t, r, b);
        frameLayout2.layout((int) (l+left_bianjie),0, (int) (l+frameLayout2_length_baifenbi*realwidth), b);
        frameLayout3.layout((int) (r-frameLayout3_xiamian_baifenbi*realwidth), t, (int) (r+right_bianjie), b);
        maskframe.layout(l, t, r, b);
    }
    private Point point=new Point();
    private int mov=0;
    private boolean CANCEL=false;
    private float left_bianjie;//左边界
    private float right_bianjie;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_MOVE:
                mov+=-((ev.getX()-point.x)/bili);
                point.x = (int) ev.getX();
                if(Math.abs(mov)>realwidth*0.07f/bili) {
                    CANCEL = true;
                    if (mov >right_bianjie)
                        mov = (int) right_bianjie;
                    else if (mov < left_bianjie)
                        mov = (int) left_bianjie;
                        scroller.startScroll(0, 0, mov, 0, 0);
                    invalidate();
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
//                mov=scroller.getCurrX();
                if((mov>=left_bianjie*0.5&&mov<0)||(mov<=right_bianjie*0.5&&mov>0)){scroller.startScroll(mov, 0, -mov, 0, 500);}
                else if(mov<left_bianjie*0.5&&mov>left_bianjie){scroller.startScroll(mov, 0, (int) (left_bianjie - mov), 0, 500);}
                else if(mov>right_bianjie*0.5&&mov<right_bianjie){scroller.startScroll(mov, 0, (int) (right_bianjie - mov), 0, 500);}
                else System.out.println("空");
                invalidate();
                break;
            }
            default:break;
        }
        return true;
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(!scroller.computeScrollOffset())return;
        //移动frameLayout2与frameLayout3速度均为bili=3.5
        frameLayout1.layout((int) (l - scroller.getCurrX()*bili), t, (int) (r - scroller.getCurrX()*bili), b);
        frameLayout2.layout((int) (l + left_bianjie) - scroller.getCurrX(), 0, (int) (l + frameLayout2_length_baifenbi * realwidth) - scroller.getCurrX(), b);
        frameLayout3.layout((int) (r-frameLayout3_xiamian_baifenbi*realwidth) - scroller.getCurrX(), t, (int) (r+right_bianjie) - scroller.getCurrX(), b);
        mov=scroller.getCurrX();
        postInvalidate();//请求更新scrollTo()方法使得此方法执行；此方法在执行scrollTo()后执行；
        System.out.println(scroller.getCurrX());
    }
}
