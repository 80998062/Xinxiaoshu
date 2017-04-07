package com.xinshu.xinxiaoshu.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by sinyuk on 2017/4/6.
 */
public class PollingHelper {
    /**
     * Start polling service 开启轮询服务.
     *
     * @param context the context
     * @param seconds the seconds
     * @param cls     the cls
     * @param action  the action
     */
    public static void startPollingService(final Context context,
                                           final int seconds,
                                           final Class<?> cls,
                                           final String action) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();

        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                seconds * 1000, pendingIntent);
    }


    /**
     * Stop polling service 停止轮询服务.
     *
     * @param context the context
     * @param cls     the cls
     * @param action  the action
     */
    public static void stopPollingService(final Context context,
                                          final Class<?> cls,
                                          final String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pendingIntent);
    }

}
