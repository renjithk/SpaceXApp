package com.spacex.mapper.api;


import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API interface used by Retrofit
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public interface ISpaceXAPI {
    @GET("/v3/rockets")
    Call<List<RocketEntity>> fetchAllRockets();

    @GET("/v3/launches")
    Call<List<RocketInfoEntity>> findLaunchInfo(@Query("rocket_id") String rocketId);
}
