package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class News {
    public static void getNews(){
        String apiUrl = " https://newsdata.io/api/1/news?apikey=pub_282524b67596732de8f9d3ecfb01a95de8781&q=NYC&country=us ";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer "
            );

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject article = results.getJSONObject(i);
                    String title = article.getString("title");
                    String link = article.getString("link");
                    String description = article.getString("description");
                    String pubDate = article.getString("pubDate");
//                    JSONArray keywords = article.getJSONArray("keywords");

                    System.out.println("Title: " + title);
                    System.out.println("Link: " + link);
                    System.out.println("Description: " + description);
                    System.out.println("Publication Date: " + pubDate);
//                    System.out.println("Keywords: " + keywords.toString());
                    System.out.println();
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
