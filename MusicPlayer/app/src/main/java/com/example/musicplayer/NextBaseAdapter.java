package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Model.Song;

import java.util.List;

public class NextBaseAdapter extends BaseAdapter {

    private Context context; //context
    private List<Song> mData;

    public NextBaseAdapter(Context context, List<Song> objects) {
        this.context = context;
        this.mData = objects;
    }

    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_template, parent, false);
        }

        Song currentSong = mData.get(position);
        TextView title = convertView.findViewById(R.id.title);
        TextView artist = convertView.findViewById(R.id.artist);
        TextView duration = convertView.findViewById(R.id.duration);
        ImageView imageView = convertView.findViewById(R.id.label);
        title.setText(currentSong.getTitle());
        title.setSelected(true);
        artist.setText(currentSong.getArtist());
        duration.setText(Util.converter(currentSong.getDuration()));
        //if(Common.getAlbumArt(currentSong.getPath()) != null)
            //Glide.with(context).asBitmap().load(R.drawable.ic_record).into(imageView);
        //Glide.with(context).asBitmap().load(currentSong.getCover()).into(imageView);
        //byte[] art = Common.getAlbumArt(currentSong.getPath());
        //if(art !=null)
            //Glide.with(context).asBitmap().load(art).into(imageView);
        //else
            //Glide.with(context).asBitmap().load(R.drawable.ic_record).into(imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("ListView", "Clicked");
                ((MusicPlayerActivity)context).nextSongClicked(position);
            }
        });

        return convertView;
    }
}
