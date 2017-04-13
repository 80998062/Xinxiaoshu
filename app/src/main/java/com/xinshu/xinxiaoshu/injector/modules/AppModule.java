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
import com.xinshu.xinxiaoshu.rest.UploadService;
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
    /**
     * The constant TAG.
     */
    public static final String TAG = "AppModule";
    private static final long MAX_HTTP_CACHE = 1000 * 1000 * 100;
    private static final long TIMEOUT = 10;
    private final App app;

    /**
     * Instantiates a new App module.
     *
     * @param app the app
     */
    public AppModule(App app) {
        this.app = app;
    }

    /**
     * App app.
     *
     * @return the app
     */
    @Singleton
    @Provides
    App app() {
        return app;
    }


    /**
     * Task task.
     *
     * @param app the app
     * @return the task
     */
    @Singleton
    @Provides
    Task task(App app) {
        return new Task(app);
    }

    /**
     * Toast utils toast utils.
     *
     * @param app the app
     * @return the toast utils
     */
    @Singleton
    @Provides
    ToastUtils toastUtils(App app) {
        return new ToastUtils(app);
    }

    /**
     * Provide gson gson.
     *
     * @return the gson
     */
    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                // Blank fields are included as null instead of being omitted.
                .serializeNulls()
                .create();
    }

    /**
     * Provide shared preferences shared preferences.
     *
     * @return the shared preferences
     */
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

    /**
     * Provide cache path file.
     *
     * @return the file
     */
    @Provides
    @Singleton
    @Named(Names.HTTP_CACHE)
    File provideCachePath() {
        return new File(app.getExternalCacheDir(), Names.HTTP_CACHE);
    }

    /**
     * Provide access token preference.
     *
     * @param prefs the prefs
     * @return the preference
     */
    @Singleton
    @Provides
    @Named(Names.ACCESS_TOKEN)
    Preference<String> provideAccessToken(final RxSharedPreferences prefs) {
        return prefs.getString(Prefs.ACCESS_TOKEN);
    }

    /**
     * Provide endpoint endpoint.
     *
     * @return the endpoint
     */
    @Singleton
    @Provides
    Endpoint provideEndpoint() {
        return new Endpoint("http://10.0.1.84:8009/");
    }

    /**
     * Provide ok http client ok http client.
     *
     * @param httpCache   the http cache
     * @param interceptor the interceptor
     * @return the ok http client
     */
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

    /**
     * Provide retrofit retrofit.
     *
     * @param gson         the gson
     * @param okHttpClient the ok http client
     * @param endpoint     the endpoint
     * @return the retrofit
     */
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


    /**
     * Provide xinshu service xinshu service.
     *
     * @param retrofit the retrofit
     * @return the xinshu service
     */
    @Singleton
    @Provides
    XinshuService provideXinshuService(final Retrofit retrofit) {
        return retrofit.create(XinshuService.class);
    }

    /**
     * Provide upload service upload service.
     *
     * @param okHttpClient the ok http client
     * @return the upload service
     */
    @Singleton
    @Provides
    UploadService provideUploadService(final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://parse.xinshu.me/")
                .client(okHttpClient)
                .build()
                .create(UploadService.class);
    }

    /**
     * Provide scheduler transformer scheduler transformer.
     *
     * @return the scheduler transformer
     */
    @Singleton
    @Provides
    SchedulerTransformer provideSchedulerTransformer() {
        return new SchedulerTransformer(Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
