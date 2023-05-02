package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayer.Model.Station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private List<Station> mData;
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener mClickListener;

    // data is passed into the constructor
    public RadioAdapter(List<Station> data) {
        this.mData = data;
        setHasStableIds(true);
        //this.unfilteredList = data;
    }

    @NonNull
    @Override
    public RadioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View radioListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(radioListView);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioAdapter.ViewHolder holder, int position) {

        Station currentStation = mData.get(position);
        try {
            holder.bind(currentStation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(holder.itemView.getContext(), MusicPlayerActivity.class);
                Bundle mBundle = new Bundle();

                mBundle.putParcelableArrayList("stations",(ArrayList) mData);
                mBundle.putInt("position",holder.getAbsoluteAdapterPosition());
                mBundle.putString("mode","Radio");
                myIntent.putExtras(mBundle);

                holder.itemView.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title = itemView.findViewById(R.id.radioTitle);
        ConstraintLayout parentLayout = itemView.findViewById(R.id.parentLayout);
        ImageView imageView = itemView.findViewById(R.id.radioLabel);

        ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind (Station station) throws IOException {
            // Set item views based on your views and data model
            title.setText(station.getTitle());
            title.setSelected(true);

            switch(station.getTitle()){
                case "Z 103.5":
                    imageView.setImageResource(R.drawable.radio_z103_5);
                    break;
                case "Virgin 99.9":
                    imageView.setImageResource(R.drawable.radio_virgin_99_9);
                    break;
                case "KISS 92.5":
                    imageView.setImageResource(R.drawable.radio_kiss_92_5);
                    break;
                case "TODAY 93.5":
                    imageView.setImageResource(R.drawable.radio_today_93_5);
                    break;
                case "CHUM 104.5":
                    imageView.setImageResource(R.drawable.radio_chum_104_5);
                    break;
            }
            //imageView.setImageResource(R.drawable.radio_z103_5);


            //byte[] art = Util.getAlbumArt(station.getPath());
            /*if(art !=null)
                //Glide.with(itemView).load(art).into(imageView);
                Glide.with(itemView).load(art).apply(
                                RequestOptions.bitmapTransform(new RoundedCorners(10))
                                        .placeholder(R.drawable.ic_album).error(R.drawable.ic_album)).override(200,200)
                        .into(imageView);*/

            /*if(song.getCover() != null)
                Glide.with(itemView).asBitmap().load(song.getCover()).apply(
                        RequestOptions.bitmapTransform(new RoundedCorners(5))
                                .placeholder(R.drawable.ic_album).error(R.drawable.ic_album)).override(200,200).into(imageView);*/

        }
    }


}
