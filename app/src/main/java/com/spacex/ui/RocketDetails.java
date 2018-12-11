package com.spacex.ui;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.spacex.R;
import com.spacex.SpaceXApp;
import com.spacex.entity.LaunchInfoEntity;
import com.spacex.entity.RocketEntity;
import com.spacex.entity.RocketInfoEntity;
import com.spacex.ui.adapter.LaunchesAdapter;
import com.spacex.utils.AppConstants;
import com.spacex.utils.CommonDataKinds;
import com.spacex.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;

/**
 * This class is responsible for displaying all details of the selected rocket
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class RocketDetails extends BaseActivity {
    @Inject Lazy<LaunchesAdapter> launchesAdapter;

    @Bind(R.id.line_chart) LineChart lineChart;
    @Bind(R.id.rocket_description) TextView rocketDescription;
    @Bind(R.id.list_recycler) RecyclerView recyclerView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.toolbar_text) TextView toolbarText;
    @Bind(R.id.layout_data) NestedScrollView parentDataLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setExitTransition(new Fade(Fade.OUT));

        setContentView(R.layout.layout_rocket_details);
        ButterKnife.bind(this);
        SpaceXApp.get(this).getComponent().inject(this);

        initUI();
        recyclerView.setNestedScrollingEnabled(false);

        if(null != savedInstanceState || !Utils.isNetworkUp(this))
            new RocketInfoFetchTask(CommonDataKinds.RESTORE_DATA).execute();
        else new RocketInfoFetchTask(CommonDataKinds.INITIAL_LOAD).execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RocketEntity chosenRocket = getIntent().getParcelableExtra(AppConstants.KEY_SELECTED_ITEM);
        toolbarText.setText(chosenRocket.getName());

        //add navigation icon listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
    }

    @Override
    public void onRefresh() {
        if(!Utils.isNetworkUp(this)) {
            Snackbar.make(refreshLayout, R.string.alert_when_internet, Snackbar.LENGTH_LONG).show();
            return;
        }
        //refresh data
        new RocketInfoFetchTask(CommonDataKinds.INITIAL_LOAD).execute();
    }

    private class RocketInfoFetchTask extends AsyncTask<Void, Void, LaunchInfoEntity> {
        private final int type;

        public RocketInfoFetchTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //only show progress loader whilst making a network request
            if(CommonDataKinds.INITIAL_LOAD == type) prepareUIForDataFetch();
        }

        @Override
        protected LaunchInfoEntity doInBackground(Void... voids) {
            RocketEntity chosenRocket = getIntent().getParcelableExtra(AppConstants.KEY_SELECTED_ITEM);
            switch (type) {
                case CommonDataKinds.INITIAL_LOAD:
                    return spaceXService.get().findLaunchInfo(chosenRocket.getRocketId());
                case CommonDataKinds.RESTORE_DATA:
                    return spaceXService.get().fetchSavedLaunchInfo(chosenRocket.getRocketId());
            }
            return null;
        }

        @Override
        protected void onPostExecute(LaunchInfoEntity rocketLaunchEntity) {
            super.onPostExecute(rocketLaunchEntity);
            if(null != rocketLaunchEntity) {
                parentDataLayout.setVisibility(View.VISIBLE);
                prepareLineChart(rocketLaunchEntity.getChartData());
                prepareUI(rocketLaunchEntity.getLaunchList());
            }
            prepareUIAfterDataFetch();
        }
    }

    private void prepareUI(List<RocketInfoEntity> launchList) {
        //set description
        RocketEntity chosenRocket = getIntent().getParcelableExtra(AppConstants.KEY_SELECTED_ITEM);
        rocketDescription.setText(chosenRocket.getDescription());

        launchesAdapter.get().setData(launchList);

        if(null == recyclerView.getAdapter()) recyclerView.setAdapter(launchesAdapter.get());
        else launchesAdapter.get().notifyDataSetChanged();
    }

    private void prepareLineChart(final LaunchInfoEntity.ChartDataEntity chartData) {
        chartData.getDataSet().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        chartData.getDataSet().setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //setup X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        //setup Y-axis
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        //set X-axis labels
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return chartData.getLabels().get((int) value);
            }
        };
        xAxis.setValueFormatter(formatter);

        // Setting Data
        LineData data = new LineData(chartData.getDataSet());
        lineChart.getDescription().setText("");
        lineChart.setData(data);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }
}
