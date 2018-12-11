package com.spacex.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides available options
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@IntDef({CommonDataKinds.INITIAL_LOAD, CommonDataKinds.RESTORE_DATA})
@Retention(RetentionPolicy.SOURCE)
public @interface CommonDataKinds {
    int INITIAL_LOAD = 1;
    int RESTORE_DATA = 2;
}
