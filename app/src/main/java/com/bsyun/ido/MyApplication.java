package com.bsyun.ido;

import android.app.Application;
import android.content.Context;

import com.bsyun.ido.utils.CrashHandler;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler.getInstance().init(this);
    }

    public static Context getContext() {
        return context;
    }
}
