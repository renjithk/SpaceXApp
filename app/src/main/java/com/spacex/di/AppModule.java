package com.spacex.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacex.R;
import com.spacex.SpaceXApp;
import com.spacex.localstorage.SpaceXDatabase;
import com.spacex.mapper.ISpaceXMapper;
import com.spacex.mapper.SpaceXMapper;
import com.spacex.mapper.api.ISpaceXAPI;
import com.spacex.service.ISpaceXService;
import com.spacex.service.SpaceXService;
import com.spacex.utils.SSLContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Indicates binding for components defined in the DaggerComponent
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Module
public class AppModule {
    private final SpaceXApp appContext;

    public AppModule(SpaceXApp appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        //create gson object from the builder
        return builder.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient, Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(context.getString(R.string.api_base_url))
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        try {
            okHttpBuilder.sslSocketFactory(SSLContext.instance(appContext));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //retrofit network logs, it is turned on by default
        //in reality, this can be restricted only for dev builds
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpBuilder.addInterceptor(interceptor);

        //set timeout to 30 seconds
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.retryOnConnectionFailure(true);
        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    ISpaceXService provideSpaceXService(ISpaceXMapper mapper, SpaceXDatabase database) {
        return new SpaceXService(mapper, database);
    }

    @Provides
    @Singleton
    ISpaceXMapper provideSpaceXMapper(ISpaceXAPI spaceXAPI) {
        return new SpaceXMapper(spaceXAPI);
    }

    @Provides
    @Singleton
    ISpaceXAPI provideAPI(Retrofit retrofit) {
        return retrofit.create(ISpaceXAPI.class);
    }

    @Provides
    @Singleton
    SpaceXDatabase provideSpaceXDB() {
        return Room.databaseBuilder(appContext, SpaceXDatabase.class,
                appContext.getString(R.string.db_name)).fallbackToDestructiveMigration().build();
    }
}
