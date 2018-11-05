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
<img src="./screenshot/delEdittext.gif" width="240"/>
