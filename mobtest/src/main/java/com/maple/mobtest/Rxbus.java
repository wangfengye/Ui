package com.maple.mobtest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maple on 2018/11/23 14:56.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class Rxbus {
    public static List<RecordingService> list =new ArrayList<>();
    public static void  add(RecordingService se){
        list.add(se);
    }
    public static void  send(){
        for (RecordingService a : list) {
            a.stopRecording();
        }
    }
}
