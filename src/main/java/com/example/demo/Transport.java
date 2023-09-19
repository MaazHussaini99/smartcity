package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;

class Bus{
    StringProperty shortName;
    StringProperty longName;
    StringProperty routeID;
    String routeMainId;

    public StringProperty getShortName() {
        return shortName;
    }

    public StringProperty getLongName() {
        return longName;
    }

    public StringProperty getRouteID() {
        return routeID;
    }

    public String getRouteMainId() {
        return routeMainId;
    }

    /***
     * dont expect to use this, make a bus with number, and long name
     * @param shortName
     * @param longName
     */
    public Bus(String shortName,String longName,String routeID, String routeMainId){
        this.shortName = new SimpleStringProperty(shortName);
        this.longName = new SimpleStringProperty(longName);
        this.routeID = new SimpleStringProperty(routeID) ;
        this.routeMainId = routeMainId;
    }
}

/***
 * A specific point in time where a bus stop at a station.
 */
class Stop{
    SimpleStringProperty stopName;
    SimpleStringProperty arrivalTime;
    SimpleStringProperty departureTime;

    public SimpleStringProperty stopNameProperty() {
        return stopName;
    }

    public SimpleStringProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    public SimpleStringProperty departureTimeProperty() {
        return departureTime;
    }

    public Stop(SimpleStringProperty stopName, SimpleStringProperty arrivalTime, SimpleStringProperty departureTime){
        this.stopName = stopName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }
}