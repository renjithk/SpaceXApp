package com.spacex.localstorage.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;

import java.util.List;

/**
 * Data access object for the all data manipulations within the app
 * Since there isn't many data manipulations, a single DAO is maintained for the whole app
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Dao
public interface SpaceXDAO {
    /**
     * Deletes all entries of table, tbl_rockets
     */
    @Query("DELETE FROM tbl_rockets")
    void deleteAllRockets();

    /**
     * Adds all rockets to the table, tbl_rockets
     * @param rockets list of rockets to be inserted
     */
    @Insert
    void insertAllRockets(List<RocketEntity> rockets);

    /**
     * Finds all entries from table, tbl_rockets
     * @return list of saved rockets
     */
    @Query("SELECT * FROM tbl_rockets")
    List<RocketEntity> fetchAllRockets();

    /**
     * Inserts all launche details of a specific rocket
     * @param rocketLaunches list of rocket launches
     */
    @Insert
    void insertAllLaunches(List<RocketInfoEntity> rocketLaunches);

    /**
     * Finds all launch years for a specific rocket from table. tbl_rocket_launches
     * @param rocketId valid rocket id against which the details are needed
     * @return list of launch years
     */
    @Query("SELECT launch_year FROM tbl_rocket_launches WHERE connected_rocket_id = :rocketId GROUP BY launch_year")
    List<String> findAllLaunchYearsFor(String rocketId);

    /**
     * Deletes all launch info against a specific rocket
     * @param rocketId valid rocket id whose launch info needs to be deleted
     */
    @Query("DELETE FROM tbl_rocket_launches WHERE connected_rocket_id = :rocketId")
    void deletAllLaunchInfoFor(String rocketId);

    /**
     * Finds number of launches made by a specific rocket in a year
     * @param year valid year when the launch took place
     * @param rocketId valid rocket id
     * @return number of launches made
     */
    @Query("SELECT COUNT(*) FROM tbl_rocket_launches WHERE launch_year = :year AND connected_rocket_id = :rocketId")
    int findLaunchesPerYearFor(String year, String rocketId);

    /**
     * Deletes all entry from table, tbl_rocket_launches
     */
    @Query("DELETE FROM tbl_rocket_launches")
    void deleteAllLaunchInfo();

    /**
     * Finds all launch info for a specific rocket
     * @param rocketId valid rocket id
     * @return list of rocket launches of type {@link RocketInfoEntity}
     */
    @Query("SELECT * FROM tbl_rocket_launches WHERE connected_rocket_id = :rocketId")
    List<RocketInfoEntity> findAllLaunchInfoFor(String rocketId);
}
