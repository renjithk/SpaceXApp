package com.spacex.service;

import com.spacex.entity.LaunchInfoEntity;
import com.spacex.entity.RocketEntity;

import java.util.List;

/**
 * Provides service definitions
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public interface ISpaceXService {
    /**
     * Fetches all available rockets and save the data to database for offline use
     * @return list of rockets of type {@link RocketEntity}
     */
    List<RocketEntity> fetchAllRockets();

    /**
     * Returns all available rockets saved on the database
     * @return list of rockets of type {@link RocketEntity}
     */
    List<RocketEntity> fetchAllSavedRockets();

    /**
     * Finds all launches of a rocket
     * @param rocketId valid id of the rocket whose details are needed
     * @return detail rocket information of type {@link LaunchInfoEntity}
     */
    LaunchInfoEntity findLaunchInfo(String rocketId);

    /**
     * Finds all launches of a rocket which is saved on the devices
     * @param rocketId rocketId valid id of the rocket whose details are needed
     * @return detail rocket information of type {@link LaunchInfoEntity}
     */
    LaunchInfoEntity fetchSavedLaunchInfo(String rocketId);
}
