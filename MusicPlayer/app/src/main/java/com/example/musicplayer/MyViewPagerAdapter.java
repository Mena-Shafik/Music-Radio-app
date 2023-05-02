package com.example.musicplayer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.Fragments.ListFragment;
import com.example.musicplayer.Fragments.RadioFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d("TABS", "Music Tab");
                return new ListFragment();
            case 1:
                Log.d("TABS", "Radio Tab");
                return new RadioFragment();
            default:
                Log.d("TABS", "Default Tab");
                return new ListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
