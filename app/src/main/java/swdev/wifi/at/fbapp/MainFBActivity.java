package swdev.wifi.at.fbapp;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public static final int EXPORT_ACTIVITY_REQUEST_CODE = 153;
    private boolean activeTrips;
    private DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy, HH:mm",
            Locale.GERMAN);

    private String exportEmail;
    private Long exportFrom;
    private Long exportTill;
    private String  exportCat;

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Trip trip = (Trip) v.getTag();
            //depending on tripdata we show activity for ACTIVE or OPEN trip
            //OPEN TRIP
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
            //ACTIVE TRIP
            } else {
                Intent intent = new Intent(MainFBActivity.this, EditActiveTripActivity.class);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPID, trip._id);
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTLOC, trip.getStartLocation());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTDATETIME, df.format(trip.getStart()));
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTKM, trip.getStartKm());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTNOTE, trip.getNote());
                intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_TRIPSTARTCAT, trip.getCategory());
                /*FEATURE DISABLED
                //pass endlocation if present (retourtrip)
                if (trip.getFinishLocation() != null && !trip.getFinishLocation().isEmpty()) {
                    intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_ENDLOCATION, trip.getFinishLocation());
                } else {
                    intent.putExtra(EditActiveTripActivity.EXTRA_REPLY_ENDLOCATION, "---");
                }
                */
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
                    //we retrieve data of last trip and past to newtripactivity
                    Intent intent = new Intent(MainFBActivity.this, NewTripActivity.class);
                    Trip lastTrip = mTripViewModel.getLastTrip();
                    if (lastTrip != null) {
                        intent.putExtra(NewTripActivity.EXTRA_LASTSTARTLOCATION,lastTrip.getStartLocation());
                        intent.putExtra(NewTripActivity.EXTRA_LASTENDKM, lastTrip.getFinishKm());
                        intent.putExtra(NewTripActivity.EXTRA_LASTENDLOCATION,lastTrip.getFinishLocation());
                    } else {
                        intent.putExtra(NewTripActivity.EXTRA_LASTSTARTLOCATION,"---");
                    }

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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_export) {
            exportData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Trip trip;
        Date d1;
        Date dnow;
        int tripid;
        String tripNote;

        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE ) {
            if (resultCode == RESULT_OK) {
                d1 = new Date(data.getLongExtra(NewTripActivity.EXTRA_REPLY_STARTDATETIME, 0));
                trip = new Trip(d1,
                        data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTLOCATION),
                        Integer.parseInt(data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTKM)),
                        data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTCAT),
                        data.getStringExtra(NewTripActivity.EXTRA_REPLY_STARTNOTE));
                String endLoc = data.getStringExtra(NewTripActivity.EXTRA_REPLY_ENDLOCATION);
            /*FEATURE DISABLED
            //if we also received an endlocation (retourtrip) we store this in the new trip
            if (!endLoc.equals("---")) {
                trip.setFinishLocation(endLoc);
            }
            */
                mTripViewModel.addTrip(trip);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Leere Fahrt nicht gespeichert...",
                        Toast.LENGTH_LONG).show();
            }
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
                        } else {
                            //if no note present and private trip (cat=0) then we can finish this trip (note is optional for private trips)
                            if (trip.getCategory() == 0) {
                                dnow = new Date();
                                trip.setSavedAt(dnow);
                            }
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
                    } else {
                        //if no note present and private trip (cat=0) then we can finish this trip (note is optional for private trips)
                        if (trip.getCategory() == 0) {
                            dnow = new Date();
                            trip.setSavedAt(dnow);
                        }
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
        } else if (requestCode == EXPORT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                exportEmail = data.getStringExtra(ExportActivity.EXTRA_REPLY_EMAIL);
                exportFrom = data.getLongExtra(ExportActivity.EXTRA_REPLY_EXPORTFROM,0);
                exportTill = data.getLongExtra(ExportActivity.EXTRA_REPLY_EXPORTTILL,0);
                exportCat = data.getStringExtra(ExportActivity.EXTRA_REPLY_EXPORTCATEGORY);
                startExport();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Kein Export...",
                        Toast.LENGTH_LONG).show();
            }

        }

        }


    public void exportData() {
        if (mTripViewModel.openTrips()) { //(mTripViewModel.activeTrips()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Export nicht möglich, nicht alle Fahrten sind gespeichert...",
                    Toast.LENGTH_LONG).show();
        } else {
            //we retrieve data of last trip and past to newtripactivity
            Intent intent = new Intent(MainFBActivity.this, ExportActivity.class);
            Trip lastTrip = mTripViewModel.getLastTrip();
            // TODO: 15.06.2018 pass default email
               //   intent.putExtra(NewTripActivity.EXTRA_LASTSTARTLOCATION,lastTrip.getStartLocation());
            startActivityForResult(intent, EXPORT_ACTIVITY_REQUEST_CODE);
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks permission */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    private void startExport() {
        if (isExternalStorageWritable() && (isStoragePermissionGranted())) {
                proceedExport();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Export nicht möglich: externe Speicher nicht verfügbar / keine Berechtingung",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            proceedExport();
        }
    }

    private void proceedExport() {
        //we have permission to proceed export
        String dataString;
        String catString;

        File file = null;
        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            //RETRIEVE TRIPDATA
            List<Trip> trips;
            if (exportCat.equals("nur berufliche Fahrten")) {
                trips = mTripViewModel.GetBusinessTripsForTimeFrame(exportFrom, exportTill);
            } else {
                trips = mTripViewModel.GetTripsForTimeFrame(exportFrom, exportTill);
            }

            if (trips.size() == 0) {
                Toast.makeText(
                        getApplicationContext(),
                        "Keine Fahrten zu exportieren...",
                        Toast.LENGTH_LONG).show();
            } else {
                //CREATE CSV FILE AND EXPORT DATA
                File directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file = new File(directoryDownload, "fahrtenbuchdata.csv");
                file.delete();
                file = new File(directoryDownload, "fahrtenbuchdata.csv");

                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(file, true));
                    bw.write("Abfahrt,Ort Abfahrt,Km Abfahrt,Ankunf,Ort Ankunft,Km Ankunft, Gefahrene Km, Beruflich");
                    bw.newLine();
                    final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMAN);
                    for (Trip trip : trips) {
                        if (trip.getCategory() == 1) {
                            catString = "X";
                        } else {
                            catString = "";
                        }
                        dataString = df.format(trip.getStart()) + "," +
                                trip.getStartLocation() + "," +
                                trip.getStartKm() + "," +
                                df.format(trip.getFinish()) + "," +
                                trip.getFinishLocation() + "," +
                                trip.getFinishKm() + "," +
                                (trip.getFinishKm() - trip.getStartKm()) + "," +
                                catString;
                        bw.write(dataString);
                        bw.newLine();
                    }

                    //bw.write("sadföslkj ewrwr,kölfjsf ösfkj,12/03/2018 12:45,45353,243" + "\n");
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Export exception",
                            Toast.LENGTH_LONG).show();

                }
                /*WORKING !!!*/
                //SEND EMAIL WITH ATTACHMENT
                try {
                    Uri u1 = null;
                    u1 = Uri.fromFile(file);
                    if (u1 != null) {
                        //ATTENTION
                        //as of android 6 in gmail app
                        //Settings->Apps->Gmail->Permissions and enable the "Storage" permission manually,
                        //otherwise attachment does not work!!!
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("plain/text");
                        String to[] = {exportEmail};
                        sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Fahrtenbuch Export");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Fahrtenbuch daten...");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                        startActivity(sendIntent);
                    }
                } catch (Throwable t) {
                    Toast.makeText(
                            getApplicationContext(),
                            "mail failed",
                            Toast.LENGTH_LONG).show();
                }
//*/
                Toast.makeText(
                        getApplicationContext(),
                        "Export fertig",
                        Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Export fehlgeschlagen",
                    Toast.LENGTH_LONG).show();
        }


    }



}
