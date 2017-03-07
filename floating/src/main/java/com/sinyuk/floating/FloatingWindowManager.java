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

import static android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;
import static android.view.WindowManager.LayoutParams.FLAG_DITHER;
import static android.view.WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class FloatingWindowManager {
    public static final String TAG = "FloatingWindowManager";

    @SuppressLint("StaticFieldLeak")
    private static FloatingWindowManager instance;

    private final Context context;
    private final int width;

    private FloatingMenu floatingMenu;

    private WindowManager mWindowManager;

    private ActivityManager mActivityManager;

    private int mScreenWidth;
    private int mScreenHeight;
    private boolean hasAdded = false;
    private int height;


    private FloatingWindowManager(Context context) {
        this.context = context;
        WindowManager windowManager = getWindowManager();
        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        height = context.getResources().getDimensionPixelOffset(R.dimen.button_radius) + dp2px(context, 16);
        width = context.getResources().getDimensionPixelOffset(R.dimen.button_radius) + dp2px(context, 16);

        Log.d(TAG, "FloatingWindowManager: ");

        Log.d(TAG, "mScreenWidth: " + mScreenWidth);
        Log.d(TAG, "mScreenHeight: " + mScreenHeight);
        Log.d(TAG, "height: " + height);

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

    public FloatingMenu getFloatingMenu() {
        return floatingMenu;
    }

    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
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

    private WindowManager.LayoutParams getDefaultLayoutParams() {

        final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();

        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        // 不设置这个弹出框的透明遮罩显示为黑色
        mLayoutParams.format = PixelFormat.RGBA_8888;

        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题


        mLayoutParams.flags = FLAG_NOT_FOCUSABLE |
                FLAG_NOT_TOUCH_MODAL |
                FLAG_WATCH_OUTSIDE_TOUCH |
                FLAG_DITHER |
                FLAG_IGNORE_CHEEK_PRESSES |
                //当此窗口为用户可见时，保持设备常开，并保持亮度不变。
                FLAG_KEEP_SCREEN_ON |
                FLAG_DIM_BEHIND;


        //当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。
        mLayoutParams.dimAmount = 0f;

        mLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        mLayoutParams.gravity = Gravity.TOP;

        mLayoutParams.width = mScreenWidth;
        mLayoutParams.height = height;

        mLayoutParams.x = 0;
        mLayoutParams.y = 0;

        mLayoutParams.packageName = "com.xinshu.xinxiaoshu";

        return mLayoutParams;
    }


    public void addView() {
        if (hasAdded)
            return;

        if (floatingMenu == null) {
            floatingMenu = new FloatingMenu(context);
            floatingMenu.setLayoutParams(getDefaultLayoutParams());
        }

        mWindowManager.addView(floatingMenu, getDefaultLayoutParams());
        hasAdded = true;
    }

    public void removeView() {
        Log.d(TAG, "removeView: ");
        if (!hasAdded) return;

        WindowManager windowManager = getWindowManager();

        windowManager.removeView(floatingMenu);
        floatingMenu = null;

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

    void toggle(boolean expanded) {

        if (floatingMenu == null) {
            Log.e(TAG, "floatingMenu is NULL!");
        }

        WindowManager.LayoutParams lps = (WindowManager.LayoutParams) floatingMenu.getLayoutParams();

        if (lps == null) {
            lps = getDefaultLayoutParams();
        }
        if (expanded) {
            lps.dimAmount = 0.45f;
        } else {
            lps.dimAmount = 0f;
        }

//        if (floatingMenu == null) {
//            floatingMenu = new FloatingMenu(context);
//            floatingMenu.setLayoutParams(lps);
//            mWindowManager.addView(floatingMenu, lps);
//        } else {
        Log.d(TAG, "updateViewLayout: ");
        mWindowManager.updateViewLayout(floatingMenu, lps);

    }

    void updatePosition(int x, int y) {
        final WindowManager.LayoutParams lps = (WindowManager.LayoutParams) floatingMenu.getLayoutParams();
        lps.x = x;
        lps.y = y;
        Log.d(TAG, "updateViewLayout: ");
        mWindowManager.updateViewLayout(floatingMenu, lps);

    }
}
