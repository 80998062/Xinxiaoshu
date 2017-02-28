package com.xinshu.xinxiaoshu.features.timeline;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.utils.Sizer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PhotoCellAdapter extends RecyclerView.Adapter<PhotoCellAdapter.PhotoCellHolder> {
    private static final String PLACEHOLDER = "placeholder";
    private final List<String> picList;
    private final List<String> originalList;
    private final int size;

    PhotoCellAdapter(@NotNull List<String> picList) {
        originalList = picList;
        size = picList.size() == 1 ? 800 : 320;

        if (picList.size() == 4) {
            // 对4张图片做特殊处理
            final List<String> newList = new ArrayList<>();
            newList.add(picList.get(0));
            newList.add(picList.get(1));
            newList.add(PLACEHOLDER);
            newList.add(picList.get(2));
            newList.add(picList.get(3));
            this.picList = newList;
        } else {
            this.picList = picList;
        }
    }

    @Override
    public PhotoCellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_cell, parent, false);
        return new PhotoCellHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoCellHolder holder, int position) {

        if (PLACEHOLDER.equals(picList.get(position))) {
            holder.imageView.setVisibility(View.INVISIBLE);
            return;
        }

        Log.d("onBindViewHolder", "URL: " + picList.get(position));

        if (picList.size() == 1) {
            Glide.with(holder.imageView.getContext())
                    .load(Sizer.getUrl(picList.get(position), size))
                    .placeholder(R.color.theme_grey_lt)
                    .error(R.color.theme_grey_lt)
                    .fitCenter()
                    .crossFade(300)
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(Sizer.getUrl(picList.get(position), size))
                    .placeholder(R.color.theme_grey_lt)
                    .error(R.color.theme_grey_lt)
                    .centerCrop()
                    .crossFade(300)
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return picList == null ? 0 : picList.size();
    }

    class PhotoCellHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        PhotoCellHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.photoCell);
        }
    }
}