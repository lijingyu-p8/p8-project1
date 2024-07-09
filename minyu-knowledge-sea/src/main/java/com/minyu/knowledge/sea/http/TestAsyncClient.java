package com.minyu.knowledge.sea.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestAsyncClient {
    public static void main(String[] args) throws Exception {
        t1();
    }

    /**
     * HTTP Client 同步简单使用示例
     */
    public static void t1() throws IOException, InterruptedException, ExecutionException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:8080/test/hello")).build();
        HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
        CompletableFuture<HttpResponse<String>> sendAsync = client.sendAsync(request, responseBodyHandler);
        HttpResponse<String> response = sendAsync.get();
        String body = response.body();
        System.out.println(body);
    }
}
