package com.abc.itbbs.common.ai.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.common.ai.listener.SseEventSourceListener;
import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpUtils extends HttpUtil {

    public static HttpResponse doPost(String requestUrl, Map<String, String> headers, Object body, Integer timeout) {
        return HttpRequest.post(requestUrl)
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(body))
                .timeout(timeout)
                .execute();
    }

    public static void doPostStream(String requestUrl, Map<String, String> headers, Object body, Integer timeout,
                                    StreamResponseListener listener) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeout != null ? timeout : 30, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(timeout != null ? timeout : 30, TimeUnit.SECONDS)
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(requestUrl)
                .post(RequestBody.create(JSONUtil.toJsonStr(body), MediaType.get("application/json")));

        // 添加请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        if (!headers.containsKey("Accept")) {
            // 确保包含SSE必需的Accept头
            requestBuilder.addHeader("Accept", "text/event-stream, application/x-ndjson");
        }

        Request request = requestBuilder.build();
        EventSource.Factory factory = EventSources.createFactory(client);

        SseEventSourceListener sseEventSourceListener = new SseEventSourceListener(listener);
        factory.newEventSource(request, sseEventSourceListener);

        if (Objects.nonNull(listener)) {
            listener.onStart();
        }

    }



}