package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {

    private List<Song> mData;
    private List<Song> unfilteredList;
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener mClickListener;


    // data is passed into the constructor
    public SongAdapter(List<Song> data) {
        this.mData = data;
        setHasStableIds(true);
        this.unfilteredList = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the custom layout
        View songListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_template, parent, false);

        // Return a new holder instance
        return new ViewHolder(songListView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        Song currentSong = mData.get(position);
        //currentSong.setCover(Common.getAlbumArt(currentSong.getPath()));
        try {
            holder.bind(currentSong);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //else
            //Glide.with(mContext).asBitmap().load(R.drawable.ic_record).into(holder.imageView);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(holder.itemView.getContext(), MusicPlayerActivity.class);
                Bundle mBundle = new Bundle();

                mBundle.putParcelableArrayList("tracks",(ArrayList) unfilteredList);
                mBundle.putInt("position",mData.get(holder.getAbsoluteAdapterPosition()).getId());
                mBundle.putString("mode","Music");
                myIntent.putExtras(mBundle);

                holder.itemView.getContext().startActivity(myIntent);
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position){

        return mData.get(position).getId();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title = itemView.findViewById(R.id.title);
        TextView artist = itemView.findViewById(R.id.artist);
        TextView duration = itemView.findViewById(R.id.duration);
        ConstraintLayout parentLayout = itemView.findViewById(R.id.parentLayout);
        ImageView imageView = itemView.findViewById(R.id.label);

        ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind (Song song) throws IOException {
            // Set item views based on your views and data model
            title.setText(song.getTitle());
            title.setSelected(true);
            artist.setText(song.getArtist());
            duration.setText(Util.converter(song.getDuration()));
            byte[] art = Util.getAlbumArt(song.getPath());
            if(art !=null)
                //Glide.with(itemView).load(art).into(imageView);
                Glide.with(itemView).load(art).apply(
                        RequestOptions.bitmapTransform(new RoundedCorners(10))
                                .placeholder(R.drawable.ic_album).error(R.drawable.ic_album)).override(200,200)
                        .into(imageView);

            /*if(song.getCover() != null)
                Glide.with(itemView).asBitmap().load(song.getCover()).apply(
                        RequestOptions.bitmapTransform(new RoundedCorners(5))
                                .placeholder(R.drawable.ic_album).error(R.drawable.ic_album)).override(200,200).into(imageView);*/

        }
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


