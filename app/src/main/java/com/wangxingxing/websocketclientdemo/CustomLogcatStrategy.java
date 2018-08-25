package com.wangxingxing.websocketclientdemo;

import android.util.Log;

import com.orhanobut.logger.LogStrategy;

public class CustomLogcatStrategy implements LogStrategy {

    private int last;

    @Override public void log(int priority, String tag, String message) {
        Log.println(priority, randomKey() + tag, message);
    }

    private String randomKey() {
        int random = (int) (10 * Math.random());
        if (random == last) {
            random = (random + 1) % 10;
        }
        last = random;
        return String.valueOf(random);
    }
}
