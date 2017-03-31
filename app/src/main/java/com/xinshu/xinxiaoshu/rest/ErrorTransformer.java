package com.xinshu.xinxiaoshu.rest;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.exceptions.Exceptions;
import retrofit2.Response;

/**
 * Created by sinyuk on 2017/3/28.
 */

public class ErrorTransformer<T extends Response<R>, R extends BaseResponse> implements ObservableTransformer<T, R> {

    private static final String DEFAULT_ERROR_MESSAGE = "Opps!";
    private static final int DEFAULT_ERROR_CODE = -1;

    @Override
    public ObservableSource<R> apply(Observable<T> upstream) {

        return upstream.map(t -> {
            int code = 0;
            String msg = null;
            if (t.isSuccessful()) {
                if (t.body() != null) {
                    if (t.body().code == 0) {
                        return t.body();
                    } else {
                        code = t.body().code;
                        msg = t.body().msg;
                    }
                } else {
                    msg = DEFAULT_ERROR_MESSAGE;
                    code = DEFAULT_ERROR_CODE;
                }
            } else {
                // 错误
                code = t.code();
                switch (t.code()) {
                    case 400: {
                        msg = "请求错误";
                        break;
                    }

                    case 401: {
                        msg = "请登录或者重新登录";
                        break;
                    }

                    case 403: {
                        msg = "操作被服务器拒绝";
                        break;
                    }

                    case 404: {
                        msg = "你好像来错了地方";
                        break;
                    }

                    case 405: {
                        msg = "操作被禁止";
                        break;
                    }

                    case 406: {
                        msg = "无法使用请求的内容特性响应请求的网页";
                        break;
                    }

                    case 407: {
                        msg = "需要代理授权";
                        break;
                    }

                    case 408: {
                        msg = "请求超时";
                        break;
                    }
                    case 409: {
                        msg = "服务器在完成请求时发生冲突";
                        break;

                    }
                    case 410: {
                        msg = "请求的资源已永久删除";
                        break;
                    }
                    case 411: {
                        msg = "请求不含有效内容长度标头字段";
                        break;

                    }
                    case 412: {
                        msg = "服务器不能满足你的要求";
                        break;
                    }
                    case 413: {
                        msg = "我们太穷了,买不起更好的服务器";
                        break;

                    }
                    case 414: {
                        msg = "请求的 URI（通常为网址）过长";
                        break;
                    }
                    case 415: {
                        msg = "页面无法提供请求的范围";
                        break;
                    }
                    case 416: {
                        msg = "服务器不能满足你的要求";
                        break;
                    }
                    case 501:
                    case 502:
                    case 503:
                    case 504:
                    case 505: {
                        msg = "服务器大姨妈了";
                        break;
                    }
                    default: {
                        msg = DEFAULT_ERROR_MESSAGE;
                    }
                }
            }

            if (msg != null || code != 0) {
                try {
                    throw new BaseException(code, msg);
                } catch (BaseException e) {
                    throw Exceptions.propagate(e);
                }
            }
            return t.body();
        });
    }
}
