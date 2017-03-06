package com.sinyuk.floating;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class FloatingWindowManager {
    public static final String TAG = "FloatingWindowManager";

    @SuppressLint("StaticFieldLeak")
    private static FloatingWindowManager instance;

    private final Context context;

    private FloatingMenu floatingMenu;

    private WindowManager.LayoutParams mLayoutParams;

    private WindowManager mWindowManager;

    private ActivityManager mActivityManager;

    private int mScreenWidth;
    private int mScreenHeight;
    private boolean hasAdded = false;

    public FloatingWindowManager(Context context) {
        Log.d(TAG, "FloatingWindowManager: ");
        this.context = context;
        init();
    }

    public static FloatingWindowManager get(Context context) {
        if (instance == null) {
            synchronized (FloatingWindowManager.class) {
                if (instance == null) {
                    instance = new FloatingWindowManager(context);
                }
            }
        }
        return instance;
    }

    private void init() {

        WindowManager windowManager = getWindowManager();

        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        Log.d(TAG, "init: ");
        Log.d(TAG, "mScreenWidth: " + mScreenWidth);
        Log.d(TAG, "mScreenHeight: " + mScreenHeight);

        initFloatingLayoutParams();
    }

    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private void inflateFloatingMenu() {
        Log.d(TAG, "inflateFloatingMenu: ");
        floatingMenu = new FloatingMenu(context);
        floatingMenu.setLayoutParams(mLayoutParams);
    }

    /**
     * <p>
     * 如果你设置了FLAG_NOT_TOUCH_MODAL，那么当触屏事件发生在窗口之外事，
     * 可以通过设置此标志接收到一个MotionEvent.ACTION_OUTSIDE事件。
     * 注意，你不会收到完整的down/move/up事件，
     * 只有第一次down事件时可以收到ACTION_OUTSIDE。
     * <p>
     * 如果忽略gravity属性，那么它表示窗口的绝对X位置。
     * 什么是gravity属性呢？简单地说，就是窗口如何停靠。
     * 当设置了 Gravity.LEFT 或 Gravity.RIGHT 之后，x值就表示到特定边的距离
     * </p>
     */

    private WindowManager.LayoutParams initFloatingLayoutParams() {

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        // 不设置这个弹出框的透明遮罩显示为黑色
        mLayoutParams.format = PixelFormat.RGBA_8888;

        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题


        mLayoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        //当此窗口为用户可见时，保持设备常开，并保持亮度不变。
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;


        mLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.width = mScreenWidth;
        mLayoutParams.height = dp2px(context, 50);
//        mLayoutParams.x = mScreenWidth / 2;
//        mLayoutParams.y = mScreenHeight / 2;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;

        mLayoutParams.packageName = "com.xinshu.xinxiaoshu";

        return mLayoutParams;
    }


    public void addView() {
        Log.d(TAG, "addView: ");

        if (hasAdded)
            return;

        if (floatingMenu == null) {
            inflateFloatingMenu();
        }

        mWindowManager.addView(floatingMenu, mLayoutParams);
        hasAdded = true;
    }

    public void removeView() {
        Log.d(TAG, "removeView: ");
        if (!hasAdded) return;
        if (floatingMenu != null) {
            WindowManager windowManager = getWindowManager();
            windowManager.removeView(floatingMenu);
            floatingMenu = null;
        }
        hasAdded = false;
    }


    private ActivityManager getActivityManager() {
        Log.d(TAG, "getActivityManager: ");
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }


    public boolean isWindowShowing() {
        Log.d(TAG, "isWindowShowing: ");
        return floatingMenu != null && floatingMenu.getVisibility() == View.VISIBLE;
    }


    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
