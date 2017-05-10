package com.meihuayishu.vone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.modul.OnMenuItemClickListener;

/**
 * Created by Dell on 2017-2-24.
 */
public class CircleMenuLayout extends ViewGroup {

    private int mRadius;

    /**
     * Layout内child控件的默认尺寸
     */
    private final static float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单中心空间的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENDION = 1 / 3f;
    /**
     * 容器的内边距,无视该属性，如果需要使用可以用这个变量
     */
    private final static float RADIO_PADDING_LAYOUT = 1 / 12f;
    private float mPadding;
    /**
     * 判断快速移动的转动角度值
     */
    private final static int FLINGABLE_VALUE = 300;
    private int mFlingableValue = FLINGABLE_VALUE;
    /**
     * 如果移动角度达到该值，屏蔽点击
     */
    private final static int NOCLICK_VALUE = 3;

    /**
     * 布局时的开始角度
     */
    private double mStartAngel = 0;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int[] mItemImages;
    /**
     * 菜单的个数
     */
    private int mItemCount;

    /**
     * 监控按下到抬起时的旋转角度
     */
    private float mTmpAngel;

    /**
     * 监控按下到抬起所用的时间
     */
    private long mDownTime;

    /**
     * 判断是否正在滚动
     */
    private boolean isFling;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;
    private int centerW;
    private int centerH;

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
    }


    /**
     * 设置布局宽高，并测量menu item的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int reswidth = 0, resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //如果宽或高的测量模式非精确值
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            reswidth = getSuggestedMinimumWidth();//设置为背景图的宽度
            reswidth = reswidth == 0 ? getDefaultWidth() : reswidth;

            resHeight = getSuggestedMinimumHeight();
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {//如果都是精确值，直接取最小值
            reswidth = resHeight = Math.min(resHeight, reswidth);
        }
        setMeasuredDimension(reswidth, resHeight);

        //获得半径
        mRadius = Math.max(getMeasuredHeight(), getMeasuredWidth());

        //menu itme数量
        final int count = getChildCount();
        //mene item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        //menu item 测量模式
        int childMode = MeasureSpec.EXACTLY;

        //迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            //计算menu item 的尺寸，以及和设置好的模式，区队item进行测量
            int makeMeasureSpec = -1;

            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec
                        ((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENDION), childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);

        }
        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }

    /**
     * 声明Menu item点击事件接口对象
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置menu item点击事件接口
     */
    public void setmOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }


    /**
     * 设置Menu item 的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadios = mRadius;

        //开始画子控件
        final int childCount = getChildCount();

        int left, top;

        //menu item 的尺寸
        int cWidth = (int) (layoutRadios * RADIO_DEFAULT_CHILD_DIMENSION);

        //根据menu item 的个数，计算角度
        float angelDelay = 360 / (getChildCount() - 1);

        //循环遍历设置menu item 的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE) {
                continue;
            }
            mStartAngel %= 360;

            //计算中心点到menu item中心点的距离

            float tmp = layoutRadios / 2f - cWidth / 2 - mPadding;

            //tmp cosA即为menu item中心的横坐标
            left = layoutRadios / 2 + (int) Math.round(
                    (tmp * Math.cos(Math.toRadians(mStartAngel)) - 1 / 2f * cWidth));

            //tmp sinA即为menu item中心的纵坐标
            top = layoutRadios / 2 + (int) (Math.round(
                    tmp * Math.sin(Math.toRadians(mStartAngel)) - 1 / 2f * cWidth));

            child.layout(left, top, left + cWidth, top + cWidth);

            //叠加尺寸
            mStartAngel += angelDelay;
        }

        //找到中心的view，如果存在，设置onClick事件
        View cView = findViewById(R.id.id_circle_menu_item_center);

        if (cView == null) {
            return;
        }
        cView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.itemCenterClick(v);
                }
            }
        });

        //设置center item的位置
        int cl = (layoutRadios - cView.getMeasuredWidth()) / 2;
        int cr = cl + cView.getMeasuredWidth();
        cView.layout(cl, cl, cr, cr);

        centerH = cView.getMeasuredHeight();
        centerW = cView.getMeasuredWidth();

    }

    /**
     * 记录上一次的坐标
     */
    private float mLastX, mLastY;

    private AutoFlingRunnable mFlingRunnable;

    public class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float angelPerSecond) {
            this.angelPerSecond = angelPerSecond;
        }

        @Override
        public void run() {
            if ((int) Math.abs(angelPerSecond) < 20) {
                isFling = false;
                return;
            }
            isFling = true;
            //不断改变mStartAngel，让其滚动， /30是避免滚的太快
            mStartAngel += (angelPerSecond / 30);
            //逐渐减小这个值
            angelPerSecond /= 1.0666F;

//            Log.i("MSL", "run: " + mStartAngel);
            postDelayed(this, 30);
            //重新布局
            requestLayout();

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX(), y = ev.getY();

//        Log.i("MSL", "dispatchTouchEvent: " +x +","+y);


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngel = 0;

                //如果当前已经在滚动，就移除滚动的回调
                if (isFling) {
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //获得开始的角度

                //如果点击在中心图片上，就屏蔽move动作
                if ((x >= centerW && x <= centerW *2) && (y >= centerH && y <= centerH *2)){
                    mLastY = 0 ; mLastX = 0;
                    return super.dispatchTouchEvent(ev);
                }

                if (((x - mLastX) <= 20 && (x -mLastX) >= -20)&&((y - mLastY) >= 20 && (y-mLastY) <= -20)){
                    return super.dispatchTouchEvent(ev);
                }

                float start = getAngel(mLastX, mLastY);

                //获得当前的角度
                float end = getAngel(x, y);

                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngel += end - start;
                    mTmpAngel += end - start;
                } else
                // 二、三象限，色角度值是负值
                {
                    mStartAngel += start - end;
                    mTmpAngel += start - end;
                }
                requestLayout();

                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:

                //计算每秒的移动速度
                float angelPerSecond = mTmpAngel * 1000 / (System.currentTimeMillis() - mDownTime);

                //如果达到该值，那么就是在快速移动
                if (Math.abs(angelPerSecond) > mFlingableValue && !isFling) {
                    //post一个任务，去自己滚动
                    post(mFlingRunnable = new AutoFlingRunnable(angelPerSecond));

                    return true;
                }

                //如果当前速度小于屏蔽界点，屏蔽此次点击事件
                if (Math.abs(angelPerSecond) > NOCLICK_VALUE) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据当前位置计算象限
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    /**
     * 保证 action down时 return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 根据触摸位置坐标计算角度
     */
    private float getAngel(float xTouch, float yTouch) {
        double x = xTouch - mRadius / 2d;
        double y = yTouch - mRadius / 2d;
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 设置menu item的图标/文本
     */

    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImages = resIds;
        mItemTexts = texts;

        //参数检查
        if (mItemImages == null && mItemTexts == null) {
            throw new IllegalArgumentException("菜单项中，图标和文本项目至少设置其一");
        }
        //初始化mMenuCount
        mItemCount = resIds == null ? texts.length : resIds.length;
        if (resIds != null && texts != null) {
            mItemCount = Math.min(resIds.length, texts.length);
        }
        addMenuItems();
    }

    /**
     * 设置MenuItem的布局文件，必须在setMenuItemTextsAndIcons之前调用
     */
    public void setMenuItemLayoutId(int mMenuItemLayoutId) {
        this.mMenuItemLayoutId = mMenuItemLayoutId;
    }

    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

//        Log.i("MSL", "addMenuItems: " + "mItemCount=" + mItemCount);
        //根据用户设置的参数，初始化view
        for (int i = 0; i < mItemCount; i++) {
            final int j = i;
            View view = inflater.inflate(mMenuItemLayoutId, this, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
            if (mItemTexts != null) {
                TextView textView = (TextView) view.findViewById(R.id.id_circle_menu_item_text);

                textView.setVisibility(View.VISIBLE);
                textView.setText(mItemTexts[i]);

            }
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(mItemImages[i]);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }


            addView(view);
//            Log.i("MSL", "addMenuItems: " + "addView" + i);
        }
    }

    /**
     * 设置内边距的距离
     */
    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    /**
     * 获得默认该Layout 的尺寸
     */
    private int getDefaultWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }
}
