package com.example.android.parkingplaces;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Chi on 2015/7/8.
 */
public class Stations implements Parcelable {
    public Integer numberOfStations = 0;
    public String company = "";
    private ArrayList<Station> stations = new ArrayList<Station>();

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    private ArrayList<Station> list = new ArrayList<Station>();
    public ArrayList<Station> StationAL()
    {
        for(int i=0;i<stations.size();i++)
        {
            list.add(stations.get(i));
        }
        return list;
    }

    //private int id;
    //private List<Review> reviews
    //private List<String> authors;

    public Stations () {
        stations = new ArrayList<Station>();

        //reviews = new ArrayList<Review>();
        //authors = new ArrayList<String>();
    }

    public Stations (Parcel in) {
        this();
        readFromParcel(in);
    }

   /* getters and setters excluded from code here */

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numberOfStations);
        dest.writeString(company);
        dest.writeList(stations);

        //dest.writeInt(id);
        //dest.writeList(reviews);
        //dest.writeStringList(authors);
    }

    public static final Parcelable.Creator<Stations> CREATOR = new Parcelable.Creator<Stations>() {

        public Stations createFromParcel(Parcel source) {
            return new Stations(source);
        }

        public Stations[] newArray(int size) {
            return new Stations[size];
        }

    };

    /*
     * Constructor calls read to create object
     */
    private void readFromParcel(Parcel in) {
        this.numberOfStations = in.readInt();
        this.company = in.readString();
        in.readTypedList(stations, Station.CREATOR); /* NULLPOINTER HERE */

        //this.id = in.readInt();
        //in.readTypedList(reviews, Review.CREATOR); /* NULLPOINTER HERE */
        //in.readStringList(authors);
    }
}
