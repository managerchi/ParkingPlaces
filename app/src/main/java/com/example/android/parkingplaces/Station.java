package com.example.android.parkingplaces;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chi on 2015/7/7.
 */
public class Station implements Parcelable {
    public String name;
    public String address;
    public Double latidude;
    public Double longitude;

    //private int id;
    //private String content;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Station() {

    }

    public Station(Parcel in) {
        readFromParcel(in);
    }

    public Station(String name1, String address1, Double latitude1, Double longitude1){
        this.name = name1;
        this.address = address1;
        this.latidude = latitude1;
        this.longitude = longitude1;
    }

    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(id);
        //dest.writeString(content);

        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latidude);
        dest.writeDouble(longitude);


    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {

        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    private void readFromParcel(Parcel in) {
        //this.id = in.readInt();
        //this.content = in.readString();

        this.name = in.readString();
        this.address = in.readString();
        this.latidude = in.readDouble();
        this.longitude = in.readDouble();
    }

}


