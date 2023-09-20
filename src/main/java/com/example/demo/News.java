package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

public class News {
    private String title, description, url, img_url;

    private static List<News> newsList = new ArrayList<>();

    public News(String title, String description, String url, String img_url){
        this.title = title;
        this.description = description;
        this.url = url;
        this.img_url = img_url;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public String getUrl(){
        return this.url;
    }
    public String getImg_url(){
        return this.img_url;
    }

    public static List<News> getNews() {

        String apiUrl = "https://newsdata.io/api/1/news?apikey=pub_282524b67596732de8f9d3ecfb01a95de8781&q=NYC&country=us";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer ");

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
                    String description = "";
                    if (article.get("description") == null){
                        description = "Description Unavailable: Please visit source link.";
                    }else{
                        description = article.get("description").toString();
                    }
                    String newsUrl = article.getString("link");
                    String newsImg = "null"; // Default value if "image_url" is not present

                    // Check if the "image_url" key exists in the JSON object
                    if (article.has("image_url") && !article.isNull("image_url")) {
                        newsImg = article.getString("image_url");
                    }
                    newsList.add(new News(title, description, newsUrl, newsImg));
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}