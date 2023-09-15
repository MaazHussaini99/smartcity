package com.example.demo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * Transport class handles the current location of bus and stops.
 * TODO review scope of variables
 */
public class Transport {
    ArrayList<Bus> buses;
    ArrayList<Station> stations;
    ArrayList<Stop> stops;

    /***
     * for testing purpose mostly,
     * @param buses
     * @param stations
     * @param stops
     */
    public Transport(ArrayList<Bus> buses, ArrayList<Station> stations, ArrayList<Stop> stops){
        this.buses = buses;
        this.stations = stations;
        this.stops = stops;
    }

    /***
     * need to make database calls and instantiate those arraylist
     */
    public Transport(){

    }

}

class Bus{
    String shortName;
    String longName;

    String routeID;
    ArrayList<Station> stations;

    /***
     * dont expect to use this, make a bus with number, and long name
     * @param shortName
     * @param longName
     */
    public Bus(String shortName,String longName,String routeID){
        this.shortName = shortName;
        this.longName = longName;
        this.routeID = routeID;
        this.stations = new ArrayList<Station>();
    }

    /***
     * todo reverse paramter?
     * todo something
     *
     * @param station
     * @param index
     * @return if add is successful
     */
    public boolean addStation(Station station,int index) {
        stations.add(index, station);
        return true;
    }
    public boolean addStation(Station station) {
        stations.add(station);
        return true;
    }

    /***
     * todo implement the thing, reach low prior
     * get the next station the bus will arive at
     * @return the station the bus will arrive at
     */
    public Station getNextStop(){
        return stations.get(0);
    }

}

/***
 * also consider bus stops, this refers to a physical location
 * where the bus stops, and store a list of stops and the name of the stop
 */
class Station{
    String name;
    String id;
    ArrayList<Stop> stops;

    public Station(String name, ArrayList<Stop> stop){
        this.name = name;
        this.stops = stop;
    }

    public Station(String name,String id){
        this.name = name;
        this.id = id;
        stops = new ArrayList<Stop>();
    }

    /***
     * add a new stop to the location
     * TODO reconsider return parameter, also do addStop
     * @return  probs change to void
     */
    public boolean addStop(Stop stop){
        stops.add(stop);
        return true;
    }

    /***
     * todo actually do it
     * given a particular bus, get its next n arrival time
     * @param bus the particular bus name and stuff
     * @param n the number of stops you want to see
     * @return the time when the bus when arrive
     */
    public ArrayList<Stop> getNextStop(Bus bus, int n){
        ArrayList<Stop> temp = new ArrayList<Stop>();

        return temp;
    }

}

/***
 * A specific point in time where a bus stop at a station.
 */
class Stop{
    Bus bus;
    Station station;
    String time;

    public Stop(Bus bus, Station station, String time){
        this.bus = bus;
        this.station = station;
        this.time = time;
    }



}