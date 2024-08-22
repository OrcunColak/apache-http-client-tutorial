package com.colak.post;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientPostExample {

    public static void main(String[] args) {
        // Create a CloseableHttpClient instance

        CookieStore cookieStore = new BasicCookieStore();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore)
                .build()) {

            HttpPost request = createHttpPostRequest(Collections.emptyMap());

            httpClient.execute(request,
                    response -> {

                        log.info("Response code : {}", response.getCode());

                        String contentMimeType = ContentType.parse(response.getEntity().getContentType()).getMimeType();
                        log.info("ContentMimeType : {}", contentMimeType);

                        String bodyAsString = EntityUtils.toString(response.getEntity());
                        log.info("bodyAsString Length : {}", bodyAsString.length());
                        return response;
                    }
            );

        } catch (Exception exception) {
            log.error("Exception", exception);
        }
    }

    private static HttpPost createHttpPostRequest(Map<String, String> requestParams) {
        // Create a HttpPost request
        HttpPost request = new HttpPost("https://www.google.com");

        List<NameValuePair> params = new ArrayList<>(requestParams.size());

        for (Map.Entry<String, String> reqParamEntry : requestParams.entrySet()) {
            BasicNameValuePair nameValuePair = new BasicNameValuePair(reqParamEntry.getKey(), reqParamEntry.getValue());
            params.add(nameValuePair);
        }
        request.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        return request;
    }
}

