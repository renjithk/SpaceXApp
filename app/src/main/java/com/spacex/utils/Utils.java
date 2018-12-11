package com.spacex.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for providing all utility operations
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class Utils {
    /**
     * Checks whether a list is empty or not
     * @param list list of any type
     * @return true if the list is empty and false otherwise
     */
    public static boolean isEmptyList(List<?> list) {
        return null == list || list.isEmpty();
    }

    /**
     * Returns current state of network connectivity
     * @param context a valid application context
     * @return true if there is internet connection and false otherwise
     */
    public static boolean isNetworkUp(Context context) {
        final ConnectivityManager connectionHandler =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectionHandler.getActiveNetworkInfo();
        //notify user you are online
        //notify user you are not online
        return networkInfo != null && networkInfo.isConnected();
    }
}
