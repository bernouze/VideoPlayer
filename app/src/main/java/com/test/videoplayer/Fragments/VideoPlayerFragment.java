package com.test.videoplayer.Fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Util;
import com.test.videoplayer.R;
import com.test.videoplayer.Views.VideoControlsView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class VideoPlayerFragment extends Fragment {

    private static final String ARG_VIDEO = "com.test.videoplayer.video";
    private static final String KEY_POSITION = "com.test.videoplayer.position";

    private ConstraintLayout mainVideoLayout;
    private PlayerView playerView;
    private Dialog fullScreenDialog;
    private MediaItem mediaItem;
    private ExoPlayer player;
    private VideoControlsView videoControlsView;
    private ConstraintLayout videoSubLayout;
    private long startPosition;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    public static VideoPlayerFragment newInstance(VideoListRecyclerViewAdapter.VideoItem video) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIDEO, video);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_player, container, false);

        mainVideoLayout = view.findViewById(R.id.video_main_layout);
        videoSubLayout = view.findViewById(R.id.video_sub_layout);

        playerView = view.findViewById(R.id.player_view);

        // hide subtitle view
        SubtitleView subtitleView = playerView.getSubtitleView();
        if (subtitleView != null) {
            subtitleView.setVisibility(View.GONE);
        }

        videoControlsView = view.findViewById(R.id.controller_view);

        initFullscreenDialog();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            openFullscreenDialog();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initPlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            startPosition = player.getCurrentPosition();
            releasePlayer();
            videoControlsView.close();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            startPosition = player.getCurrentPosition();
            releasePlayer();
            videoControlsView.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        videoControlsView.close();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_POSITION, startPosition);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            openFullscreenDialog();
        } else {
            closeFullscreenDialog();
        }
    }

    private void initPlayer() {
        if (player == null && getContext() != null) {
            mediaItem = createMediaItem();

            player = new ExoPlayer.Builder(getContext()).build();

            videoControlsView.setPlayer(player);
            playerView.setPlayer(player);
        }

        player.setMediaItem(mediaItem);
        player.seekTo(startPosition);
        player.setPlayWhenReady(true);

        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private MediaItem createMediaItem() {
        MediaItem mediaItem = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            VideoListRecyclerViewAdapter.VideoItem video = arguments.getParcelable(ARG_VIDEO);
            videoControlsView.setTitle(video.title);
            mediaItem = MediaItem.fromUri(video.uri);
        }
        return mediaItem;
    }

    // FULLSCREEN HANDLE
    private void initFullscreenDialog() {
        fullScreenDialog = new Dialog(getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                closeFullscreenDialog();
            }
        };
    }

    private void openFullscreenDialog() {
        if (!fullScreenDialog.isShowing()) {
            ((ViewGroup) videoSubLayout.getParent()).removeView(videoSubLayout);
            fullScreenDialog.addContentView(videoSubLayout,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            fullScreenDialog.show();
            videoControlsView.setIsFullScreen(true);
            int uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            videoSubLayout.setSystemUiVisibility(uiOption);

        }
    }

    private void closeFullscreenDialog() {
        if (fullScreenDialog.isShowing()) {
            ((ViewGroup) videoSubLayout.getParent()).removeView(videoSubLayout);
            mainVideoLayout.addView(videoSubLayout);
            fullScreenDialog.dismiss();
            videoControlsView.setIsFullScreen(false);

            videoSubLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}