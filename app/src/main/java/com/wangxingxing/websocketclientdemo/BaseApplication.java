package com.wangxingxing.websocketclientdemo;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wangxingxing.websocketclientdemo.websocketclientdemo.BuildConfig;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)   // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .tag("wxx")             // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .logStrategy(new CustomLogcatStrategy())
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}
