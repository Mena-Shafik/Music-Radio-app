package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {



    public static List<Song> getAllAudioFromDevice(final Context context) throws IOException {

        final List<Song> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //String path = "/storage/emulated/0/Music/";
        //String fakePath = "https://dummyimage.com/%dx%d/000/fff.png";
        String filePath = Environment.getExternalStorageDirectory() + "/Music/*";
        // what data to grab
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.DURATION};
        // check if it is a song
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        Cursor c = context.getContentResolver().query(uri, projection, where, null,  "title");
        int count =0;
        Random rand = new Random();

        if (c != null) {
            while (c.moveToNext()) {
                //randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
                String path = c.getString(0);
                String title = c.getString(1);
                String artist = c.getString(2);
                Double duration = c.getDouble(3);
                int value1 = 100 + rand.nextInt((2048 - 100) + 1);
                int value2 = 100 + rand.nextInt((2048 - 100) + 1);
                //fakePath = String.format(fakePath, value1, value2);
                //Song song =  new Song(count, title, artist, path, duration);
                //if(getAlbumArt(c.getString(0)) != null) {
                Song song = new Song(count, title, artist, path, duration, getAlbumArt(c.getString(0)));
                //Song song = new Song(count, title, artist, path, duration);
                tempAudioList.add(song);
                count++;
                Log.i("data: ", "Album id: " + song.getId() + " Title: " + song.getTitle() + " Artist: " + song.getArtist() + " Path: " + song.getPath() + " Duration: " + song.getDuration());
                //}
            }
            c.close();
        }
        return tempAudioList;
    }

    public static byte[] getAlbumArt(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }


    public static String converter(double time){
        String  elapsedTime;
        int  minutes = (int)(time / 1000 / 60);
        int  seconds = (int)(time / 1000 % 60);

        elapsedTime = minutes + ":";
        if (seconds < 10)
            elapsedTime += "0";
        elapsedTime+=seconds;

        return elapsedTime;
    }

    public static String converter(float time){
        String  elapsedTime;
        int  minutes = (int)(time / 1000 / 60);
        int  seconds = (int)(time / 1000 % 60);

        elapsedTime = minutes + ":";
        if (seconds < 10)
            elapsedTime += "0";
        elapsedTime+=seconds;

        return elapsedTime;
    }

    public static Bitmap getThumbnail(Context context, Uri uri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(context, uri);
        rawArt = mmr.getEmbeddedPicture();

        // if rawArt is null then no cover art is embedded in the file or is not
        // recognized as such.
        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

        else
            art = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_record);
        return art;
        // Code that uses the cover art retrieved below.
    }




}

//OUTDATED CODE



    /*private ArrayList<Song> getPlayList(String rootPath) {
        ArrayList<Song> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    Song song;
                    song = new Song(file.getName(), file.getAbsolutePath());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

        private static boolean checkArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        if(retriever.getEmbeddedPicture() != null)
            return true;
        else
            return false;
    }


    */
