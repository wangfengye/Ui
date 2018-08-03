package com.maple.ui.overlapping;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maple.ui.R;

/**
 * Created by fengye on 2018/8/3.
 * email 1040441325@qq.com
 */

public class OverlappingAdapter extends RecyclerView.Adapter<OverlappingAdapter.ViewHolder>{


    private final PorterDuff.Mode[] mdata;

    public OverlappingAdapter() {
        mdata = PorterDuff.Mode.values();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overlapping,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ovi.setMode(mdata[position]);
        holder.text.setText(mdata[position].name());
    }

    @Override
    public int getItemCount() {
        return mdata.length;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        OverlappingTestView ovi;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            ovi = itemView.findViewById(R.id.ovi);
            text = itemView.findViewById(R.id.tv_desc);
        }
    }
}
