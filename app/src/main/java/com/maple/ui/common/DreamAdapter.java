package com.maple.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.maple.common.dreame.BaseAdapter;
import com.maple.ui.R;

import java.util.ArrayList;

/**
 * @author maple on 2018/12/13 14:38.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class DreamAdapter extends BaseAdapter<String, String> {
    public DreamAdapter(String center, ArrayList<String> list) {
        super(center, list);
    }

    @Override
    protected View onCreateCenter(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dream, parent, false);
        TextView tv = v.findViewById(R.id.tv);
        tv.setText(center);
        v.setOnClickListener(vu -> addChild(DreamActivity.strs[(int) (Math.random() * 4)]));
        return v;
    }

    @Override
    protected View onCreateChild(ViewGroup parent, String s) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dream_img, parent, false);
        // 设置随机色的圆形背景
     /*   GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
        drawable.setCornerRadius(1080);
        v.setBackground(drawable);*/
        ImageView img = v.findViewById(R.id.img);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.heros).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().transform(new CircleCrop());
        Glide.with(parent.getContext())
                .asBitmap()
                .load(s)
                .apply(options)
                .into(img);

        return v;

    }
}
