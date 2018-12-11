package com.spacex.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Entity to map individual roacket item
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@Entity(tableName = "tbl_rockets")
public class RocketEntity implements Parcelable {
    @NonNull
    @Expose @SerializedName("id")
    private int id;

    @NonNull
    @Expose @SerializedName("rocket_name")
    private String name;

    @NonNull @PrimaryKey
    @Expose @SerializedName("rocket_id")
    @ColumnInfo(name = "rocket_id")
    private String rocketId;

    @NonNull
    @Expose @SerializedName("country")
    private String country;

    @Expose @SerializedName("active")
    private boolean active;

    @Nullable
    @Expose @SerializedName("description")
    private String description;

    @Embedded
    @Expose @SerializedName("engines")
    EngineEntity engines;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getRocketId() {
        return rocketId;
    }

    public void setRocketId(@NonNull String rocketId) {
        this.rocketId = rocketId;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public EngineEntity getEngines() {
        return engines;
    }

    public void setEngines(EngineEntity engines) {
        this.engines = engines;
    }

    public static class EngineEntity implements Parcelable {
        @Expose @SerializedName("number")
        private int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public EngineEntity() {}

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.number);
        }

        protected EngineEntity(Parcel in) {
            this.number = in.readInt();
        }

        public static final Creator<EngineEntity> CREATOR = new Creator<EngineEntity>() {
            @Override
            public EngineEntity createFromParcel(Parcel source) {
                return new EngineEntity(source);
            }

            @Override
            public EngineEntity[] newArray(int size) {
                return new EngineEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RocketEntity() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.rocketId);
        dest.writeString(this.country);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeString(this.description);
        dest.writeParcelable(this.engines, flags);
    }

    protected RocketEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.rocketId = in.readString();
        this.country = in.readString();
        this.active = in.readByte() != 0;
        this.description = in.readString();
        this.engines = in.readParcelable(EngineEntity.class.getClassLoader());
    }

    public static final Creator<RocketEntity> CREATOR = new Creator<RocketEntity>() {
        @Override
        public RocketEntity createFromParcel(Parcel source) {
            return new RocketEntity(source);
        }

        @Override
        public RocketEntity[] newArray(int size) {
            return new RocketEntity[size];
        }
    };
}
