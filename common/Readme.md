## 常用的自定義UI控件

1. 帶刪除功能的EditText
    
```
    <!--anim:是否开启清除动画-->
    <!--drawableRight: 删除图标,必填-->
    <com.maple.common.DelEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:anim="true" 
        android:drawableLeft="@android:drawable/star_big_on"
        android:drawableRight="@android:drawable/ic_delete" />
```

| DelEditText | ParticleText | 3dTagCloudAndroid|
| :--: | :--: | :--: |
| ![](./screenshot/delEditText.gif)  | ![](./screenshot/particle.gif)|![](./screenshot/3d.gif)

2. 粒子化文字
    > `setText()` 设置初始文字><br/>
     ` setNextString()` 设置要变化的文字(连续调用改方法,会逐步变化至目标) <br/>
     `setTextSize()` 设置文字大小(单位: dp)
    
```
    <com.maple.common.particle.ParticleTextView
            android:id="@+id/ptv"
            android:layout_centerHorizontal="true"
            android:layout_width="400dp"
            android:layout_height="400dp"/>
```
3. [一个可拖动的3d标签云](https://github.com/wangfengye/3dTagCloudAndroid),在misakuo/3dTagCloudAndroid的基础上对拖动
效果做了优化,貌似原作者许久不维护了,提了pr没反应.[原作地址](https://github.com/misakuo/3dTagCloudAndroid)

| GridComputingView |DreamLayout|
| :--: | :--: |
| ![](./screenshot/brx.gif)  |  ![](./screenshot/woniu.gif) |

4. 自定义View之贝塞尔曲线.
> GridComputingView是一个绘图基类,提供网格线,及点位移动功能,继承该View,通过它提供的点位进行贝塞尔曲线绘制,可以
手动拖动点位来调整View;

5. 自定义星空图,
> 子控件随机显示在布局中