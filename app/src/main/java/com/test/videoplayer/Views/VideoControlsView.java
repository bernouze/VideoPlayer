package com.test.videoplayer.Views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.metadata.Metadata;
import com.test.videoplayer.Miscs.DoubleClickListener;
import com.test.videoplayer.Miscs.Utils;
import com.test.videoplayer.R;


import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class VideoControlsView extends LinearLayout {

    private final long SMALL_BACKWARD_TIME = 10000; // 10 secs
    private final long BIG_BACKWARD_TIME = 60000; // 60 secs
    private final long SMALL_FORWARD_TIME = 30000; // 30 secs
    private final long BIG_FORWARD_TIME = 5 * 60000; // 5 mins

    private final TextView timeText;
    private final TextView titleText;
    private final ImageButton playButton;
    private final ProgressBar videoLoading;
    private final ImageButton backwardButton;
    private final ImageButton forwardButton;
    private final ConstraintLayout ControlsLayout;

    private Runnable timeUpdater;
    private Handler timeHandler;

    private Player player;
    private boolean isFullScreen;

    private final long ELLAPSED_TIME_ACTION = 5000; // 5 secs

    private final Runnable actionUpdater;
    private final Handler actionHandler;

    public VideoControlsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_video_controls, this);

        timeText = findViewById(R.id.time_text);
        titleText = findViewById(R.id.title_text);
        backwardButton = findViewById(R.id.backward_button);
        playButton = findViewById(R.id.play_button);
        forwardButton = findViewById(R.id.forward_button);
        videoLoading = findViewById(R.id.video_loading);
        ControlsLayout = findViewById(R.id.controls_main_layout);

        actionHandler = new Handler();
        actionUpdater = new Runnable() {
            @Override
            public void run() {
                if (isFullScreen) {
                    hideControls();
                }
            }
        };


        hideControls();
        initActions();
    }

    public void close() {
        hideControls();
        showLoading();
        if (timeHandler != null) {
            timeHandler.removeCallbacks(timeUpdater);
        }
        timeHandler = null;

        if (actionHandler != null) {
            actionHandler.removeCallbacks(actionUpdater);
        }
    }

    private void updateTime() {
        if (timeHandler == null) {
            timeHandler = new Handler();
            timeUpdater = new Runnable() {
                @Override
                public void run() {
                    Resources resources = getContext().getResources();
                    String timeString = resources.getString(R.string.video_controller_time,
                            Utils.formatToDigitalClock(player.getCurrentPosition()),
                            Utils.formatToDigitalClock(player.getDuration()));
                    timeText.setText(timeString);
                    timeHandler.postDelayed(timeUpdater, 1000);
                }
            };
            timeHandler.post(timeUpdater);
        }
    }

    private void updateAction() {
        if (isFullScreen) {
            showControls();
            actionHandler.removeCallbacks(actionUpdater);
            actionHandler.postDelayed(actionUpdater, ELLAPSED_TIME_ACTION);
        }
    }

    private void hideControls() {
        timeText.setVisibility(View.GONE);
        titleText.setVisibility(View.GONE);
        backwardButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        forwardButton.setVisibility(View.GONE);
    }

    private void showControls() {
        timeText.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        backwardButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        forwardButton.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        videoLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        videoLoading.setVisibility(View.VISIBLE);
    }

    private void initActions() {

        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    if (player.getDuration() - player.getCurrentPosition() < 10) {
                        player.seekTo(0);
                        player.play();
                    }

                    player.play();
                }
                updateAction();
            }
        });

        ControlsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAction();
            }
        });

        forwardButton.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick() {
                player.seekTo(player.getCurrentPosition() + SMALL_FORWARD_TIME);
                updateAction();
            }

            @Override
            public void onDoubleClick() {
                player.seekTo(player.getCurrentPosition() + BIG_FORWARD_TIME);
                updateAction();
            }
        });

        backwardButton.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick() {
                player.seekTo(player.getCurrentPosition() - SMALL_BACKWARD_TIME);
                updateAction();
            }

            @Override
            public void onDoubleClick() {
                player.seekTo(player.getCurrentPosition() - BIG_BACKWARD_TIME);
                updateAction();
            }
        });
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.player.addListener(new PlayerEventListener());

    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setIsFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;

        if (isFullScreen) {
            hideControls();
        } else {
            showControls();
        }
    }

    private class PlayerEventListener implements Player.Listener {

        @Override
        public void onRenderedFirstFrame() {
            updateTime();
            if (!isFullScreen) {
                showControls();
            }
            hideLoading();
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            if (isPlaying) {
                playButton.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                if ( player.getDuration() - player.getCurrentPosition() < 10 ) {
                    playButton.setImageResource(android.R.drawable.ic_popup_sync);
                }else {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        }

        @Override
        public void onMetadata(Metadata metadata) {
            Log.d("test", "onMetadata");
        }

        @Override
        public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
            Log.d("test", "onMediaMetadataChanged");
        }

    }
}
