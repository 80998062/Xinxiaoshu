package com.xinshu.xinxiaoshu.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.sinyuk.floating.FloatingMenu;
import com.sinyuk.floating.FloatingWindowManager;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.features.reception.ReceptionActivity;
import com.xinshu.xinxiaoshu.features.upload.UploadActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class PTRService extends IntentService implements FloatingMenu.ItemClickListener {
    public static final String TAG = "FloatingMenu";
    private static final int ONGOING_NOTIFICATION_ID = 0x123;
    private static final long SWIPE_INTERVAL = 400;
    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;
    private FloatingWindowManager floatingWindowManager;

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

//        EventBus.getDefault().register(this);

        floatingWindowManager = FloatingWindowManager.get(getApplicationContext());

        // If we get killed, after returning from here, restart

        showAndRegister();

        if (timer == null) {
            timer = new Timer();
        }

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setAutoCancel(true);
        builder.setContentTitle(getString(R.string.fetch_data_title));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_warning));
            builder.setLargeIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_warning_red));
        }

        Intent notificationIntent = new Intent(getApplicationContext(), UploadActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        builder.setWhen(SystemClock.currentThreadTimeMillis());
        builder.setShowWhen(false);

        builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_red));

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        startForeground(ONGOING_NOTIFICATION_ID, notification);


        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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


    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }


    public void resumeTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.schedule(new RefreshTask(), 0, SWIPE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeAndUnregister();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;

        handler.removeCallbacksAndMessages(null);

        stopForeground(true);
    }

    // Calling startActivity() from outside of an Activity  context
    // requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?

    @Override
    public void onClick(View view, int index) {
        switch (index) {
            case 0: {
                Log.d(TAG, "onClick: ");

                break;
            }
            case 1: {
                Log.d(TAG, "onClick: home");
                Intent intent = new Intent(getApplicationContext(), ReceptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
            case 2: {
                Log.d(TAG, "onClick: upload");
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
            case 3: {
                // 开启定时器，每隔0.5秒刷新一次
                if (view.isActivated()) {
                    resumeTimer();
                } else {
                    pauseTimer();
                }

                break;
            }

        }
    }

    @Override
    public void onExpanded(boolean expanded) {
        Log.d(TAG, "onExpanded: ");
    }


    private class RefreshTask extends TimerTask {
        @Override
        public void run() {

            if (isWechat()) {
                try {
                    executeCommand();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    timer.cancel();
                    timer = null;
                }
            }
        }

    }

    private void executeCommand() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream dos = new DataOutputStream(
                process.getOutputStream());

        String cmd = "input swipe 45 1000 45 255\n";
        dos.writeBytes(cmd);
        //dos.writeBytes("input keyevent 93\n");
        dos.writeBytes("exit\n");
        dos.flush();
        process.waitFor();
    }

    private void removeAndUnregister() {
        if (floatingWindowManager.isWindowShowing()) {
            if (floatingWindowManager.getFloatingMenu()
                    .isRegistered(PTRService.this)) {
                floatingWindowManager.getFloatingMenu()
                        .removeItemListener(PTRService.this);
            }

            handler.post(() -> floatingWindowManager.removeView());
        }
    }

    private void showAndRegister() {
        if (!floatingWindowManager.isWindowShowing()) {
            handler.post(() -> floatingWindowManager.addView());
        }

        if (floatingWindowManager.isWindowShowing()) {
            if (!floatingWindowManager.getFloatingMenu().isRegistered(PTRService.this)) {
                floatingWindowManager.getFloatingMenu().addItemListener(PTRService.this);
            }
        }
    }

    private boolean isWechat() {
        // TODO: 判断当前界面是微信 就显示悬浮窗
        return true;
    }


}
