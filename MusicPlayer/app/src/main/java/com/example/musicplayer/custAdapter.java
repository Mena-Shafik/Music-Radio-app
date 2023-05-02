package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicplayer.Model.Song;

import java.util.ArrayList;

public class custAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song>songList;
    public custAdapter(Context context, ArrayList<Song> songArrayList){
        this.context = context;
        this.songList = songArrayList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        // inflate the layout for each list row
        if (currentView == null) {
            currentView = LayoutInflater.from(context).inflate(R.layout.list_template, parent, false);
        }

        // get current item to be displayed
        Song currentItem = (Song) getItem(position);

        // get the TextView for item name and item description
        TextView title = (TextView) currentView.findViewById(R.id.title);
        TextView artist = (TextView) currentView.findViewById(R.id.artist);
        TextView duration = (TextView) currentView.findViewById(R.id.duration);

        //sets the text for item name and item description from the current item object
        title.setText(currentItem.getTitle());
        artist.setText(currentItem.getArtist());
        duration.setText(currentItem.getDuration().toString());
        // returns the view for the current row
        return currentView;
    }
}
