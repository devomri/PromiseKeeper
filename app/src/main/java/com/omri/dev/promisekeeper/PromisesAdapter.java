package com.omri.dev.promisekeeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omri.dev.promisekeeper.Model.PromiseListItem;

import java.util.List;
import java.util.ArrayList;


class PromisesAdapter extends RecyclerView.Adapter<PromisesAdapter.ViewHolder> {
    private List<PromiseListItem> mDataSet;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mDescription;
        TextView mNextTime;

        ViewHolder(View view) {
            super(view);

            mTitle= (TextView) view.findViewById(R.id.promise_list_item_title);
            mDescription = (TextView) view.findViewById(R.id.promise_list_item_description);
            mNextTime = (TextView) view.findViewById(R.id.promise_list_item_next_time);
        }
    }

    PromisesAdapter() {
        mDataSet = new ArrayList<PromiseListItem>();
        mDataSet.add(new PromiseListItem("First promise",
                                         "keeping my promises",
                                         "01/01/2017",
                                         "01/08/2017"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View promiseItemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.promise_list_item,
                        parent, false);

        return new ViewHolder(promiseItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mDataSet.get(position).getmTitle());
        holder.mDescription.setText(mDataSet.get(position).getmDescription());
        holder.mNextTime.setText(mDataSet.get(position).getmPromiseNextTime());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
