package com.maple.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.maple.ui.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

public class SkyTagAdapter extends TagsAdapter{
    List<String> mList;
    public SkyTagAdapter(List<String> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_star,parent,false);
        TextView view1 = view.findViewById(R.id.tv_name);
        view1.setText(mList.get(position));
        view.setOnClickListener(v -> {
            Toast.makeText(context, "点击了"+mList.get(position), Toast.LENGTH_SHORT).show();});
        return view;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position%30;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
