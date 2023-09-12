package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Weather {

    private double tempFarhenhiet, feelsLikeFarhenhiet, windMph, precipIn;
    private int humidity, visibilityMiles, uvIndex;
    private String conditionIcon, conditionText;

    public Weather(double tempFarhenhiet, double feelsLikeFarhenhiet, double windMph, int humidity, int visibilityMiles,
                   int uvIndex, double precipIn, String conditionIcon, String conditionText){
        this.tempFarhenhiet = tempFarhenhiet;
        this.feelsLikeFarhenhiet = feelsLikeFarhenhiet;
        this.windMph = windMph;
        this.precipIn = precipIn;
        this.humidity = humidity;
        this.visibilityMiles = visibilityMiles;
        this.uvIndex = uvIndex;
        this.conditionIcon = conditionIcon;
        this.conditionText =conditionText;
    }

    public double getTempFarhenhiet() {
        return tempFarhenhiet;
    }

    public double getFeelsLikeFarhenhiet() {
        return feelsLikeFarhenhiet;
    }

    public double getWindMph() {
        return windMph;
    }

    public double getPrecipIn() {
        return precipIn;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getVisibilityMiles() {
        return visibilityMiles;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }

    public String getConditionText() {
        return conditionText;
    }

    public static Weather getWeather() {
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=d8a18be87c82447eb77182645232508&q=Albany,NY";

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
                JSONObject current = jsonResponse.getJSONObject("current");
                JSONObject condition = current.getJSONObject("condition");

                // Now you can access fields within the "current" object
                double tempFarhenhiet = current.getDouble("temp_f");
                double feelsLikeFarhenhiet = current.getDouble("feelslike_f");
                double windMph = current.getDouble("wind_mph");
                int humidity = current.getInt("humidity");
                int visibilityMiles = current.getInt("vis_miles");
                int uvIndex = current.getInt("uv");
                double precipIn = current.getDouble("precip_in");
                String conditionIcon = condition.getString("icon");
                String conditionText = condition.getString("text");

                return new Weather(tempFarhenhiet, feelsLikeFarhenhiet, windMph, humidity,
                        visibilityMiles, uvIndex, precipIn, conditionIcon, conditionText);

            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}