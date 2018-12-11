package com.spacex.ui;

import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;

import com.spacex.R;
import com.spacex.service.ISpaceXService;
import com.spacex.ui.custom.ProgressLoader;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;

import static com.spacex.utils.AppConstants.TAG_PROGRESS_LOADER;

/**
 * Base class for {@link RocketList} and {@link RocketDetails}
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
abstract class BaseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Inject Lazy<ISpaceXService> spaceXService;

    @Bind(R.id.list_recycler) RecyclerView recyclerView;
    @Bind(R.id.layout_swipe_refresh) SwipeRefreshLayout refreshLayout;

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    protected void initUI() {
        //initialise swipe refresh layout
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                                                android.R.color.holo_green_light,
                                                android.R.color.holo_orange_light,
                                                android.R.color.holo_red_light);
        //initialize recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_drop_down));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected void prepareUIForDataFetch() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressLoader progressLoader = ProgressLoader.instance();
                progressLoader.show(getSupportFragmentManager(), TAG_PROGRESS_LOADER);
            }
        }, 300);
    }

    protected void prepareUIAfterDataFetch() {
        refreshLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_LOADER);
                if(null != f) ((DialogFragment) f).dismiss();
            }
        },1000);
    }


}
