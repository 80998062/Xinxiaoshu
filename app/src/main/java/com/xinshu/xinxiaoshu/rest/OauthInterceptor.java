package com.xinshu.xinxiaoshu.rest;

import com.f2prateek.rx.preferences2.Preference;
import com.xinshu.xinxiaoshu.injector.Names;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sinyuk on 2017/3/28.
 */

public class OauthInterceptor implements Interceptor {

    private static final String TAG = "OauthInterceptor";

    @Named(Names.ACCESS_TOKEN)
    private final Preference<String> tokenKey;

    @Inject
    public OauthInterceptor(@Named(Names.ACCESS_TOKEN) final Preference<String> token) {
        this.tokenKey = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (tokenKey.isSet()) {
            builder.header("Authorization", tokenKey.get());
        }
        return chain.proceed(builder.build());
    }
}
