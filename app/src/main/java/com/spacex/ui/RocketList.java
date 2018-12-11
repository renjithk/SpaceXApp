package com.spacex.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.spacex.R;
import com.spacex.SpaceXApp;
import com.spacex.entity.RocketEntity;
import com.spacex.ui.adapter.RocketAdapter;
import com.spacex.utils.CommonDataKinds;
import com.spacex.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.Lazy;

import static com.spacex.utils.AppConstants.KEY_SELECTED_ITEM;

/**
 * This class is responsible for displaying list of rocket information
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RocketList extends BaseActivity {
    @Inject Lazy<RocketAdapter> rocketAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setExitTransition(new Fade(Fade.OUT));

        setContentView(R.layout.layout_rocket_list);
        ButterKnife.bind(this);
        SpaceXApp.get(this).getComponent().inject(this);

        initUI();

        RocketAdapter.IClickListener clickListener = new RocketAdapter.IClickListener() {
            @Override
            public void onItemClicked(RocketEntity chosenItem) {
                //open details screen
                Intent intent = new Intent(RocketList.this, RocketDetails.class);
                intent.putExtra(KEY_SELECTED_ITEM, chosenItem);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(RocketList.this).toBundle());
            }
        };
        rocketAdapter.get().addClickListener(clickListener);

        if(null != savedInstanceState || !Utils.isNetworkUp(this))
            new RocketFetchTask(CommonDataKinds.RESTORE_DATA).execute();
        else new RocketFetchTask(CommonDataKinds.INITIAL_LOAD).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rocket_list, menu);
        MenuItem mSearch = menu.findItem(R.id.filter_rockets);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint(getString(R.string.hint_filter_rockets));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                rocketAdapter.get().getFilter().filter(query);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        if(!Utils.isNetworkUp(this)) {
            Snackbar.make(refreshLayout, R.string.alert_when_internet, Snackbar.LENGTH_LONG).show();
            return;
        }
        //refresh data
        new RocketFetchTask(CommonDataKinds.INITIAL_LOAD).execute();
    }

    private class RocketFetchTask extends AsyncTask<Void, Void, List<RocketEntity>> {
        private final int type;

        public RocketFetchTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //only show progress loader whilst making a network request
            if(CommonDataKinds.INITIAL_LOAD == type) prepareUIForDataFetch();
        }

        @Override
        protected List<RocketEntity> doInBackground(Void... params) {
            switch (type) {
                case CommonDataKinds.INITIAL_LOAD:
                    return spaceXService.get().fetchAllRockets();
                case CommonDataKinds.RESTORE_DATA:
                    return spaceXService.get().fetchAllSavedRockets();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<RocketEntity> result) {
            super.onPostExecute(result);

            prepareUIAfterDataFetch();

            //set list data
            rocketAdapter.get().setData(result);
            if(null == recyclerView.getAdapter()) recyclerView.setAdapter(rocketAdapter.get());
            else rocketAdapter.get().notifyDataSetChanged();
        }
    }
}
