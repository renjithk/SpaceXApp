package com.spacex.service;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.spacex.entity.LaunchInfoEntity;
import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;
import com.spacex.localstorage.SpaceXDatabase;
import com.spacex.mapper.ISpaceXMapper;
import com.spacex.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation as defined in {@link ISpaceXService}
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class SpaceXService implements ISpaceXService {
    private final ISpaceXMapper mapper;
    private final SpaceXDatabase database;

    public SpaceXService(ISpaceXMapper mapper, SpaceXDatabase database) {
        this.mapper = mapper;
        this.database = database;
    }

    @Override
    public List<RocketEntity> fetchAllRockets() {
        List<RocketEntity> rocketList = null;
        try {
            //get all rockets from API
            rocketList = mapper.fetchAllRockets();
            //delete all existing cached data
            database.spaceXDAO().deleteAllLaunchInfo();
            database.spaceXDAO().deleteAllRockets();
            //add all new ones
            database.spaceXDAO().insertAllRockets(rocketList);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return rocketList;
    }

    @Override
    public List<RocketEntity> fetchAllSavedRockets() {
        try {
            return database.spaceXDAO().fetchAllRockets();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public LaunchInfoEntity findLaunchInfo(String rocketId) {
        try {
            //get detailed info of the chosen rocket
            List<RocketInfoEntity> launchList = mapper.findLaunchInfo(rocketId);
            return buildLaunchInfo(rocketId, launchList);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public LaunchInfoEntity fetchSavedLaunchInfo(String rocketId) {
        try {
            //get detailed info of the chosen rocket
            List<RocketInfoEntity> launchList = database.spaceXDAO().findAllLaunchInfoFor(rocketId);
            return buildLaunchInfo(rocketId, launchList);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private LaunchInfoEntity buildLaunchInfo(String rocketId, List<RocketInfoEntity> launchList) {
        if(Utils.isEmptyList(launchList)) return null;

        //remove existing launch info
        database.spaceXDAO().deletAllLaunchInfoFor(rocketId);

        //save all entries to database
        database.spaceXDAO().insertAllLaunches(launchList);

        //find all available years
        List<String> years = database.spaceXDAO().findAllLaunchYearsFor(rocketId);

        //build dataset list
        List<Entry> entries = new ArrayList<>(years.size());

        for(int i = 0; i < years.size(); i++) {
            int launches = database.spaceXDAO().findLaunchesPerYearFor(years.get(i), rocketId);
            entries.add(new BarEntry(i, launches));
        }

        //create data set for lie chart
        LineDataSet lineDataSet = new LineDataSet(entries, "Launches per year");

        //now build launch info entity so that view layer can easily use
        //this contains line chart data + launch info per year
        LaunchInfoEntity.ChartDataEntity chartData = new LaunchInfoEntity.ChartDataEntity(years, lineDataSet);
        LaunchInfoEntity launchInfo = new LaunchInfoEntity(launchList, chartData);

        return launchInfo;
    }
}
