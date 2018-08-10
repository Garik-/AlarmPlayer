package com.github.garik_.testapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// TODO: посмотри проект https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/  там описана логика работы с данными

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    static final String ACTION = "action";
    static final String POSITION = "position";
    static final int ACTION_UPDATE = 0;
    static final int ACTION_CREATE = 1;

    private RecyclerView recyclerView;
    private List<Alarm> cartList;
    private AlarmListAdapter mAdapter;

    private BroadcastReceiver mActivityReceiver;
    private DatabaseHandler DB;
    private AlarmBroadcastReceiver mAlarmReceiver;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mActivityReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        DB = new DatabaseHandler(this);

        recyclerView = findViewById(R.id.recycler_view);

        cartList = new ArrayList<>();
        mAdapter = new AlarmListAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Alarm alarm = cartList.get(position);
                Intent intent = new Intent(context, InsertActivity.class);
                intent.putExtras(alarm.toBundle());
                intent.putExtra(ACTION, ACTION_UPDATE);
                intent.putExtra(POSITION, position);

                Log.d(GarikApp.TAG, "click intent position: " + position);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InsertActivity.class);
                intent.putExtra(ACTION, ACTION_CREATE);
                startActivity(intent);
            }
        });


        mActivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (null != extras) {
                    Alarm alarmInfo = new Alarm(extras);

                    final int action = extras.getInt(MainActivity.ACTION);
                    final int position = extras.getInt(MainActivity.POSITION);


                    switch (action) {
                        case MainActivity.ACTION_CREATE:
                            mAlarmReceiver.setAlarm(getApplication(), alarmInfo);
                            DB.addAlarm(alarmInfo);
                            mAdapter.addItem(alarmInfo);
                            break;
                        case MainActivity.ACTION_UPDATE:
                            mAlarmReceiver.cancelAlarm(getApplication(), alarmInfo);
                            mAlarmReceiver.setAlarm(getApplication(), alarmInfo);

                            DB.updateAlarm(alarmInfo);
                            mAdapter.changeItem(alarmInfo, position);
                            break;
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(InsertActivity.BROADCAST_ACTION);
        this.registerReceiver(mActivityReceiver, filter);
        mAlarmReceiver = new AlarmBroadcastReceiver();

        createAlarms();
        prepareCart();
    }

    private void createAlarms() {
        GarikApp app = (GarikApp) getApplication();

        for (Alarm a : DB.getNewAlarms(app.getKeys())) {
            mAlarmReceiver.setAlarm(getApplication(), a);
            DB.updateAlarm(a);
        }
    }

    private void prepareCart() {


        // adding items to cart list
        cartList.clear();
        cartList.addAll(DB.getAllAlarms());

        // refreshing recycler view
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AlarmListAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            //String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Alarm deletedItem = cartList.get(deletedIndex);


            // remove the item from recycler view
            mAdapter.removeItem(deletedIndex);

            mAlarmReceiver.cancelAlarm(getApplicationContext(), deletedItem);
            DB.deleteAlarm(deletedItem);
        }
    }
}
