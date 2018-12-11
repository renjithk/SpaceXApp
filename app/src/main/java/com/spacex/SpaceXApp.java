package com.spacex;

import android.app.Application;
import android.content.Context;

import com.spacex.di.AppComponent;
import com.spacex.di.AppModule;
import com.spacex.di.DaggerAppComponent;

/**
 * Application class for SpaceX application
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class SpaceXApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static SpaceXApp get(Context context) {
        return (SpaceXApp) context.getApplicationContext();
    }

    public AppComponent getComponent() {
        if(null == appComponent) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }
}
