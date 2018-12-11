package com.spacex.mapper;

import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;

import java.util.List;

/**
 * Mapper definitions which can be accessed by the service
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public interface ISpaceXMapper {
    /**
     * Performs network operations to fetch all available rockets
     * @return list of rockets of type {@link RocketEntity}
     * @throws Exception when I/O error occurs
     */
    List<RocketEntity> fetchAllRockets() throws Exception;

    /**
     * Performs network operations to find launch info against a rocket
     * @param rocketId valid rocket id whose details are needed
     * @return valid list of {@link RocketInfoEntity}
     * @throws Exception when I/O error occurs
     */
    List<RocketInfoEntity> findLaunchInfo(String rocketId) throws Exception;
}
