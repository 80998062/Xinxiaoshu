package com.xinshu.xinxiaoshu.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sinyuk.myutils.android.AppUtils;
import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.events.OrderComingEvent;
import com.xinshu.xinxiaoshu.events.StartPollingEvent;
import com.xinshu.xinxiaoshu.events.StopPollingEvent;
import com.xinshu.xinxiaoshu.features.reception.ReceptionActivity;
import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by sinyuk on 2017/4/6.
 */
public class PollingService extends IntentService {

    /**
     * Start.
     *
     * @param context the context
     */
    public static void start(final Context context) {
        Intent starter = new Intent(context, PollingService.class);
        context.startService(starter);
    }

    /**
     * The constant TAG.
     */
    public static final String TAG = "PollingService";
    private static final long INTERVAL = 5;
    private static final int ONGOING_NOTIFICATION_ID = 0x12554;
    private static final int RING_NOTIFICATION_ID = 0x12774;
    private Observable<Long> mIntervalObservable =
            Observable.interval(INTERVAL, TimeUnit.SECONDS)
                    .doOnError(Throwable::printStackTrace);

    private Disposable mIntervalDisposable;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<OrderEntity> oldOrders = new ArrayList<>();

    /**
     * Instantiates a new Push service.
     *
     * @param repository the repository
     */
    @Inject
    RemoteDataRepository mRepository;

    /**
     * Instantiates a new Polling service.
     *
     * @param name the name
     */
    public PollingService(String name) {
        super(name);
    }

    /**
     * Instantiates a new Polling service.
     */
    public PollingService() {
        this("PollingService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        App.get(getApplicationContext()).getAppComponent().inject(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        //
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setAutoCancel(false);
        builder.setContentTitle(getString(R.string.notification_title));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_warning));
            builder.setLargeIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_warning_red));
        }

        Intent notificationIntent = new Intent(getApplicationContext(), ReceptionActivity.class);

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

        // 开始轮询
        startInterval();

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        stopInterval();

        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    /**
     * Stop interval.
     */
    public void stopInterval() {
        Log.d(TAG, "stopInterval: ");
        if (mIntervalDisposable != null) {
            mIntervalDisposable.dispose();
        }
    }

    /**
     * Start interval.
     */
    public void startInterval() {
        Log.d(TAG, "startInterval: ");
        mIntervalDisposable = mIntervalObservable.subscribeWith(
                new DisposableObserver<Long>() {
                    @Override
                    public void onNext(final Long aLong) {
                        Log.d(TAG, "request at: " + aLong);
                        mCompositeDisposable.add(request());
                    }

                    @Override
                    public void onError(final Throwable e) {
                        e.printStackTrace();
                        stopInterval();
                    }

                    @Override
                    public void onComplete() {
                        stopInterval();
                    }
                });

    }


    /**
     * Request.
     */
    private Disposable request() {
        return mRepository
                .requestOrder()
                .doOnError(Throwable::printStackTrace)
                .onErrorReturnItem(new ArrayList<>())
                .subscribe(orders -> {
                    OrderComingEvent event = new OrderComingEvent(orders);
                    event.setExecutionScope(PollingService.class);
                    EventBus.getDefault().post(event);
                    if (!Objects.equals(orders.toString(), oldOrders.toString())) {
                        if (!AppUtils.isAppForeground(getApplicationContext())) {
                            ring();
                        } else {
                            Toast.makeText(getApplicationContext(), "您有新的微信书订单啦", Toast.LENGTH_SHORT).show();
                        }
                        oldOrders = orders;
                    }
                });
    }

    private void ring() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("您有新的微信书订单啦")
                        .setContentText("点击查看");
        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(this, ReceptionActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(ReceptionActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
//        mBuilder.setOngoing(true);
//        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mBuilder.setSound(alarmSound);

        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
//        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(RING_NOTIFICATION_ID, note);

    }


    /**
     * On start event.
     *
     * @param event the event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartEvent(final StartPollingEvent event) {
        startInterval();
    }


    /**
     * On stop event.
     *
     * @param event the event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopEvent(final StopPollingEvent event) {
        stopInterval();
    }
}
