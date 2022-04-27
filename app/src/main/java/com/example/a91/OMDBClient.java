package com.example.a91;


import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


//https://www.youtube.com/watch?v=OWID-D5W9cU used as source and applied

public class OMDBClient {

    public static final String SEARCH_URL = "https://www.omdbapi.com/?t=TITLE&apikey=APIKEY";
    public static final String SEARCH_URL_ID = "https://www.omdbapi.com/?i=ID&apikey=APIKEY";

    public static String sendGetRequest(String requestURL) {
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);

            String line;
            while((line=buffer.readLine())!=null){
                response.append(line);
            }
            buffer.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String searchMovieByTitle(String title, String key) throws JSONException {
        try{
            title = URLEncoder.encode(title,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestUrl = SEARCH_URL
                .replaceAll("TITLE",title)
                .replaceAll("APIKEY",key);
        return sendGetRequest(requestUrl);
    }

    public String searchMovieById(String id, String key) throws JSONException {
        try{
            id = URLEncoder.encode(id,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestUrl = SEARCH_URL_ID
                .replaceAll("ID",id)
                .replaceAll("APIKEY",key);
        return sendGetRequest(requestUrl);
    }
}
