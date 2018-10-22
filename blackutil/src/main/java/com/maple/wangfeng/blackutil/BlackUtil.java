package com.maple.wangfeng.blackutil;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BlackUtil {
    public static void activation(Context context) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 初始化要激活的组件
        ComponentName mDeviceAdminSample = new ComponentName(context, TestDeviceAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活可以防止随意卸载应用");
        context.startActivity(intent);
    }
}
