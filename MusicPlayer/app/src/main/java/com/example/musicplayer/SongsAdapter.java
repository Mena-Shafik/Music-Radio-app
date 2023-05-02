package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.musicplayer.Model.Song;

import java.util.List;

public class SongsAdapter extends BaseAdapter implements Filterable {

    private Context context; //context
    private List<Song> mData;

    public SongsAdapter(Context context, int resource, List<Song> objects){
        this.context = context;
        this.mData = objects;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = View.inflate(convertView, R.layout.list_template, null);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_template, parent, false);
        }

        Song currentSong = (Song) getItem(position);
        TextView title = convertView.findViewById(R.id.title);
        TextView artist = convertView.findViewById(R.id.artist);
        TextView duration = convertView.findViewById(R.id.duration);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData = (List<Song>) results.values;
                // Adapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // List<Song> filteredResults = getFilteredResults(constraint);
                FilterResults results = new FilterResults();
                //results.values = filteredResults;
                return results;
            }

        };
    }
}
