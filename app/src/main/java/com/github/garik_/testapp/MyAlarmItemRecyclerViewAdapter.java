package com.github.garik_.testapp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.garik_.testapp.AlarmItemFragment.OnListFragmentInteractionListener;
import com.github.garik_.testapp.databinding.AlarmsItemBinding;

import java.util.List;


public class MyAlarmItemRecyclerViewAdapter extends RecyclerView.Adapter<MyAlarmItemRecyclerViewAdapter.ViewHolder> implements IDatabaseHandler {

    private final List<Alarm> mValues;

    @Override
    public void addAlarm(Alarm alarm) {
        mValues.add(alarm);
        notifyDataSetChanged();
    }

    @Override
    public Alarm getAlarm(int index) {
        return mValues.get(index);
    }

    @Override
    public List<Alarm> getAllAlarms() {
        return mValues;
    }

    @Override
    public int updateAlarm(Alarm alarm) {
        int updated = 0;

        for (int i = 0; i < mValues.size(); i++) {
            Alarm value = this.getAlarm(i);


            if (value.getId() == alarm.getId()) {
                mValues.set(i, alarm);
                updated = 1;

                notifyDataSetChanged();
                break;
            }
        }

        return updated;
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        for (int i = 0; i < mValues.size(); i++) {
            Alarm value = this.getAlarm(i);
            if (value.getId() == alarm.getId()) {

                mValues.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void deleteAll() {
        mValues.clear();
        notifyDataSetChanged();
    }

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
}
