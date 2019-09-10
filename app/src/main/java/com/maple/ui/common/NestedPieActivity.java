package com.maple.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.maple.common.nestedpiechart.NestPie;
import com.maple.common.nestedpiechart.NestedPieChart;
import com.maple.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hexiao
 * @Date: 2019/9/9 17:57.
 * @Description:
 */
public class NestedPieActivity extends AppCompatActivity {
    public static final String TITLE = "嵌套饼型图";
    NestedPieChart nestedPieChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_pie_chart);
        nestedPieChart=findViewById(R.id.nestedPieChart);
        initData();
    }

    private void initData() {
        List<NestPie> innerNestPies =new ArrayList<>();
        NestPie nestPie1 =new NestPie();
        nestPie1.setNumber(100);
        nestPie1.setTitle("私募");
        nestPie1.setColor(R.color.color1);
        NestPie nestPie2 =new NestPie();
        nestPie2.setNumber(130);
        nestPie2.setTitle("信托");
        nestPie2.setColor(R.color.color2);
        innerNestPies.add(nestPie1);
        innerNestPies.add(nestPie2);

        List<NestPie> outerNestPies =new ArrayList<>();
        NestPie outer1=new NestPie();
        outer1.setTitle("证券类");
        outer1.setNumber(30);
        outer1.setColor(R.color.color3);
        outer1.setLineColor(R.color.color8);
        outer1.setNumberColor(R.color.color9);
        outer1.setTextColor(R.color.color10);
        NestPie outer2=new NestPie();
        outer2.setTitle("信托计划");
        outer2.setNumber(20);
        outer2.setColor(R.color.color4);
        outer2.setLineColor(R.color.color8);
        outer2.setNumberColor(R.color.color9);
        outer2.setTextColor(R.color.color10);
        NestPie outer3=new NestPie();
        outer3.setTitle("投资类");
        outer3.setNumber(50);
        outer3.setColor(R.color.color5);
        outer3.setLineColor(R.color.color8);
        outer3.setNumberColor(R.color.color9);
        outer3.setTextColor(R.color.color10);
        NestPie outer4=new NestPie();
        outer4.setTitle("融资类");
        outer4.setNumber(60);
        outer4.setColor(R.color.color6);
        outer4.setLineColor(R.color.color8);
        outer4.setNumberColor(R.color.color9);
        outer4.setTextColor(R.color.color10);
        NestPie outer5=new NestPie();
        outer5.setTitle("事务管理");
        outer5.setNumber(70);
        outer5.setColor(R.color.color7);
        outer5.setLineColor(R.color.color8);
        outer5.setNumberColor(R.color.color9);
        outer5.setTextColor(R.color.color10);
        outerNestPies.add(outer1);
        outerNestPies.add(outer2);
        outerNestPies.add(outer3);
        outerNestPies.add(outer4);
        outerNestPies.add(outer5);

        nestedPieChart.setData(innerNestPies, outerNestPies);
    }

}
