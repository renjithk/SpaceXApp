package com.spacex.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Entity to map details of selected rocket
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Entity(tableName = "tbl_rocket_launches",
        foreignKeys = {
                        @ForeignKey(entity = RocketEntity.class,
                                    parentColumns = "rocket_id",
                                    childColumns = "connected_rocket_id")
                       },
        indices = {@Index("connected_rocket_id")})
public class RocketInfoEntity {
    @NonNull @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @Expose @SerializedName("mission_name")
    @ColumnInfo(name = "mission_name")
    private String missionName;

    @NonNull
    @Expose @SerializedName("launch_year")
    @ColumnInfo(name = "launch_year")
    private String launchYear;

    @NonNull
    @Expose @SerializedName("launch_date_utc")
    @ColumnInfo(name = "launch_date")
    private String launchDate;

    @NonNull
    @Expose @SerializedName("launch_success")
    @ColumnInfo(name = "is_launch_success")
    private boolean launchSuccess;

    @NonNull @Embedded
    private LinksEntity links;

    @Nullable @Embedded
    @Expose @SerializedName("rocket")
    private ConnectedRocketEntity connectRocket;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(@NonNull String missionName) {
        this.missionName = missionName;
    }

    @NonNull
    public String getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(@NonNull String launchYear) {
        this.launchYear = launchYear;
    }

    @NonNull
    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(@NonNull String launchDate) {
        this.launchDate = launchDate;
    }

    @NonNull
    public boolean isLaunchSuccess() {
        return launchSuccess;
    }

    public void setLaunchSuccess(@NonNull boolean launchSuccess) {
        this.launchSuccess = launchSuccess;
    }

    @NonNull
    public LinksEntity getLinks() {
        return links;
    }

    public void setLinks(@NonNull LinksEntity links) {
        this.links = links;
    }

    @Nullable
    public ConnectedRocketEntity getConnectRocket() {
        return connectRocket;
    }

    public void setConnectRocket(@Nullable ConnectedRocketEntity connectRocket) {
        this.connectRocket = connectRocket;
    }

    public String launchDateAsString(Context context) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                                                Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = formatter.parse(launchDate);

            return DateUtils.formatDateTime(context, date.getTime(),
                    DateUtils.FORMAT_ABBREV_MONTH |
                            DateUtils.FORMAT_SHOW_DATE |
                            DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME);

        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String status() {
        return launchSuccess ? "Success" : "Fail";
    }

    public static class LinksEntity {
        @Expose @SerializedName("mission_patch")
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class ConnectedRocketEntity {
        @Nullable
        @Expose @SerializedName("rocket_id")
        @ColumnInfo(name = "connected_rocket_id")
        private String connectedRocketId;

        @Nullable
        public String getConnectedRocketId() {
            return connectedRocketId;
        }

        public void setConnectedRocketId(@Nullable String connectedRocketId) {
            this.connectedRocketId = connectedRocketId;
        }
    }
}
