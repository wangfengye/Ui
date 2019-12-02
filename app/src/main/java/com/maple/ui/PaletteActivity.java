package com.maple.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class PaletteActivity extends AppCompatActivity {
    public static final String TITLE = "Palette:图片颜色提取";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        //String picUrl = "https://w.wallhaven.cc/full/83/wallhaven-83pjzk.jpg";
        String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575009827129&di=f9314d2741594991902d8ff733f20b20&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg";
        // String picUrl = "http://img1.imgtn.bdimg.com/it/u=3987153828,2539495055&fm=26&gp=0.jpg";
        ImageView img = findViewById(R.id.img);
        Glide.with(this).load(picUrl).into(img);

        Glide.with(this).asBitmap().load(picUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette.from(resource).generate(p -> {
                    int defaultColor = 0x0;//默认色,用于当指定色调的颜色取不到时

                    findViewById(R.id.VibrantColor).setBackgroundColor(p.getVibrantColor(defaultColor));
                    findViewById(R.id.LightVibrantColor).setBackgroundColor(p.getLightVibrantColor(defaultColor));
                    findViewById(R.id.DarkVibrantColor).setBackgroundColor(p.getDarkVibrantColor(defaultColor));

                    findViewById(R.id.MutedColor).setBackgroundColor(p.getMutedColor(defaultColor));
                    findViewById(R.id.DarkMutedColor).setBackgroundColor(p.getDarkMutedColor(defaultColor));
                    findViewById(R.id.LightMutedColor).setBackgroundColor(p.getLightMutedColor(defaultColor));

                });

            }
        });


    }
}
