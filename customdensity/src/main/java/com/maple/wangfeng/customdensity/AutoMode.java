package com.maple.wangfeng.customdensity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.maple.wangfeng.customdensity.CustomDensity.HEIGHT;
import static com.maple.wangfeng.customdensity.CustomDensity.WIDTH;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author maple on 2018/11/26 14:14.
 * @version v1.0
 * @see 1040441325@qq.com
 * desc 限制参数类型必须为(WIDTH,HEIGHT中一个)
 */

@Retention(SOURCE)
@IntDef({WIDTH, HEIGHT})
public @interface AutoMode {

}
