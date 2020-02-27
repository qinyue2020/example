package com.qinyue.example.base;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/26
 * 描述:
 **/
public class MySingleLiveEvent <T> extends MutableLiveData<T> {
    private static final String TAG = "SingleLiveEvent";
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public MySingleLiveEvent() {
    }

    @MainThread
    public void observe(LifecycleOwner owner, final Observer<? super T> observer) {
        if (this.hasActiveObservers()) {
            Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.");
        }

        super.observe(owner, new Observer<T>() {
            public void onChanged(@Nullable T t) {
                if (MySingleLiveEvent.this.mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }

            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        this.mPending.set(true);
        super.setValue(t);
    }

    @MainThread
    public void call() {
        this.setValue((T) null);
    }
}
