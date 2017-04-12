package com.xinshu.xinxiaoshu.utils.rx;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sinyuk.myutils.ConvertUtils;


/**
 * Created by Prefs on 16.1.21.
 */
public class OrderDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;


    public OrderDecoration(Context context) {
        this.mSpace = ConvertUtils.dp2px(context, 16);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (position != 0) {
            outRect.left = mSpace;

        }
    }
}
