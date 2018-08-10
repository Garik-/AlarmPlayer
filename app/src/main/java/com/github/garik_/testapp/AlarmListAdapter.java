package com.github.garik_.testapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private List<Alarm> alarmList;

    public class ViewHolder extends RecyclerView.ViewHolder {


        AlarmsItemBinding mBinding;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View view) {
            super(view);

            mBinding = DataBindingUtil.bind(view);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }

    }

    public AlarmListAdapter(Context context, List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AlarmsItemBinding binding = AlarmsItemBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Alarm item = alarmList.get(position);
        holder.mBinding.setAlarm(item);

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }


    public void removeItem(int position) {
        alarmList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void changeItem(Alarm item, int position) {
        alarmList.set(position, item);
        notifyItemChanged(position);
    }

    public void addItem(Alarm item) {
        alarmList.add(item);
        int position = alarmList.size() - 1;
        notifyItemInserted(position);
    }
}
