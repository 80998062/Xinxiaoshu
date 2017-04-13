package com.sinyuk.floating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class FloatingMenu extends FrameLayout {
    public static final String TAG = "FloatingMenu";

    private static final float FACTOR = 0.88f;

    private int statusBarHeight;

    private View mView;

    private List<ItemClickListener> itemListeners = new ArrayList<>();

    private final ItemClickListener internalItemListener = new ItemClickListener() {
        @Override
        public void onClick(View view, int index) {
            Log.d(TAG, "onClick: "+ index);
            switch (index) {
                case 0: {

                    break;
                }
                case 1:
                case 2: {
                    expanded = !expanded;
                    getWindowManager().toggle(expanded);
                    toggleItemVisibility(expanded);
                    internalItemListener.onExpanded(expanded);
                    notifyExpanded(expanded);
                    break;
                }
                case 3: {
                    togglePlayState(!view.isActivated());
                    break;
                }

            }
        }

        @Override
        public void onExpanded(boolean expanded) {

        }
    };

    private void togglePlayState(boolean playing) {

        mView.findViewById(R.id.homeBtn).setEnabled(!playing);
        mView.findViewById(R.id.uploadBtn).setEnabled(!playing);
        mView.findViewById(R.id.homeBtn).setClickable(!playing);
        mView.findViewById(R.id.uploadBtn).setClickable(!playing);

        mView.findViewById(R.id.playBtn).setActivated(playing);
    }

    private boolean expanded = false;

    private float xInView;
    private float yInView;
    private float xDownInScreen;
    private float yDownInScreen;
    private float xInScreen;
    private float yInScreen;
    private int mTouchSlop;
    private boolean hasMoved;


    public FloatingMenu(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mView = LayoutInflater.from(getContext()).inflate(R.layout.floating_menu, this, false);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setupItemListeners();

        setupSpring();

        addView(mView, layoutParams);

    }

    private void setupSpring() {
        if (mView == null) {
            return;
        }

//        new Actor.Builder(SpringSystem.create(), mView.findViewById(R.id.emulator))
//                .addMotion(new ToggleImitator(null, 1.0, 0.5),null)
//                .build();
    }

    private void setupItemListeners() {
        if (mView == null) {
            return;
        }

        mView.findViewById(R.id.emulator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mView.findViewById(R.id.emulator).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                        hasMoved = false;
                        xInView = event.getX();
                        yInView = event.getY();
                        xDownInScreen = event.getRawX();
                        yDownInScreen = event.getRawY() - getStatusBarHeight();
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY() - getStatusBarHeight();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY() - getStatusBarHeight();

                        if (Math.abs(xInScreen - xDownInScreen) > 2 * mTouchSlop ||
                                Math.abs(yInScreen - yDownInScreen) > 2 * mTouchSlop) {
                            // 手指移动的时候更新小悬浮窗的位置
                            updateViewPosition();
                            hasMoved = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，
                        // 则视为触发了单击事件。
                        if (!hasMoved) {
                            internalItemListener.onClick(view, 0);
                            notifyClick(view, 0);
                            expanded = !expanded;
                            getWindowManager().toggle(expanded);
                            toggleItemVisibility(expanded);
                            internalItemListener.onExpanded(expanded);
                            notifyExpanded(expanded);
                        }
                    default:
                        break;
                }
                return onTouchEvent(event);
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

        togglePlayState(false);
    }

    private void toggleItemVisibility(boolean expanded) {
        int visible = expanded ? VISIBLE : GONE;
        mView.findViewById(R.id.playBtn).setVisibility(visible);
        mView.findViewById(R.id.uploadBtn).setVisibility(visible);
        mView.findViewById(R.id.homeBtn).setVisibility(visible);
    }


    private void updateViewPosition() {
        getWindowManager().updatePosition((int) ((xInScreen - xInView) * FACTOR), (int) ((yInScreen - yInView) * FACTOR));
    }


    public void addItemListener(ItemClickListener listener) {
        if (listener != null) {
            Log.d(TAG, "addItemListener: " + listener.getClass().getSimpleName());
            itemListeners.add(listener);
        }
    }

    public void removeItemListener(ItemClickListener listener) {
        itemListeners.remove(listener);
    }

    private void notifyExpanded(boolean expanded) {
        for (int i = 0; i < itemListeners.size(); i++) {
            if (itemListeners.get(i) != null) {
                itemListeners.get(i).onExpanded(expanded);
            }
        }
    }

    private void notifyClick(View view, int index) {
        for (int i = 0; i < itemListeners.size(); i++) {
            if (itemListeners.get(i) != null) {
                Log.d(TAG, "notifyClick: ");
                itemListeners.get(i).onClick(view, index);
            }
        }
    }

    public boolean isRegistered(Object o) {
        Log.d(TAG, "isRegistered: " + o.getClass().getSimpleName());

        if (itemListeners == null || itemListeners.isEmpty()) return false;

        if (o instanceof ItemClickListener) {
            for (int i = 0; i < itemListeners.size(); i++) {
                if (itemListeners.get(i).equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeAllListeners() {
        itemListeners.clear();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllListeners();
    }

    private FloatingWindowManager getWindowManager() {
        return FloatingWindowManager.get(getContext().getApplicationContext());
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
