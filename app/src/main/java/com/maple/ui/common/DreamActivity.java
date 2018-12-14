package com.maple.ui.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.maple.common.dreame.DreamLayout;
import com.maple.ui.R;

import java.util.ArrayList;

public class DreamActivity extends AppCompatActivity implements Runnable {
    public static final String TITLE = "蜗牛梦话圈";
    public static String[] strs = new String[]{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544782301297&di=d46fa4680d67f317b2afb55ceaa871d4&imgtype=0&src=http%3A%2F%2Fd.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F91ef76c6a7efce1b5ef04082a251f3deb58f659b.jpg"
            ,"https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=de08a1f093510fb367197197e932c893/b999a9014c086e062550d0020f087bf40bd1cbfb.jpg"
            ,"https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=91faff9259e736d147138a08ab514ffc/241f95cad1c8a786e0e3fb406a09c93d71cf50b6.jpg"
            ,"https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=bde864ccc6134954611eee64664f92dd/ac6eddc451da81cbdaec5f805f66d01609243171.jpg"};
    private Handler handler;
    private DreamAdapter adapter;
    final int[] index = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream);
        DreamLayout dreamLayout = findViewById(R.id.dream);
        ArrayList<String> urls = new ArrayList<>();
        adapter = new DreamAdapter("搜搜", urls);
        dreamLayout.setAdapter(adapter);
        handler = new Handler();

        handler.post(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        adapter.addChild(strs[index[0] % 4]);
        index[0]++;
        if (adapter.getList().size() > 3) {
            adapter.removeChild(adapter.getList().get(0));
        }
        handler.postDelayed(this, 5000);
    }
}
