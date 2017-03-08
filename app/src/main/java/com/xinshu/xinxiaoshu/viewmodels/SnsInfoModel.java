package com.xinshu.xinxiaoshu.viewmodels;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

import com.xinshu.xinxiaoshu.models.SnsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Function;

/**
 * Created by sinyuk on 2017/2/27.
 */

public class SnsInfoModel implements ViewModel<SnsInfo> {

    public static final Function<List<SnsInfo>, List<SnsInfoModel>> func = infos -> {
        final List<SnsInfoModel> models = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            models.add(new SnsInfoModel(infos.get(i)));
        }
        return models;
    };

    private SnsInfo data;

    public SnsInfoModel(SnsInfo data) {
        this.data = data;
    }

    @Override
    public SnsInfo getData() {
        return data;
    }

    @Override
    public void setData(SnsInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        data.print();
        return super.toString();
    }

    public String getAuthorName() {
        return data.authorName;
    }

    public String getContent() {
        return data.content;
    }

    public boolean getIsImage() {
        return data.mediaList != null && !data.mediaList.isEmpty();
    }

    public boolean getIsLink() {
        return !getIsImage() && TextUtils.isEmpty(data.content);
    }

    public List<String> getImages() {
        return data.mediaList;
    }

    public String getTimeStamp() {
        return data.timestamp + "";
    }

    public String getType() {
        if (getIsImage()) {
            return "image";
        } else if (getIsLink()) {
            return "link";
        }
        return "text";
    }


    /**
     *
     */
    @BindingAdapter("createdAt")
    public static void setCreatedAt(TextView textView, String timestamp) {
        long mills = Long.parseLong(timestamp) * 1000L;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.CHINA);
            Date netDate = (new Date(mills));
            textView.setText(sdf.format(netDate));
        } catch (Exception ex) {
            textView.setText("很久很久以前");
        }
    }
}
