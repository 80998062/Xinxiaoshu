package com.xinshu.xinxiaoshu.injector.modules;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.core.Config;
import com.xinshu.xinxiaoshu.core.Parser;
import com.xinshu.xinxiaoshu.core.SnsReader;
import com.xinshu.xinxiaoshu.core.Task;
import com.xinshu.xinxiaoshu.injector.scopes.ActivityScope;

import java.io.File;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dalvik.system.DexClassLoader;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sinyuk on 2017/2/21.
 * <p>
 * 调用这个的时候应该保证对apk文件的操作都成功了
 */
@Module
public class DatabaseModule {


    @ActivityScope
    @Provides
    Parser parser(App app) {
        final File outputAPKFile = new File(Config.EXT_DIR, "/wechat.apk");
        Config.initWeChatVersion("6.3.13.64_r4488992");
        DexClassLoader cl = new DexClassLoader(
                outputAPKFile.getAbsolutePath(),
                app.getDir("outdex", 0).getAbsolutePath(),
                null,
                ClassLoader.getSystemClassLoader());


        try {
            Class SnsDetail = cl.loadClass(Config.PROTOCAL_SNS_DETAIL_CLASS);
            Class SnsObject = cl.loadClass(Config.PROTOCAL_SNS_OBJECT_CLASS);
            Class SnsDetailParser = cl.loadClass(Config.SNS_XML_GENERATOR_CLASS);
            return new Parser(SnsDetail, SnsDetailParser, SnsObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Parser();
    }

    @ActivityScope
    @Provides
    SnsReader snsReader(Task task,Parser parser) {
        return new SnsReader(task, parser);
    }


    @ActivityScope
    @Provides
    Single<SnsReader> snsReaderSingle(Lazy<SnsReader> snsReaderLazy) {
        return Single.fromCallable(snsReaderLazy::get)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
