package com.omri.dev.promisekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;

import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseTypes;

import org.w3c.dom.Text;

import java.util.List;
import java.util.ArrayList;


class PromisesAdapter extends RecyclerView.Adapter<PromisesAdapter.ViewHolder> {
    private List<PromiseListItem> mDataSet;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mID;
        TextView mTitle;
        TextView mDescription;
        TextView mBaseDate;
        ImageView mIcon;

        ViewHolder(View view) {
            super(view);
            context = view.getContext();

            mID = (TextView)view.findViewById(R.id.promise_list_item_id);
            mTitle= (TextView) view.findViewById(R.id.promise_list_item_title);
            mDescription = (TextView) view.findViewById(R.id.promise_list_item_description);
            mBaseDate = (TextView) view.findViewById(R.id.promise_list_item_base_time);
            mIcon = (ImageView)view.findViewById(R.id.promise_list_item_icon);
        }
    }

    PromisesAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void updatePromisesDataSet(List<PromiseListItem> newPromisesDataSet) {
        mDataSet = newPromisesDataSet;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View promiseItemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.promise_list_item,
                        parent, false);

        promiseItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int promiseID = Integer.parseInt(((TextView)v.findViewById(R.id.promise_list_item_id)).getText().toString());
                PromiseListItem currPromise = mDataSet.get(promiseID);

                Intent intent = currPromise.toIntent(context, PromiseDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        return new ViewHolder(promiseItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PromiseListItem currPromise = mDataSet.get(position);

        holder.mID.setText(String.valueOf(position));
        String promiseTitle = currPromise.getmTitle();
        holder.mTitle.setText(promiseTitle);
        holder.mDescription.setText(currPromise.getmDescription());
        holder.mBaseDate.setText(currPromise.getmBaseTime());

        if (currPromise.getmPromiseType() == PromiseTypes.GENERAL) {
            holder.mIcon.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_popup_reminder));
        } else if (currPromise.getmPromiseType() == PromiseTypes.LOCATION) {
            holder.mIcon.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_dialog_map));
        } else if (currPromise.getmPromiseType() == PromiseTypes.CALL) {
            holder.mIcon.setImageDrawable(context.getResources().getDrawable(android.R.drawable.stat_sys_phone_call));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
