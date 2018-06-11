package swdev.wifi.at.fbapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import swdev.wifi.at.fbapp.db.Trip;
import swdev.wifi.at.fbapp.db.TripViewModel;

public class MainFBActivity extends AppCompatActivity {

    private TripViewModel mTripViewModel;
    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 123;
    private boolean activeTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we can not start new trip when active trip(s) present
                // TODO: 11.06.2018 uncomment, is already working!!!
                if (false) { //(mTripViewModel.activeTrips()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Hinzufügen nicht möglich, es gibt ein aktive Fahrt...",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainFBActivity.this, NewTripActivity.class);
                    startActivityForResult(intent, NEW_TRIP_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.RV_alltrips);
        final TripListAdapter adapter = new TripListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Get a new or existing ViewModel from the ViewModelProvider.
        mTripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);


        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mTripViewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                // Update the cached copy of the words in the adapter.
                adapter.setTrips(trips);
            }
        });

//        mTripViewModel.getAllActiveTrips().observe(this, new Observer<List<Trip>>() {
//            @Override
//            public void onChanged(@Nullable final List<Trip> atrips) {
//                //update activetrips boolean
//                activeTrips = (atrips.size() > 0);
//            }
//        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //Date d1 = new Date();
            Date d1 = new Date(data.getLongExtra(NewTripActivity.EXTRA_REPLY_STARTDATETIME,0));
            Trip trip = new Trip(d1,
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTLOCATION),
                    Integer.parseInt(data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTKM)),
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTCAT),
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTNOTE));
            // TODO: 08.06.2018 add note and cat if needed
            mTripViewModel.addTrip(trip);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Leere Fahrt nicht gespeichert...",
                    Toast.LENGTH_LONG).show();
        }
    }


}
