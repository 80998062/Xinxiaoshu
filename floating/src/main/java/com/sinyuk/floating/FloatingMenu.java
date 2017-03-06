package com.sinyuk.floating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class FloatingMenu extends FrameLayout {
    public static final String TAG = "FloatingMenu";

    private int statusBarHeight;

    private WindowManager windowManager;
    private View mView;

    public FloatingMenu(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick!!!");
                Toast.makeText(getContext(), "Click me", Toast.LENGTH_SHORT).show();
            }
        });

        mView = LayoutInflater.from(getContext()).inflate(R.layout.floating_menu, this, false);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mView, layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

}
