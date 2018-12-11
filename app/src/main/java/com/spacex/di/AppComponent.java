package com.spacex.di;

import com.google.gson.Gson;
import com.spacex.localstorage.SpaceXDatabase;
import com.spacex.service.ISpaceXService;
import com.spacex.ui.RocketDetails;
import com.spacex.ui.RocketList;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Dagger dependency component definitions
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(RocketList rocketList);
    void inject(RocketDetails rocketDetails);

    Gson gson();
    OkHttpClient okHttpClient();
    Retrofit retrofit();
    ISpaceXService service();
    SpaceXDatabase spaceXDB();
}
