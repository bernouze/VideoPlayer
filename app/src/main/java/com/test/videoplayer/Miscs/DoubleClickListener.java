package com.test.videoplayer.Miscs;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

public abstract class DoubleClickListener implements View.OnClickListener {
    private static final long CLICK_SPAN = 250;
    private boolean isSingleClick;
    private long timestampLastClick;
    private final Handler handler;
    private final Runnable runnable;

    public DoubleClickListener() {
        timestampLastClick = 0;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isSingleClick) {
                    onSingleClick();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - timestampLastClick < CLICK_SPAN) {
            isSingleClick = false;
            handler.removeCallbacks(runnable);
            onDoubleClick();
            return;
        }
        isSingleClick = true;
        handler.postDelayed(runnable, CLICK_SPAN);
        timestampLastClick = SystemClock.elapsedRealtime();
    }

    public abstract void onSingleClick();
    public abstract void onDoubleClick();
}
