package com.omri.dev.promisekeeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


class PromisesAdapter extends RecyclerView.Adapter<PromisesAdapter.ViewHolder> {
    private String mDataSet[];

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;

        ViewHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.promise_item_name);
        }
    }

    PromisesAdapter() {
        mDataSet = new String[2];
        mDataSet[0] = "Test1";
        mDataSet[1] = "Test2";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View promiseItemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.promise_item,
                        parent, false);

        return new ViewHolder(promiseItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
