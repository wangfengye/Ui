package com.maple.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fengye on 2018/6/20.
 * email 1040441325@qq.com
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private ArrayList<ActivityItem> mItems;
    private OnClickListener<ActivityItem> mListener;
    public ActivityAdapter(ArrayList<ActivityItem> items) {
        mItems = items;
    }
    public void setListener(OnClickListener<ActivityItem> listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mTv.setText((position + 1) + ".  " + mItems.get(position).title);
        if (mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(mItems.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTv;
        public View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTv = itemView.findViewById(R.id.text);
        }
    }
}
