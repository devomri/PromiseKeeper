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

import java.util.List;
import java.util.ArrayList;


class PromisesAdapter extends RecyclerView.Adapter<PromisesAdapter.ViewHolder> {
    private List<PromiseListItem> mDataSet;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mDescription;
        TextView mNextDate;
        ImageView mIcon;

        ViewHolder(View view) {
            super(view);
            context = view.getContext();

            mTitle= (TextView) view.findViewById(R.id.promise_list_item_title);
            mDescription = (TextView) view.findViewById(R.id.promise_list_item_description);
            mNextDate = (TextView) view.findViewById(R.id.promise_list_item_next_date);
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
                String title = ((TextView)v.findViewById(R.id.promise_list_item_title)).getText().toString();
                String nextDate = ((TextView)v.findViewById(R.id.promise_list_item_next_date)).getText().toString();
                String description = ((TextView)v.findViewById(R.id.promise_list_item_description)).getText().toString();

                Intent intent = new Intent(context, PromiseDetailsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("nextDate", nextDate);
                intent.putExtra("description", description);
                context.startActivity(intent);
            }
        });

        return new ViewHolder(promiseItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PromiseListItem currPromise = mDataSet.get(position);

        holder.mTitle.setText(currPromise.getmTitle());
        holder.mDescription.setText(currPromise.getmDescription());
        holder.mNextDate.setText(currPromise.getPromiseNextTime());

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
