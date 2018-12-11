package com.spacex.ui.custom;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacex.R;

/**
 * Progress loader dialog with custom messages
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class ProgressLoader extends DialogFragment {
    /**
     * Creates a new instance of ProgressLoader
     * @return newly created instance of type {@link ProgressLoader}
     */
    public static ProgressLoader instance() {
        return new ProgressLoader();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_progress_loader, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        if(null != getDialog() && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
