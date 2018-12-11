package com.spacex.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacex.R;
import com.spacex.entity.RocketInfoEntity;
import com.spacex.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This class is responsible for rendering list of rocket launches
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class LaunchesAdapter extends RecyclerView.Adapter<LaunchesAdapter.ViewHolder> {
    private final List<RocketInfoEntity> dataList;

    @Inject
    LaunchesAdapter() {
        dataList = new ArrayList<>();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_launches_row,
                                        parent, false);
        return new LaunchesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getId();
    }

    public void setData(List<RocketInfoEntity> data) {
        this.dataList.clear();
        if(null != data) {
            this.dataList.addAll(data);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.launch_year) TextView launchYear;
        @Bind(R.id.mission_name) TextView missionName;
        @Bind(R.id.launch_date) TextView launchDate;
        @Bind(R.id.status) TextView status;
        @Bind(R.id.icon_mission_patch) AppCompatImageView patchImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RocketInfoEntity entity) {
            launchYear.setText(entity.getLaunchYear());
            missionName.setText(entity.getMissionName());
            launchDate.setText(entity.launchDateAsString(itemView.getContext()));
            status.setText(entity.status());
            GlideApp.with(itemView.getContext())
                    .load(entity.getLinks().getImage())
                    .override(32, 32)
                    .into(patchImage);
        }
    }
}
