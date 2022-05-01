package com.example.a91;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;



//https://java.tutorialink.com/fast-and-asynchronous-way-of-making-multiple-http-requests-in-java/
// Used as template and applied

public class HTTPworker implements Callable<String> {

    private final LinkedBlockingQueue queue;

    public HTTPworker(LinkedBlockingQueue queue) {
        this.queue = queue;
    }

    private String sendGetRequest(String url) throws IOException {

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    @Override
    public String call() throws InterruptedException, IOException {
        while (true) {
                String data = (String) queue.take();
                String response = sendGetRequest(data);
                return response;
        }
    }

}



