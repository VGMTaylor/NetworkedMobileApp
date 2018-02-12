package com.example.android.basicgesturedetect;

import android.app.Application;
import android.content.Context;

/**
 * Created by 462904 on 14/03/2016.
 */
// This class allows us to get an application context
public class ApplicationContentProvidor extends Application {
    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

}
