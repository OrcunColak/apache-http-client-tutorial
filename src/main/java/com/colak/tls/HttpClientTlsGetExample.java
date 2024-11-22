package com.colak.tls;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HttpClientTlsGetExample {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // Create an SSLContext that trusts all certificates
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(new TrustAllStrategy())
                .build();

        // Create a DefaultClientTlsStrategy with the custom SSLContext
        // Disable hostname verification
        DefaultClientTlsStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext, null, null);

        // Configure a connection manager with the TLS strategy
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setTlsSocketStrategy(tlsStrategy)
                .build();

        // Build the HttpClient with the custom SSLContext and hostname verifier
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
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

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}
