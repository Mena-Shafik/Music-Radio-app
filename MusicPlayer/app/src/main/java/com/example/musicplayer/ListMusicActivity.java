package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.musicplayer.Fragments.MusicPlayerFragment;
import com.example.musicplayer.Model.Song;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;


public class ListMusicActivity extends AppCompatActivity {

    //private List<Song> songList;
    //SongBaseAdapter songsAdapter;
    //ListView listView;
    //SongBaseAdapter songsAdapter;
    //ArrayList<Song> songListFinal;

    private SwipeRefreshLayout swipeContainer;
    private List<Song> songList;
    TextView noSongs;
    ImageView noSongsImage;
    Fragment mfragment =  new MusicPlayerFragment();
    FragmentTransaction mfrag;
    SongAdapter songsAdapter;
    NavigationBarView bottomNavigationView;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        Log.d("MusicPlayerActivity", "Start");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        /*bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.songs);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.songs:
                        openFragment(ListFragment.newInstance());
                        return true;
                    case R.id.radio:
                        openFragment(RadioFragment.newInstance());
                        return true;
                }
                return false;
            }
        });*/


        /*noSongs = findViewById(R.id.noSongs);
        noSongsImage = findViewById(R.id.noSongsIV);

        swipeContainer = findViewById(R.id.swipeContainer);
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

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.orange_600));*/
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("");
        //setSupportActionBar(toolbar);




        //songListFinal = new ArrayList<Song>();

        //ArrayList<Song> songList=getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath());
        /*if(songList!=null){
            for(int i=0;i<songList.size();i++){
                String fileName=songList.get(i).getTitle();
                String filePath=songList.get(i).getPath();
                songListFinal.add(new Song(songList.get(i).getTitle(), songList.get(i).getPath()));
                //here you will get list of file name and file path that present in your device
                Log.d("data: ", "name: ="+fileName +" path: = "+filePath);
            }
        }*/

        runtimePermission();
        //songList= Common.getAllAudioFromDevice(this);

        /*try {
            createRecyclerView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        //createListView();

        //for(int i=0;i<songList.size();i++) {
            //Log.d("data: ", "Title: " + songList.get(i).getTitle() + " Artist: " + songList.get(i).getArtist() + " Duration: " + Common.converter(songList.get(i).getDuration()) + " Path: " + songList.get(i).getPath());
        //}

        /*if(songList.size() == 0) {
            /*for(int i=0;i<20;i++){
                songList.add(new Song(i,"Song"+i, "Artist"+i, "Path"+i, 229022.0));
            }
            noSongs.setVisibility(View.VISIBLE);
            noSongsImage.setVisibility(View.VISIBLE);

        }
        else{
            noSongs.setVisibility(View.INVISIBLE);
            noSongsImage.setVisibility(View.INVISIBLE);
        }*/




        //songAdapter = new SongAdapter(this, songList);
        //songAdapter.setClickListener(this);

    }

    // if need multiple permissions
    /*public void runtimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        songList= Common.getAllAudioFromDevice(getApplicationContext());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }*/

    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_MEDIA_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //songList= Common.getAllAudioFromDevice(getApplicationContext());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem myMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (songsAdapter != null) {
                    songsAdapter.getFilter().filter(query.toString());
                    Log.d("Query", query);
                }
                return false;
            }
        });
        return true;
    }

    /*public List<Song> getAllAudioFromDevice(final Context context) {
        final List<Song> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.DURATION,};
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

        if (c != null) {
            while (c.moveToNext()) {
                Song song = new Song();
                String name = c.getString(0);
                String artist = c.getString(1);
                String path = c.getString(2);
                Double duration = c.getDouble(3);

                song.setTitle(name);
                song.setDuration(duration);
                song.setArtist(artist);
                song.setPath(path);

                Log.e("Name :" + name, " Duration :" + duration);
                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioList.add(song);
            }
            c.close();
        }

        return tempAudioList;
    }*/

   /* private void createFragment(Bundle mBundle){
        mfrag = getSupportFragmentManager().beginTransaction();
        mfragment.setArguments(mBundle);
        mfrag.add(R.id.fragment_container, mfragment, null)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack(null)
                .commit();
    }*/

    /*private void openFragment(Fragment fragment) {
        Log.d("TAG", "openFragment: ");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        //transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); //if you add fragments it will be added to the backStack. If you replace the fragment it will add only the last fragment
        transaction.commit(); // commit() performs the action
    }*/
    /*private void createRecyclerView() throws IOException {
        Log.d("ListView: ", "createRecyclerView: Start!");
        RecyclerView rvSongs = findViewById(R.id.listViewSong);
        songList= Common.getAllAudioFromDevice(this);
        songsAdapter = new SongAdapter(songList);
        rvSongs.setAdapter(songsAdapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
        rvSongs.setHasFixedSize(true);
        rvSongs.setItemViewCacheSize(120);
        rvSongs.setDrawingCacheEnabled(true);
        rvSongs.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("ListView: ", "createRecyclerView: Complete!");
    }*/

    /*private void refreshList() throws IOException {
        Log.d("ListView: ", "refreshList: Start!");
        songsAdapter.clear();
        songsAdapter.addAll(Common.getAllAudioFromDevice(this));
        if(songList.size() == 0) {
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

        Log.d("ListView: ", "refreshList: Complete!");
    }*/
    /*private void createListView(){
        Log.d("ListView: ", "createListView: Start!");
        ListView listView = findViewById(R.id.listViewSong);
        songsAdapter = new SongBaseAdapter(this, songList);
        listView.setAdapter(songsAdapter);
        Log.d("ListView: ", "createListView: Complete!");
    }*/

                 /*if (savedInstanceState == null) {
                    if (mBundle !=null){
                        createFragment(mBundle);
                        listView.setVisibility(view.GONE);
                    }
                    else{

                    }
                }
    }*/



}