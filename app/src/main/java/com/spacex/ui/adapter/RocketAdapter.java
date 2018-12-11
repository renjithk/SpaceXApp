package com.spacex.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.spacex.R;
import com.spacex.entity.RocketEntity;
import com.spacex.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter class responsible for rendering list of rockets
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class RocketAdapter extends RecyclerView.Adapter<RocketAdapter.ViewHolder> implements Filterable {
    private List<RocketEntity> dataList, mBackupList;
    private IClickListener clickListener;

    @Inject
    RocketAdapter() {
        dataList = new ArrayList<>();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rocket_row,
                parent, false);
        return new ViewHolder(view);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (Utils.isEmptyList(mBackupList)) return null;
                FilterResults results = new FilterResults();
                if(TextUtils.isEmpty(constraint)) results.values = mBackupList;
                else {
                    ArrayList<RocketEntity> filteredList = new ArrayList<RocketEntity>();
                    constraint = constraint.toString().toLowerCase();
                    for(RocketEntity rocket : mBackupList) {
                        if(rocket.isActive() &&
                           rocket.getName().toLowerCase().contains(constraint)) filteredList.add(rocket);
                    }
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.values != null) {
                    dataList = (List<RocketEntity>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public void setData(List<RocketEntity> rockets) {
        this.dataList.clear();
        mBackupList = new ArrayList<>();
        if(null != rockets) {
            this.dataList.addAll(rockets);
            this.mBackupList.addAll(rockets);
        }
    }

    public void addClickListener(IClickListener listener) {
        this.clickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.rocket_name) TextView rocketName;
        @Bind(R.id.country) TextView country;
        @Bind(R.id.engine_count) TextView engineCount;
        @Bind(R.id.tag_active) TextView activeTag;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if(null != clickListener)
                clickListener.onItemClicked(dataList.get(getAdapterPosition()));
        }

        public void bind(RocketEntity entity) {
            rocketName.setText(entity.getName());
            country.setText(entity.getCountry());
            engineCount.setText(itemView.getContext().getString(R.string.template_engine_count, entity.getEngines().getNumber()));
            activeTag.setVisibility(entity.isActive() ? View.VISIBLE : View.GONE);
        }
    }

    public interface IClickListener {
        void onItemClicked(RocketEntity chosenItem);
    }
}
