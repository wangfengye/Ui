package com.maple.mobtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mob.MobSDK;
import com.mob.ums.OperationCallback;
import com.mob.ums.SocialNetwork;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobSDK.init(getApplicationContext());
        UMSSDK.loginWithSocialAccount(SocialNetwork.WECHAT,new OperationCallback<User>(){
            @Override
            public void onSuccess(User var1) {
                Log.i(TAG, "onSuccess: "+var1.toString());
            }
            @Override
            public void onFailed(Throwable var1) {
                Log.i(TAG, "onFailed: "+var1.getMessage());
            }
            @Override
            public void onCancel() {
            }
        });
        /*UMSSDK.loginWithPhoneNumber("86", "13800138000", "123abc", new OperationCallback<User>() {
            public void onSuccess(User user) {
                // 执行成功的操作
                Log.i(TAG, "onSuccess: "+user.toString());
            }

            public void onCancel() {
                // 执行取消的操作
                Log.i(TAG, "onCancel: ");
            }

            public void onFailed(Throwable t) {
                // 提示错误信息
                Log.i(TAG, "onFailed: " + t.getMessage());
            }
        });*/

    }
}
