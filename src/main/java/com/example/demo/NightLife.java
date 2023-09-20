package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NightLife {
    private String name;
    private String description;
    private String imageUrl;
    private String moreInfoUrl;

    public NightLife(String name, String description, String imageUrl, String moreInfoUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.moreInfoUrl = moreInfoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }
    public static List<NightLife> getNightlifeInfo() {
        List<NightLife> nightlifeList = new ArrayList<>();
        String sql = "SELECT * FROM nightlife";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             ResultSet resultSet = preparedStatement.executeQuery(sql);
                while (resultSet.next()) {
                    NightLife nightlife = new NightLife(
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("imageUrl"),
                            resultSet.getString("moreInfoUrl")
                    );
                    nightlifeList.add(nightlife);
                }
            }
        catch (Exception e) {
                e.printStackTrace();
            }
            return nightlifeList;
    }

}
