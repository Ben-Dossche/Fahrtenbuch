package swdev.wifi.at.fbapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import swdev.wifi.at.fbapp.db.Trip;
import swdev.wifi.at.fbapp.db.TripViewModel;

public class MainFBActivity extends AppCompatActivity {

    private TripViewModel mTripViewModel;
    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 123;
    public static final int EDIT_ACTIVETRIP_ACTIVITY_REQUEST_CODE = 456;
    public static final int EDIT_OPENTRIP_ACTIVITY_REQUEST_CODE = 789;
    private boolean activeTrips;
    private DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy, HH:mm",
            Locale.GERMAN);

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Trip trip = (Trip) v.getTag();
            //depending on tripdata we show activity for ACTIVE or OPEN trip
            if (trip.getFinishKm() > 0) {
                Intent intent = new Intent(MainFBActivity.this, EditOpenTripActivity.class);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPID, trip._id);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTLOC, trip.getStartLocation());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTDATETIME, df.format(trip.getStart()));
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTKM, trip.getStartKm());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTNOTE, trip.getNote());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTCAT, trip.getCategory());

                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_ENDLOCATION, trip.getFinishLocation());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_ENDKM, trip.getFinishKm());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_ENDDATETIME, df.format(trip.getFinish()));

                startActivityForResult(intent, EDIT_OPENTRIP_ACTIVITY_REQUEST_CODE);
            } else {
                Intent intent = new Intent(MainFBActivity.this, EditActiveTripActivity.class);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPID, trip._id);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTLOC, trip.getStartLocation());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTDATETIME, df.format(trip.getStart()));
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTKM, trip.getStartKm());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTNOTE, trip.getNote());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTCAT, trip.getCategory());
                startActivityForResult(intent, EDIT_ACTIVETRIP_ACTIVITY_REQUEST_CODE);
            }
        }
    };

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
                if (mTripViewModel.activeTrips()) { //(mTripViewModel.activeTrips()) {
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
        final TripListAdapter adapter = new TripListAdapter(this, itemClickListener);
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
        Trip trip;
        Date d1;
        Date dnow;
        int tripid;
        String tripNote;

        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            d1 = new Date(data.getLongExtra(NewTripActivity.EXTRA_REPLY_STARTDATETIME, 0));
            trip = new Trip(d1,
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTLOCATION),
                    Integer.parseInt(data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTKM)),
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTCAT),
                    data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTNOTE));
            mTripViewModel.addTrip(trip);
        } else if (requestCode == EDIT_ACTIVETRIP_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                d1 = new Date(data.getLongExtra(EditActiveTripActivity.EXTRA_REPLY_ENDDATETIME, 0));
                tripid = data.getIntExtra(EditActiveTripActivity.EXTRA_REPLY_ENDTRIPID, 0);
                //VALID TRIPID
                if (tripid > 0) {
                    //UPDATE TRIP
                    if (EditActiveTripActivity.EXTRA_ACTION_UPDATE.equals(data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ACTION))) {
                        trip = mTripViewModel.getTripById(tripid);
                        d1 = new Date(data.getLongExtra(EditActiveTripActivity.EXTRA_REPLY_ENDDATETIME, 0));
                        trip.setFinish(d1);
                        trip.setFinishKm(Integer.parseInt(data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDKM)));
                        trip.setFinishLocation(data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDLOCATION));
                        String sCat = data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDCAT);
                        if (sCat.equals("beruflich")) {
                            trip.setCategory(1);
                        } else {
                            trip.setCategory(0);
                        }
                        tripNote = data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDNOTE);
                        if (tripNote != null && !tripNote.isEmpty()) {
                            trip.setNote(tripNote);
                            //if note also present then we can finish this trip (ie set saved_at date)
                            dnow = new Date();
                            trip.setSavedAt(dnow);
                        }

                        mTripViewModel.updateTrip(trip);
                        //DELETE TRIP
                    } else if (EditActiveTripActivity.EXTRA_ACTION_DELETE.equals(data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ACTION))) {
                        mTripViewModel.deleteTrip(tripid);
                        Toast.makeText(
                                getApplicationContext(),
                                "Aktive Fahrt wurde gelöscht.",
                                Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Änderungen aktive Fahrt NICHT gespeichert, unbekannte ID.",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Änderungen aktive Fahrt wurden nicht gespeichert...",
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == EDIT_OPENTRIP_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tripid = data.getIntExtra(EditActiveTripActivity.EXTRA_REPLY_ENDTRIPID, 0);
                //VALID TRIPID
                if (tripid > 0) {
                    //UPDATE TRIP
                    trip = mTripViewModel.getTripById(tripid);
                    String sCat = data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDCAT);
                    if (sCat.equals("beruflich")) {
                        trip.setCategory(1);
                    } else {
                        trip.setCategory(0);
                    }
                    tripNote = data.getStringExtra(EditActiveTripActivity.EXTRA_REPLY_ENDNOTE);
                    if (tripNote != null && !tripNote.isEmpty()) {
                        trip.setNote(tripNote);
                        //if note also present then we can finish this trip (ie set saved_at date)
                        dnow = new Date();
                        trip.setSavedAt(dnow);
                    }
                    mTripViewModel.updateTrip(trip);

                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Änderungen offene Fahrt NICHT gespeichert, unbekannte ID.",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Änderungen offene Fahrt NICHT gespeichert...",
                        Toast.LENGTH_LONG).show();
            }
        } else{
                Toast.makeText(
                        getApplicationContext(),
                        "Leere Fahrt nicht gespeichert...",
                        Toast.LENGTH_LONG).show();
            }
        }


    }
