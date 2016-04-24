package com.infrastructure.net;

import android.os.Handler;

import com.infrastructure.cache.CacheManager;
import com.infrastructure.utils.BaseUtils;
import com.infrastructure.utils.LogWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by duke on 2016/4/24.
 */
public class OkHttpRequestTest implements Runnable{

    // 区分get还是post的枚举
    public static final String REQUEST_GET = "get";
    public static final String REQUEST_POST = "post";

    Request request;
    private OkHttpClient client;

    private URLData urlData = null;
    private RequestCallback requestCallback = null;
    private List<RequestParameter> parameter = null;
    private String url = null; // 原始url
    private String newUrl = null; // 拼接key-value后的url

    private Handler handler;

    public OkHttpRequestTest (final URLData data, final List<RequestParameter> params,
                                   final RequestCallback callBack){
        this.urlData = data;
        url = urlData.getUrl();
        parameter = params;

        requestCallback = callBack;

        handler = new Handler();

        client = new OkHttpClient();

    }

    @Override
    public void run() {
        try {
            if (urlData.getNetType().equals(REQUEST_GET)) {
                // 添加参数
                final StringBuffer paramBuffer = new StringBuffer();
                if ((parameter != null) && (parameter.size() > 0)) {

                    for (final RequestParameter p : parameter) {
                        if (paramBuffer.length() == 0) {
                            paramBuffer.append(p.getName() + "="
                                    + BaseUtils.UrlEncodeUnicode(p.getValue()));
                        } else {
                            paramBuffer.append("&" + p.getName() + "="
                                    + BaseUtils.UrlEncodeUnicode(p.getValue()));
                        }
                    }

                    newUrl = url + "?" + paramBuffer.toString();
                } else {
                    newUrl = url;
                }

                request = new Request.Builder()
                        .url(newUrl)
                        .build();

                LogWrapper.logD("url:" + newUrl);
                // 如果这个get的API有缓存时间（大于0）
                if (urlData.getExpires() > 0 && !urlData.getIsRefresh()) {
                    final String content = CacheManager.getInstance()
                            .getFileCache(newUrl);
                    if (content != null) {
                        final InputStream in;
                        in = new ByteArrayInputStream(content.getBytes("utf-8"));
                        requestCallback.onSuccess(in);
                        return;
                    }
                }

            } else if (urlData.getNetType().equals(REQUEST_POST)){
                if ((parameter != null) && (parameter.size() > 0)) {
                    FormBody.Builder builder = new FormBody.Builder();
                    for (RequestParameter p : parameter){
                        builder.add(p.getName(), p.getValue());
                    }
                    RequestBody body = builder.build();
                    request = new Request.Builder().url(url).post(body).build();
                } else {
                    handleNetworkError("Http POST param is NULL");
                    return;
                }
            } else {
                return;
            }

            LogWrapper.logD("duke okHttp");
            Response response = client.newCall(request).execute();
            String data = response.body().string();

            if (urlData.getNetType().equals(REQUEST_GET)
                    && urlData.getExpires() > 0) {
                LogWrapper.logD("expiresTime:" + urlData.getExpires());
                CacheManager.getInstance().putFileCache(newUrl, data, urlData.getExpires());
            }

            final InputStream inn =  new ByteArrayInputStream(data.getBytes("utf-8"));
            requestCallback.onSuccess(inn);

//            LogWrapper.logD("response:" + response.body().string());
        } catch (IOException e) {
            handleNetworkError("网络异常");
            e.printStackTrace();
        }

    }

    public void handleNetworkError(final String errorMsg) {
        if (requestCallback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    requestCallback.onFail(errorMsg);
                }
            });
        }
    }
}
