package com.xinshu.xinxiaoshu;

import android.app.Application;
import android.content.Context;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.xinshu.xinxiaoshu.injector.components.AppComponent;
import com.xinshu.xinxiaoshu.injector.components.DaggerAppComponent;
import com.xinshu.xinxiaoshu.injector.components.DatabaseComponent;
import com.xinshu.xinxiaoshu.injector.modules.AppModule;
import com.xinshu.xinxiaoshu.injector.modules.DatabaseModule;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sinyuk on 2017/2/17.
 */

public class App extends Application {

    private AppComponent appComponent = null;
    private DatabaseComponent databaseComponent = null;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();


        //Use it only in debug builds
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }

    }

    public Observable<AppComponent> appComponentOB() {
        return Observable.fromCallable(this::build)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private AppComponent build() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
        return appComponent;
    }

    private DatabaseComponent plus() {
        if (databaseComponent == null)
            databaseComponent = getAppComponent().plus(new DatabaseModule());
        return databaseComponent;
    }

    public AppComponent getAppComponent() {
        if (appComponent == null)
            return build();
        return appComponent;
    }

    public Observable<DatabaseComponent> databaseComponentOB() {
        return Observable.fromCallable(this::plus)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
