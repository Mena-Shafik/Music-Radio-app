package com.example.musicplayer.Fragments;

import static com.example.musicplayer.Util.*;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.Model.Song;
import com.example.musicplayer.SongAdapter;

import java.io.IOException;
import java.util.List;


public class ListFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private List<Song> songList;

    private RecyclerView rvSongs;

    TextView noSongs;
    ImageView noSongsImage;
    SongAdapter songsAdapter;
    public ListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list,container,false);

        noSongs = view.findViewById(R.id.noSongs);
        noSongsImage = view.findViewById(R.id.noSongsIV);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.orange_600));

        rvSongs = view.findViewById(R.id.listViewSong);
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
        songList= getAllAudioFromDevice(getActivity());
        songsAdapter = new SongAdapter(songList);
        rvSongs.setAdapter(songsAdapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSongs.setHasFixedSize(true);
        rvSongs.setItemViewCacheSize(120);
        rvSongs.setDrawingCacheEnabled(true);
        rvSongs.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("RecyclerView: ", "createRecyclerView for Songs: Complete!");
    }

    private void refreshList() throws IOException {
        Log.d("ListFragment: ", "refreshList: Start!");
        songsAdapter.clear();
        songsAdapter.addAll(getAllAudioFromDevice(getActivity()));
        if(songList.size() == 0) {
            /*for(int i=0;i<20;i++){
                songList.add(new Song(i,"Song"+i, "Artist"+i, "Path"+i, 229022.0));
            }*/
            noSongs.setVisibility(View.VISIBLE);
            noSongsImage.setVisibility(View.VISIBLE);

        }
        else{
            noSongs.setVisibility(View.INVISIBLE);
            noSongsImage.setVisibility(View.INVISIBLE);
        }

        Log.d("ListFragment: ", "refreshList: Complete!");
    }
}