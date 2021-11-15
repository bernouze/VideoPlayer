package com.test.videoplayer.Miscs;

import android.app.Activity;

public class Utils {

    static public String formatToDigitalClock(long ms) {
        long seconds = (ms / 1000) % 60;
        long minutes = (ms / (1000 * 60)) % 60;
        long hours = (ms / (1000 * 60 * 60));

        return (hours == 0 ? "" : hours < 10 ? "0" + hours + ":" : hours + ":") +
                (minutes == 0 ? "00:" : minutes < 10 ? "0" + minutes + ":" : minutes + ":") +
                (seconds == 0 ? "00" : seconds < 10 ? "0" + seconds : String.valueOf(seconds));
    }

    static public void changeToTheme(Activity activity, int theme) {
        activity.setTheme(theme);
        activity.recreate();
    }
}
