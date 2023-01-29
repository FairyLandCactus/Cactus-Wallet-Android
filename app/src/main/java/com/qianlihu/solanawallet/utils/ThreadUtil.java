package com.qianlihu.solanawallet.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author : Duan
 * date   : 2022/4/610:16
 * desc   : 多线程处理耗时
 * version: 1.0.0
 */
public class ThreadUtil {

    private ExecutorService services;

    public static ThreadUtil thread;

    public static ThreadUtil getInstance() {
        if (thread == null) {
            thread = new ThreadUtil();
        }
        return thread;
    }

    public ThreadUtil() {
        services = Executors.newSingleThreadExecutor();
    }

    public void add(final Method method, final Object _class) {
        services.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(_class);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
