package com.example.musicplayer.Fragments;


import static com.example.musicplayer.Util.*;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Model.Station;
import com.example.musicplayer.R;
import com.example.musicplayer.RadioAdapter;
import com.example.musicplayer.SongAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadioFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private List<Station> radioList;

    private RecyclerView recyclerView;

    TextView noSongs;
    ImageView noSongsImage;

    RadioAdapter radioAdapter;
    public RadioFragment() {
    }

    public static RadioFragment newInstance() {
        RadioFragment fragment = new RadioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);

        //noSongs = view.findViewById(R.id.noSongs);
        //noSongsImage = view.findViewById(R.id.noSongsIV);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*try {
                    refreshList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                swipeContainer.setRefreshing(false);*/
            }
        });

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.orange_600));

        recyclerView = view.findViewById(R.id.listViewRadio);
        radioList = new ArrayList<Station>();
        Station aST0 = new Station("Z 103.5", "https://evanov.leanstream.co/CIDCFM?args=web_01");
        Station aST1 = new Station("Virgin 99.9", "https://15313.live.streamtheworld.com/CKFMFMAAC_SC");
        Station aST2 = new Station("KISS 92.5", "https://rogers-hls.leanstream.co/rogers/tor925.stream/48k/playlist.m3u8?listeningSessionID=634c3f150acc1d2d_16967025_HQcmJ7Kr_OTUuMjE3LjExMC4yNDA6ODAwMA!!_0000002P16l&downloadSessionID=0");
        //Station aST3 = new Station("TODAY 93.5", "");
        Station aST4 = new Station("CHUM 104.5", "https://20603.live.streamtheworld.com/CHUMFMAAC_SC");
        radioList.add(aST0);
        radioList.add(aST1);
        radioList.add(aST2);
        //radioList.add(aST3);
        radioList.add(aST4);


        try {
            createRecyclerView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Inflate the layout for this fragment
        return view;
    }

    public void createRecyclerView() throws IOException {

        Log.d("RecyclerView: ", "createRecyclerView: Start!");
        radioAdapter = new RadioAdapter(radioList);
        recyclerView.setAdapter(radioAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(120);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("RecyclerView: ", "createRecyclerView for Songs: Complete!");
    }


    /*private void refreshList() throws IOException {
        Log.d("ListView: ", "refreshList: Start!");
        radioAdapter.clear();
        //radioAdapter.addAll(getAllAudioFromDevice(getActivity()));
        if(radioList.size() == 0) {
            /*for(int i=0;i<20;i++){
                songList.add(new Song(i,"Song"+i, "Artist"+i, "Path"+i, 229022.0));
            }
            noSongs.setVisibility(View.VISIBLE);
            noSongsImage.setVisibility(View.VISIBLE);

        }
        else{
            noSongs.setVisibility(View.INVISIBLE);
            noSongsImage.setVisibility(View.INVISIBLE);
        }

        Log.d("Radio Fragment: ", "refreshList: Complete!");
    }*/


}