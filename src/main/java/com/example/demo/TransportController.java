package com.example.demo;

import java.util.ArrayList;

/***
 * Handles the button stuff for the transport tab
 */
public class TransportController {
    Transport transport;

    public TransportController(){
        Bus bus10 = new Bus("10", "Street Something to Street Anywhere");
        Bus bus200 = new Bus("200", "Street thing to exact");
        Bus bus300 = new Bus("300", "THe stations for 300");
        Station stationFor10 = new Station("Station10");
        Station stationFor20 = new Station("StationFor20");
        Station stationFor30 = new Station("StationFor30");
        Station intersection = new Station("The intersection");
        Stop stop10 = new Stop(bus10,stationFor10, "900");
        Stop stop20 = new Stop(bus200,stationFor20, "900");
        Stop stop10x1 = new Stop(bus10,intersection,"930");
        Stop stop20x1 = new Stop(bus200, stationFor20, "1000");
        bus10.addStation(stationFor10);
        bus10.addStation(intersection);
        bus200.addStation(stationFor20);
        bus200.addStation(intersection);

        stationFor10.addStop(stop10);
        stationFor20.addStop(stop20);

        intersection.addStop(stop10x1);
        intersection.addStop(stop20x1);

        ArrayList<Bus> buses = new ArrayList<Bus>();
        ArrayList<Station> stations = new ArrayList<Station>();
        ArrayList<Stop> stops = new ArrayList<>();

        buses.add(bus10);
        buses.add(bus200);
        buses.add(bus300);

        stations.add(stationFor10);
        stations.add(stationFor30);
        stations.add(stationFor20);

        stops.add(stop10);
        stops.add(stop10x1);
        stops.add(stop20);
        stops.add(stop20x1);

        transport = new Transport(buses,stations,stops);
    }


}
