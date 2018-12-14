package com.maple.common.dreame;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author maple on 2018/12/12 17:32.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public abstract class BaseAdapter<T, C> {
    private static final String TAG = BaseAdapter.class.getSimpleName();
    protected C center;
    private ArrayList<T> mList;
    private AdapterDataObserver mObserver;

    public ArrayList<T> getList() {
        return mList;
    }

    public BaseAdapter(C center, ArrayList<T> list) {
        this.center = center;
        this.mList = list;
    }

    public void addChild(T t) {
        // 添加View;
        mList.add(t);
        mObserver.notifyDataAdded(mList.size() - 1);
    }

    public void removeChild(T t) {
        // 添加View;
        int i = mList.indexOf(t);
        if (i == -1) {
            Log.e(TAG, "removeChild: 不存在的元素");
            return;
        }
        mList.remove(i);
        mObserver.notifyDataRemoved(i);
    }


    protected abstract View onCreateCenter(ViewGroup parent);

    protected abstract View onCreateChild(ViewGroup parent, T t);

    void registerAdapterDataObserver(AdapterDataObserver dataObserver) {
        mObserver = dataObserver;
    }

    public T getItem(int i) {
        return mList.get(i);
    }

    interface AdapterDataObserver {
        void notifyDataAdded(int i);

        void notifyDataRemoved(int i);
    }
}
