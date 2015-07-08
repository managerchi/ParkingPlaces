package com.example.android.parkingplaces;

import java.util.ArrayList;

/**
 * Created by Chi on 2015/7/8.
 */
public class Stations1 {

    //private static Stations instance;

    public Integer numberOfStations = 0;
    public String company = "";
    public ArrayList<Station> StationAL = new ArrayList<Station>();

    protected Stations1() {

    }

    //public static Stations getInstance(){
    //    if(instance == null){
    //        instance = new Stations();
    //    }
    //    return instance;
    //}

    public ArrayList<Station> StationAL(){
        return StationAL;
    }
}

