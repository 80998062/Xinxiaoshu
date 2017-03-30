package com.xinshu.xinxiaoshu.injector.modules;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sinyuk.myutils.system.ToastUtils;
import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.config.Prefs;
import com.xinshu.xinxiaoshu.core.Task;
import com.xinshu.xinxiaoshu.injector.Names;
import com.xinshu.xinxiaoshu.rest.Endpoint;
import com.xinshu.xinxiaoshu.rest.OauthInterceptor;
import com.xinshu.xinxiaoshu.rest.SchedulerTransformer;
import com.xinshu.xinxiaoshu.rest.XinshuService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sinyuk on 2017/2/21.
 */
@Module
public class AppModule {
    public static final String TAG = "AppModule";
    private static final long MAX_HTTP_CACHE = 1000 * 1000 * 100;
    private static final long TIMEOUT = 10;
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    App app() {
        return app;
    }


    @Singleton
    @Provides
    Task task(App app) {
        return new Task(app);
    }

    @Singleton
    @Provides
    ToastUtils toastUtils(App app) {
        return new ToastUtils(app);
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                // Blank fields are included as null instead of being omitted.
                .serializeNulls()
                .create();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return app.getSharedPreferences("global", MODE_PRIVATE);
    }

    /**
     * Provide global prefs rx shared preferences.
     *
     * @param prefs the prefs
     * @return the rx shared preferences
     */
    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(final SharedPreferences prefs) {
        return RxSharedPreferences.create(prefs);
    }

    @Provides
    @Singleton
    @Named(Names.HTTP_CACHE)
    File provideCachePath() {
        return new File(app.getExternalCacheDir(), Names.HTTP_CACHE);
    }

    @Singleton
    @Provides
    @Named(Names.ACCESS_TOKEN)
    Preference<String> provideAccessToken(final RxSharedPreferences prefs) {
        return prefs.getString(Prefs.ACCESS_TOKEN);
    }

    @Singleton
    @Provides
    Endpoint provideEndpoint() {
        return new Endpoint("http://10.0.1.84:8009/");
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(
            final @Named(Names.HTTP_CACHE) File httpCache,
            final OauthInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(interceptor);

        if (httpCache != null) {
            Cache cache = new Cache(httpCache, MAX_HTTP_CACHE);
            builder.cache(cache);
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);


        //设置超时
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(
            final Gson gson,
            final OkHttpClient okHttpClient,
            final Endpoint endpoint) {
        return new Retrofit.Builder()
                .baseUrl(endpoint.getEndpoint())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Singleton
    @Provides
    XinshuService provideXinshuService(final Retrofit retrofit) {
        return retrofit.create(XinshuService.class);
    }

    @Singleton
    @Provides
    SchedulerTransformer provideSchedulerTransformer() {
        return new SchedulerTransformer(Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
