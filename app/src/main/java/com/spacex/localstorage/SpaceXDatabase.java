package com.spacex.localstorage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;
import com.spacex.localstorage.dao.SpaceXDAO;

/**
 * Abstract definition for the database
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Database(entities = {RocketEntity.class, RocketInfoEntity.class}, version = 1, exportSchema = false)
public abstract class SpaceXDatabase extends RoomDatabase {
    public abstract SpaceXDAO spaceXDAO();
}
