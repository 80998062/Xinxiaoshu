package com.xinshu.xinxiaoshu.features.reception;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.view.jameson.library.CardAdapterHelper;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinyuk on 2017/4/17.
 */
public class OrderCardAdapter extends RecyclerView.Adapter<OrderCardAdapter.ViewHolder> {
    private final DrawableRequestBuilder<String> builder;
    private List<OrderEntity> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    /**
     * Instantiates a new Order card adapter.
     *
     * @param context the context
     * @param mList   the m list
     */
    public OrderCardAdapter(final Context context, final List<OrderEntity> mList) {
        this.mList = mList;
        builder = Glide.with(context)
                .fromString()
                .crossFade()
                .placeholder(R.color.placeholder_color)
                .error(R.color.placeholder_color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderEntity data = mList.get(position);
        if (data == null) return;
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        builder.load(data.getHeadimgurl()).into(holder.mAvatar);
        holder.mUsername.setText(data.getNickname());
        holder.mLocation.setText("浙江 杭州");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Swap.
     *
     * @param entities the entities
     */
    public void swap(final  List<OrderEntity> entities){
        mList.clear();
        mList.addAll(entities);
        notifyDataSetChanged();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The M avatar.
         */
        public final ImageView mAvatar;
        /**
         * The M username.
         */
        public final TextView mUsername;
        /**
         * The M location.
         */
        public final TextView mLocation;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(final View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mUsername = (TextView) itemView.findViewById(R.id.username);
            mLocation = (TextView) itemView.findViewById(R.id.location);
        }

    }

}
