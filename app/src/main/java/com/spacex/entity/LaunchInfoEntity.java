package com.spacex.entity;

import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

/**
 * Entity to hold all necessary information to be used by the details screen
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class LaunchInfoEntity {
    private final List<RocketInfoEntity> launchList;
    private final ChartDataEntity chartData;

    public LaunchInfoEntity(List<RocketInfoEntity> launchList, ChartDataEntity chartData) {
        this.launchList = launchList;
        this.chartData = chartData;
    }

    public List<RocketInfoEntity> getLaunchList() {
        return launchList;
    }

    public ChartDataEntity getChartData() {
        return chartData;
    }

    public static class ChartDataEntity {
        private final List<String> labels;
        private final LineDataSet dataSet;

        public ChartDataEntity(List<String> labels, LineDataSet dataSet) {
            this.labels = labels;
            this.dataSet = dataSet;
        }

        public List<String> getLabels() {
            return labels;
        }

        public LineDataSet getDataSet() {
            return dataSet;
        }
    }
}
