package com.test.videoplayer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.videoplayer.Activities.MainActivity;
import com.test.videoplayer.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListFragment extends Fragment implements VideoListRecyclerViewAdapter.VideoItemClickListener {

    private static final String ARG_VIDEOS = "com.test.videoplayer.videos";
    private ArrayList<VideoListRecyclerViewAdapter.VideoItem> videos;

    public VideoListFragment() {
    }

    public static VideoListFragment newInstance(ArrayList<VideoListRecyclerViewAdapter.VideoItem> videos) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_VIDEOS, videos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            videos = getArguments().getParcelableArrayList(ARG_VIDEOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            VideoListRecyclerViewAdapter adapter = new VideoListRecyclerViewAdapter(context, videos);
            adapter.setVideoItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onVideoItemClick(VideoListRecyclerViewAdapter.VideoItem item) {

        ((MainActivity) getActivity()).loadFragment(VideoPlayerFragment.newInstance(item), "videoPlayer");
    }
}