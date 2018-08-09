package com.github.garik_.testapp;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    private static final String TAG = MainActivity.class.getSimpleName();

    static final String ACTION = "action";
    static final String POSITION = "position";
    static final int ACTION_UPDATE = 0;
    static final int ACTION_CREATE = 1;

    private RecyclerView recyclerView;
    private List<Alarm> cartList;
    private AlarmListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    private BroadcastReceiver mActivityReceiver;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mActivityReceiver);
    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        cartList = new ArrayList<>();
        mAdapter = new AlarmListAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);

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
                Log.d(GarikApp.TAG, "main recive");
            }
        };

        IntentFilter filter = new IntentFilter(InsertActivity.BROADCAST_ACTION);
        this.registerReceiver(mActivityReceiver, filter);

        prepareCart();


       /* DatabaseHandler db = new DatabaseHandler(this);
        Alarm alarm = new Alarm("test2123", System.currentTimeMillis(), 1000 * 60, 2);
        db.addAlarm(alarm);

        alarm.setFilePath("test35345");
        db.addAlarm(alarm);*/


        /*
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {





           // receiver.setAlarm(this.getApplicationContext(), new Alarm(filePath, System.currentTimeMillis(), 1000*60, 2));

            DatabaseHandler db = new DatabaseHandler(this);


            Log.d(GarikApp.TAG, "Inserting...");
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/1.mp3";

            Alarm alarm = new Alarm(filePath, System.currentTimeMillis(), 1000*60, 2);
            db.addAlarm(alarm);

            List<Alarm> alarms = db.getAllAlarms();

            AlarmBroadcastReceiver receiver = new AlarmBroadcastReceiver();


            for(Alarm a : alarms) {
                Log.d(GarikApp.TAG, "Set alarm "+a.getFilePath());

                int uniqueId = receiver.setAlarm(this.getApplicationContext(), a);
                a.setUniqueId(uniqueId);

                db.updateAlarm(a);
            }

            db.deleteAll();
        }*/
    }

    private void prepareCart() {
        DatabaseHandler db = new DatabaseHandler(this);

        // adding items to cart list
        cartList.clear();
        cartList.addAll(db.getAllAlarms());

        // refreshing recycler view
        mAdapter.notifyDataSetChanged();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AlarmListAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            //String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Alarm deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, " Removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
