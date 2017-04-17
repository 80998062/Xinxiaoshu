package com.xinshu.xinxiaoshu.viewmodels;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

/**
 * Created by sinyuk on 2017/3/31.
 */
public class UserModel implements ViewModel<UserEntity> {
    private UserEntity data;
    public static final String TAG = "UserModel";

    /**
     * Instantiates a new User model.
     *
     * @param data the data
     */
    public UserModel(UserEntity data) {
        this.data = data;
    }

    @Override
    public UserEntity getData() {
        return data;
    }

    @Override
    public void setData(UserEntity data) {
        this.data = data;
    }

    public String getAvatar() {
        return data.getHeadimgurl();
    }

    @BindingAdapter("toolbar_avatar")
    public static void setAvatar(final ImageView view, final String url) {
        Log.d("setAvatar", "url: " + url);
        Glide.with(view.getContext())
                .load(url)
                .crossFade()
                .placeholder(R.color.placeholder_color)
                .error(R.color.placeholder_color)
                .into(view);
    }

    public String getOrderCount() {
        return String.valueOf(data.getOrderCount());
    }


    public String getOrderMoney() {
        return String.valueOf(data.getOrderMoney());
    }
}
