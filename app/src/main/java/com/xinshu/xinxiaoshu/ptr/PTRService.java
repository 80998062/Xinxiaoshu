package com.xinshu.xinxiaoshu.ptr;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sinyuk.floating.FloatingWindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class PTRService extends IntentService {

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    public PTRService() {
        this("PTRService");
    }

    public PTRService(String name) {
        super(name);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PTRService.class);
        context.startService(starter);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return START_STICKY;
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    private class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (isWechat()) {
                if (!FloatingWindowManager.get(getApplicationContext()).isWindowShowing()) {
                    handler.post(() -> FloatingWindowManager.get(getApplicationContext()).addView());
                }
            } else {
                handler.post(() -> FloatingWindowManager.get(getApplicationContext()).removeView());
            }
        }

    }

    private boolean isWechat() {
        // TODO: 判断当前界面是微信 就显示悬浮窗

        return true;
    }
}
