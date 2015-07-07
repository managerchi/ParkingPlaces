package com.example.android.parkingplaces;

import java.util.ArrayList;

/**
 * Created by Chi on 2015/7/7.
 */

public class Stations {

    //private static Stations instance;

    public Integer numberOfStations = 0;
    public String company = "";
    public ArrayList<Station> StationAL = new ArrayList<Station>();

    protected Stations() {

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

