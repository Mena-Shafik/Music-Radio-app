package com.example.musicplayer.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Station implements Parcelable {


    private String title;
    private String path;
    private byte[] cover;

    public Station() {

    }
    public Station(String title, String path) {
        this.title = title;
        this.path = path;
    }

    protected Station(Parcel in) {
        //id = in.readInt();
        title = in.readString();
        //artist = in.readString();
        path = in.readString();
        //duration = in.readDouble();
    }

    public Station(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeInt(id);
        parcel.writeString(title);
        //parcel.writeString(artist);
        parcel.writeString(path);
        //parcel.writeDouble(duration);
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
