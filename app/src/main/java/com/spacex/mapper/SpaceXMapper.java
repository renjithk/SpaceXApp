package com.spacex.mapper;

import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;
import com.spacex.mapper.api.ISpaceXAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Provides mapper implementation as defined in {@link ISpaceXMapper}
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class SpaceXMapper implements ISpaceXMapper {
    private final ISpaceXAPI spaceXAPI;

    public SpaceXMapper(ISpaceXAPI spaceXAPI) {
        this.spaceXAPI = spaceXAPI;
    }

    @Override
    public List<RocketEntity> fetchAllRockets() throws Exception {
        Call<List<RocketEntity>> call = spaceXAPI.fetchAllRockets();
        Response<List<RocketEntity>> response = call.execute();
        return response.body();
    }

    @Override
    public List<RocketInfoEntity> findLaunchInfo(String rocketId) throws Exception {
        Call<List<RocketInfoEntity>> call = spaceXAPI.findLaunchInfo(rocketId);
        Response<List<RocketInfoEntity>> response = call.execute();
        return response.body();
    }
}
