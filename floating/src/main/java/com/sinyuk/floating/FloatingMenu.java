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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class FloatingMenu extends FrameLayout {
    public static final String TAG = "FloatingMenu";

    private int statusBarHeight;

    private WindowManager windowManager;

    private View mView;

    private List<ItemClickListener> itemListeners = new ArrayList<>();

    private final static ItemClickListener internalItemListener = new ItemClickListener() {
        @Override
        public void onClick(View view, int index) {
            Log.d(TAG, "onClick: ");
            Toast.makeText(view.getContext(), "Click at: " + index, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onExpanded(boolean expanded) {
            Log.d(TAG, "onExpanded");
        }
    };

    private FloatingWindowManager mFloatingWindowManager;

    private boolean mExpanded = false;


    public FloatingMenu(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mView = LayoutInflater.from(getContext()).inflate(R.layout.floating_menu, this, false);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setupItemListeners();

        addView(mView, layoutParams);

        mFloatingWindowManager = FloatingWindowManager.get(getContext().getApplicationContext());
    }

    private void setupItemListeners() {
        if (mView == null) {
            return;
        }

        mView.findViewById(R.id.emulator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                internalItemListener.onClick(view, 0);
                notifyClick(view, 0);
                mExpanded = !mExpanded;
                mFloatingWindowManager.toggle(mExpanded);
                internalItemListener.onExpanded(mExpanded);
                notifyExpanded(mExpanded);
            }
        });

        mView.findViewById(R.id.homeBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                internalItemListener.onClick(view, 1);
                notifyClick(view, 1);
            }
        });

        mView.findViewById(R.id.uploadBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                internalItemListener.onClick(view, 2);
                notifyClick(view, 2);
            }
        });

        mView.findViewById(R.id.playBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                internalItemListener.onClick(view, 3);
                notifyClick(view, 3);
            }
        });
    }

    private void notifyExpanded(boolean expanded) {
        for (int i = 0; i < itemListeners.size(); i++) {
            if (itemListeners.get(i) == null) {
                itemListeners.get(i).onExpanded(expanded);
            }
        }
    }

    private void notifyClick(View view, int index) {
        for (int i = 0; i < itemListeners.size(); i++) {
            if (itemListeners.get(i) == null) {
                itemListeners.get(i).onClick(view, index);
            }
        }
    }

    public void removeAllListeners() {
        itemListeners.clear();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果你设置了FLAG_NOT_TOUCH_MODAL，那么当触屏事件发生在窗口之外事，
        // 可以通过设置此标志接收到一个MotionEvent.ACTION_OUTSIDE事件。
        // 注意，你不会收到完整的down/move/up事件，
        // 只有第一次down事件时可以收到ACTION_OUTSIDE。
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            Log.d(TAG, "MotionEvent.ACTION_OUTSIDE");
            return true;
        }
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

    public interface ItemClickListener {
        void onClick(View view, int index);

        void onExpanded(boolean expanded);
    }
}