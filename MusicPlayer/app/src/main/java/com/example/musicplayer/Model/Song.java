package com.example.musicplayer.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String title;
    private String artist;
    private String path;
    private byte[] cover;
    private Double duration;
    private int id;



    private String fakePath;


    public Song() {

    }
   /* public Song(String title, String artist, String path, Double duration,  byte[] cover) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.cover = cover;
    }*/

    public Song(int id, String title, String artist, String path, Double duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }
    public Song(int id, String title, String artist, String path, Double duration, byte[] cover) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.cover = cover;
    }


    protected Song(Parcel in) {
        id = in.readInt();
        title = in.readString();
        artist = in.readString();
        path = in.readString();
        duration = in.readDouble();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFakePath() {
        return fakePath;
    }

    public void setFakePath(String fakePath) {
        this.fakePath = fakePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(path);
        dest.writeDouble(duration);
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
