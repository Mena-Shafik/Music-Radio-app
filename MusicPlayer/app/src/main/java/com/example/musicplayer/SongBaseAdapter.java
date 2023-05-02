package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongBaseAdapter extends BaseAdapter implements Filterable {

    private Context context; //context
    private List<Song> mData;
    private List<Song> unfilteredList;

    public SongBaseAdapter(Context context, List<Song> objects) {
        this.context = context;
        this.mData = objects;
        this.unfilteredList = objects;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return mData.indexOf(getItem(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = View.inflate(convertView, R.layout.list_template, null);

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
        //title.setSelected(true);
        artist.setText(currentSong.getArtist());
        duration.setText(Util.converter(currentSong.getDuration()));
        //if(currentSong.getCover() != null)
         //   Glide.with(context).asBitmap().load(currentSong.getCover()).into(imageView);
       // else
         //   Glide.with(context).asBitmap().load(R.drawable.ic_record).into(imageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, MusicPlayerActivity.class);
                Bundle mBundle = new Bundle();

                mBundle.putParcelableArrayList("tracks",(ArrayList) unfilteredList);
                mBundle.putInt("position",mData.get(position).getId());
                myIntent.putExtras(mBundle);

                context.startActivity(myIntent);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                // If there's nothing to filter on, return the original data for
                // your list
                if (charSequence != null || charSequence.length() > 0) {
                    charSequence = charSequence.toString().toLowerCase();
                    ArrayList<Song> filter = new ArrayList<Song>();

                    for (int i = 0; i< unfilteredList.size(); i++)
                    {
                        if(unfilteredList.get(i).getTitle().toString().toLowerCase().contains(charSequence))
                        {
                            filter.add(unfilteredList.get(i));
                        }
                    }
                    results.count = filter.size();
                    results.values = filter;
                }else{
                    results.count = unfilteredList.size();
                    results.values = unfilteredList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // set the data to the filter results and notifyDataSetChanged()
                mData = (List<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Song> list){
        mData.addAll(list);
        notifyDataSetChanged();
    }

}
