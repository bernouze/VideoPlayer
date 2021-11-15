package com.test.videoplayer.Activities;

import android.os.Bundle;

import com.test.videoplayer.Fragments.VideoListFragment;
import com.test.videoplayer.Fragments.VideoListRecyclerViewAdapter;
import com.test.videoplayer.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create videos list
        ArrayList<VideoListRecyclerViewAdapter.VideoItem> videos = new ArrayList<>();
        videos.add(new VideoListRecyclerViewAdapter.VideoItem("C dans l'air",
                "https://test.voxiweb.com/live2/francetv-replay/master.m3u8?media_id=f747ba50-ee3c-434a-ae26-70bf0aad3772"));
        videos.add(new VideoListRecyclerViewAdapter.VideoItem("A vous de voir",
                "https://test.voxiweb.com/live2/francetv-replay/master.m3u8?media_id=fd8cab45-673a-4afa-a566-49fe274a9b3f"));
        videos.add(new VideoListRecyclerViewAdapter.VideoItem("Lapins crétins",
                "https://test.voxiweb.com/live2/francetv-replay/master.m3u8?media_id=501646c4-8cd5-4e59-b7b2-b034623dd509"));

        loadFragment(VideoListFragment.newInstance(videos));
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
