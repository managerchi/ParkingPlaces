package com.example.android.parkingplaces;

import java.util.ArrayList;

/**
 * Created by Chi on 2015/7/5.
 */
public class ClassicSingleton {
    private static ClassicSingleton instance = null;

    public Integer stations = 0;
    public String company = "";
    public ArrayList<String> name = new ArrayList<String>();
    public ArrayList<String> address = new ArrayList<String>();
    public ArrayList<Double> latitude = new ArrayList<Double>();
    public ArrayList<Double> longitude = new ArrayList<Double>();

    protected ClassicSingleton() {
        // Exists only to defeat instantiation.
    }

    public static ClassicSingleton getInstance() {
        if (instance == null) {
            instance = new ClassicSingleton();
        }
        return instance;
    }

    public ArrayList<String> Name() {
        return name;
    }

    public ArrayList<String> Address() {
        return address;
    }

    public ArrayList<Double> Latitude() {
        return latitude;
    }

    public ArrayList<Double> Longitude() {
        return longitude;
    }

    //ClassicSingleton CS= new ClassicSingleton();
    //CS.getInstance();
    //String myName=CS.getname(); // Output will be >> Chintan Khetiya
    //String like=CS.getNameformarray().get(1); // Output will be >> Android

}
