package com.github.garik_.testapp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.garik_.testapp.AlarmItemFragment.OnListFragmentInteractionListener;
import com.github.garik_.testapp.databinding.AlarmsItemBinding;

import java.util.List;

/*
https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/

// notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);


          // notify item added by position
        notifyItemInserted(position);
 */

public class MyAlarmItemRecyclerViewAdapter extends RecyclerView.Adapter<MyAlarmItemRecyclerViewAdapter.ViewHolder> {

    private final List<Alarm> mValues;

    private final OnListFragmentInteractionListener mListener;

    public MyAlarmItemRecyclerViewAdapter(List<Alarm> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AlarmsItemBinding binding = AlarmsItemBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBinding.setAlarm(holder.mItem);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public Alarm mItem;
        AlarmsItemBinding mBinding;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
        }

    }

    public void removeItem(int position) {
        mValues.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Alarm item, int position) {
        mValues.add(position, item);
        // ncartLotify item added by position
        notifyItemInserted(position);
    }
}
