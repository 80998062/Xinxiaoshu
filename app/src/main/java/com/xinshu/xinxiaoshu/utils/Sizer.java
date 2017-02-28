package com.xinshu.xinxiaoshu.utils;

import android.text.TextUtils;

/**
 * Created by sinyuk on 2016/11/17.
 */

// 考虑后面加到Glide module 里面 实现不同的手机分辨率加载不同的图片
public final class Sizer {


    private static final int THUMBNAIL_SIZE_1 = 800;

    private static final int THUMBNAIL_SIZE_2 = 480;

    private static final int THUMBNAIL_SIZE_3 = 200;


    public static String getUrl(String src, final int width) {
        if (TextUtils.isEmpty(src)) {
            src = "";
        }
        if (src.contains(";")) {
            String[] splits = src.split(";");
            src = splits[0];
        }
        return "https://image.gui-quan.com/fetch?url=" + src + "&size=" + width;
    }

    public static String getUrlForGrids(String src, final int count) {
        int size;
        if (count > 4) {
            size = THUMBNAIL_SIZE_3;
        } else if (count == 4) {
            size = THUMBNAIL_SIZE_2;
        } else if (count > 1) {
            size = THUMBNAIL_SIZE_3;
        } else {
            size = THUMBNAIL_SIZE_1;
        }
        return getUrl(src, size);
    }


}
