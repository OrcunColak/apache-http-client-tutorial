package com.colak.get;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;

@Slf4j
public class HttpClientGetExample2 {

    public static void main(String[] args) {
        // Create a CloseableHttpClient instance

        CookieStore cookieStore = new BasicCookieStore();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore)
                .build()) {
            // Create a HttpGet request
            HttpGet request = new HttpGet("https://www.google.com");
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
}

