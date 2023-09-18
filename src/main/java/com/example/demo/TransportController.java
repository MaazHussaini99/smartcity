package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;
import javafx.stage.Stage;

/***
 * Handles the button stuff for the transport tab
 */
public class TransportController {
    Transport transport;

    @FXML
    VBox theBox;

    static ArrayList<Bus> buses = new ArrayList<>();
    HashMap<String,Station> stations = new HashMap<String ,Station>();
    Connection connection;

//    public TransportController(VBox theBox){
//        connection = DBConn.connectDB();
//        this.theBox = theBox;
//        try {
//            getBuses();
//            getStops();
//            //giveBusStops();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        for(int i =0;i<buses.size();i++){
//            TitledPane tp = new TitledPane();
//            Bus bus = buses.get(i);
//            tp.setText(bus.shortName+ " "+bus.longName);
//            tp.setExpanded(false);
//            tp.setContent(new Button());
//            theBox.getChildren().add(tp);
//        }
//    }

    /***
     * generate a list of buses
     * @throws SQLException
     */
    public static ArrayList<Bus> getBuses() throws SQLException {
        String busQ = "SELECT route_id, route_name, route_busNumber, route_desc FROM transport_route";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(busQ)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String routeName = resultSet.getString("route_name");
                String busNum = resultSet.getString("route_busNumber");
                String busDesc = resultSet.getString("route_desc");
                ArrayList<Stop> test = getStops(resultSet.getString("route_id"));
                buses.add(new Bus(busNum,routeName,busDesc, test));
            }
            return buses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * generate a hashmap of stations
     * @throws SQLException
     */
    public static ArrayList<Stop> getStops(String routeId) throws SQLException {
        ArrayList<Stop> stops = new ArrayList<>();

        // Use SQL joins with distinct aliases for tables
        String query = "SELECT ts.stop_name, tt.arrival_time, tt.departure_time " +
                "FROM transport_trip ttrip " +
                "JOIN transport_time tt ON ttrip.trip_id = tt.trip_id " +
                "JOIN transport_stop ts ON tt.stop_id = ts.stop_id " +
                "WHERE ttrip.route_id = ?";

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SimpleStringProperty stopName = new SimpleStringProperty(resultSet.getString("stop_name"));
                SimpleStringProperty arrivalTime = new SimpleStringProperty(resultSet.getString("arrival_time"));
                SimpleStringProperty departureTime = new SimpleStringProperty(resultSet.getString("departure_time"));
                System.out.println(routeId + ": " + stopName);
                stops.add(new Stop(stopName, arrivalTime, departureTime));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stops;
    }





    /***
     * generate a list of stops associated with each bus
     * todo something about route id not being 1-1 with the trip ids, need to change something
     * @throws SQLException
     */
//    public void giveBusStops() throws SQLException {
//        String routeQ = "SELECT route_id, trip_id FROM transport_trip";
//        String stopQ = "SELECT trip_id, arrival_time, stop_id,stop_sequence FROM transport_time";
//        HashMap<String, String> routeTripTranslation = new HashMap<>();
//        class temp{
//            String arrival,stopID,stopSequence;
//
//            public temp(String arrival,String stopID, String stopSequence){
//                this.arrival = arrival;
//                this.stopID = stopID;
//                this.stopSequence = stopSequence;
//            }
//        }
//        //take a trip id, and find the bus with the same route id
//        try(PreparedStatement ps = connection.prepareStatement(routeQ)){
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                String routeID = rs.getString("route_id");
//                String tripID = rs.getString("trip_id");
//                if(!routeTripTranslation.containsKey(routeID)){
//                    routeTripTranslation.put(routeID,tripID);
//                    System.out.println(routeID + " "+ tripID);}
//
//            }
//        }
//
//        HashMap<String,ArrayList<temp>> map = new HashMap<>();
//
//        try(PreparedStatement ps = connection.prepareStatement(stopQ)){
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                //when I cycle through the bus list, I will get a arraylist of temps to generate things
//                String tripID = rs.getString("trip_id");
//                String arrival = rs.getString("arrival_time");
//                String stopID = rs.getString("stop_id");
//                String stopSeq = rs.getString("stop_sequence");
//                if(!map.containsKey(tripID)){
//                    ArrayList<temp> temps = new ArrayList<temp>();
//                    map.put(tripID,temps);
//                    temps.add(new temp(arrival,stopID,stopSeq));
//                }
//                else{
//                    map.get(tripID).add(new temp(arrival,stopID,stopSeq));
//                }
//                ArrayList<temp> newTemp = map.get(tripID);
//                System.out.println(tripID);
//                for(temp tempo:newTemp){
//                    System.out.print(tempo.stopID+ " ");
//                }
//            }
//            ArrayList<temp> temptemp = map.get("10494434-JUN23-Albany-Weekday-03");
//            System.out.println(temptemp == null);
//            // now cycle through the buslist, and add the stops
//            for(int i =0;i<buses.size();i++){
//                Bus bus = buses.get(i); //I have a bus, and I have its route id
//                String routeID = bus.routeID; //now I want its trip id;
//                String tripID = routeTripTranslation.get(routeID);
//                System.out.println(tripID);
//                ArrayList<temp> stops = map.get(tripID);
//
//                System.out.println(stops==null);
//                if(stops!=null){
//                    System.out.println("The stops listed for "+bus.longName+": ");
//                    for(temp info: stops){
//                        System.out.println(info.stopID);
//                    }
//                }
//            }
//        }
//
//    }
}