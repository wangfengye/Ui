package com.maple.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChartActivity extends AppCompatActivity {
    private BarChart mBarChart;
    private Long c;
    private boolean pullToRight;
    private static final Long HOUR_TIME = 60*60*1000L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mBarChart = findViewById(R.id.bar);
        Log.i("Tag", "onCreate: "+getLocalIpAddress());
        initChart(mBarChart);
        c = getDayStart(System.currentTimeMillis())/HOUR_TIME;
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            c--;
            entries.add(new BarEntry(c, (float) (Math.random() * 30)));
        }
        setData(mBarChart, entries);
    }
    public static long getDayStart(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();

    }
    public String getLocalIpAddress() {
        NetworkInfo info = ((ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            }  else if (info.getType() == ConnectivityManager.TYPE_ETHERNET){
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }
    private void initChart(BarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        IAxisValueFormatter axisValueFormatter = new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("MMMdd HH");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long time = ((long) value) * 60 * 60 * 1000;
                return mFormat.format(time);

            }
        };
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        xAxis.setGranularity(1f); // one hour
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(axisValueFormatter);

        IAxisValueFormatter custom = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int va = (int) value;
                return String.valueOf(va);
            }
        };

        YAxis leftAxis = chart.getAxisLeft();

        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setSpaceBottom(0f);
        leftAxis.setAxisMaximum(60);

        chart.getAxisRight().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        chart.setOnChartGestureListener(new Load() {
            @Override
            public BarChart getBarChart() {
                return mBarChart;
            }

            @Override
            public void loadWhenScroll2Start() {
                ArrayList<BarEntry> bar = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    c--;
                    bar.add(new BarEntry(c, (float) (Math.random()*60)));
                }

                setData(getBarChart(),bar);
            }

            @Override
            public void loadWhenScroll2End() {
            }

        });
    }

    private void setData(BarChart chart, ArrayList<BarEntry> yVals1) {
        float tag = chart.getHighestVisibleX();
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(0);
            for (int i = 0; i < yVals1.size(); i++) {
                set.addEntryOrdered(yVals1.get(i));

            }

            chart.getData().notifyDataChanged();
            chart.moveViewToX(chart.getXChartMax());
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(23);

            Log.i("S", "setData: ");
        } else {
            Collections.sort(yVals1, new Comparator<BarEntry>() {
                @Override
                public int compare(BarEntry entry, BarEntry t1) {
                    return (int) (entry.getX() - t1.getX());
                }
            });
            BarDataSet set = new BarDataSet(yVals1, "dev");
            set.setDrawIcons(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(.5f);
            chart.setData(data);
            chart.setVisibleXRangeMaximum(23);
            chart.moveViewToX(chart.getXChartMax());
        }
    }

    private abstract class Load implements OnChartGestureListener{
        public abstract BarChart getBarChart();

        /**
         * 滑动到最左侧触发
         */
        public abstract void  loadWhenScroll2Start();
        /**
         * 滑动到最右侧触发
         */
        public abstract void  loadWhenScroll2End();
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            if (Math.abs(getBarChart().getLowestVisibleX() - getBarChart().getXChartMin()) <= 1) {
                // 滑动到最左端
                loadWhenScroll2Start();
            }else if (Math.abs(getBarChart().getHighestVisibleX()-getBarChart().getXChartMax())<=1){
                // 滑动到最右端
                loadWhenScroll2End();
            }
        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    }
}
