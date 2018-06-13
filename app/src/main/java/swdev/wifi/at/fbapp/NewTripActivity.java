package swdev.wifi.at.fbapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_REPLY_STARTLOCATION = "startlocation";
    public static final String EXTRA_REPLY_STARTKM = "startkm";
    public static final String EXTRA_REPLY_STARTDATETIME = "startdatetime";
    public static final String EXTRA_REPLY_STARTNOTE = "startnote";
    public static final String EXTRA_REPLY_STARTCAT = "startcat";
    public static final String EXTRA_REPLY_ENDLOCATION = "endlocation";

    public static final String EXTRA_LASTSTARTLOCATION = "laststartlocation";
    public static final String EXTRA_LASTENDKM = "laststartkm";
    public static final String EXTRA_LASTENDLOCATION = "lastendlocation";

    private EditText etStartLocation;
    private EditText etStartDate;
    private EditText etStartTime;
    private EditText etStartKm;
    private EditText etNote;
    private EditText etStartAddress;
    private Switch swCat;
    private ImageButton btRetourTrip;
    private ImageButton btLastKm;
    
    private String lastStartLocation;
    private String lastEndLocation;
    private int lastEndKm;
    private String replyEndLocation;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        etStartLocation = findViewById(R.id.ET_StartLocation);
        etStartDate = findViewById(R.id.ET_StartDate);
        etStartTime = findViewById(R.id.ET_StartTime);
        etStartKm = findViewById(R.id.ET_StartKm);
        etNote = findViewById(R.id.ET_Note);
        swCat = findViewById(R.id.SW_Category);
        etStartAddress = findViewById(R.id.ET_StartAddress);
        btRetourTrip = findViewById(R.id.BT_RetourTrip);
        btLastKm = findViewById(R.id.BT_LastKm);
        replyEndLocation = "---";
        
        //RETRIEVE LAST TRIP INFO AND IF PRESENT THEN DISPLAY RETOURTRIP BUTTON
        lastStartLocation = getIntent().getExtras().getString(EXTRA_LASTSTARTLOCATION);
        if (!lastStartLocation.equals("---")) {
            lastEndLocation = getIntent().getExtras().getString(EXTRA_LASTENDLOCATION);
            lastEndKm =getIntent().getExtras().getInt(EXTRA_LASTENDKM);
            btRetourTrip.setVisibility(View.VISIBLE);
        } else {
            btRetourTrip.setVisibility(View.GONE);
        }

        //FILL IN CURRENT DATE & TIME BY DEFAULT
        Date d1 = new Date();
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
        final DateFormat tf = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        etStartDate.setText(df.format(d1));
        etStartTime.setText(tf.format(d1));

        //SAVE BUTTON CLICK
        final Button button = findViewById(R.id.BT_StartTrip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyintent = new Intent();
                Date dstart;

                //obligatory fields must have data
                if ((TextUtils.isEmpty(etStartLocation.getText())) ||
                        (TextUtils.isEmpty(etStartAddress.getText())) ||
                        (TextUtils.isEmpty(etStartDate.getText())) ||
                        (TextUtils.isEmpty(etStartTime.getText())) ||
                        (TextUtils.isEmpty(etStartKm.getText()))) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Bitte zuerst pflichtfelder eintragen...",
                            Toast.LENGTH_LONG).show();
                    return;
                }


                replyintent.putExtra(EXTRA_REPLY_STARTLOCATION, etStartAddress.getText().toString() + " - " + etStartLocation.getText().toString());
                replyintent.putExtra(EXTRA_REPLY_STARTKM, etStartKm.getText().toString());
                if (!TextUtils.isEmpty(etNote.getText())) {
                    replyintent.putExtra(EXTRA_REPLY_STARTNOTE, etNote.getText().toString());
                }
                if (swCat.isChecked()) {
                    replyintent.putExtra(EXTRA_REPLY_STARTCAT, "beruflich");
                } else {
                    replyintent.putExtra(EXTRA_REPLY_STARTCAT, "privat");
                }
                //we store date and time as a long
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMAN);
                    dstart = dtf.parse(etStartDate.getText().toString() + " " + etStartTime.getText().toString());
                    replyintent.putExtra(EXTRA_REPLY_STARTDATETIME, dstart.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                replyintent.putExtra(EXTRA_REPLY_ENDLOCATION, replyEndLocation); //if no endlocation yet then it is defaultvalue "---"

                setResult(RESULT_OK, replyintent);

                finish();
            }
        });

        //CALENDARDIALOG BUTTON CLICK
        final ImageButton btDate = findViewById(R.id.BT_CalenderDlg);
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
                    date = dtf.parse(etStartDate.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewTripActivity.this, NewTripActivity.this, year, month, day);
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        //RETOURTRIP BUTTON CLICK
        btRetourTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] sData;
                sData = lastEndLocation.split(" - ");
                if (sData.length != 2) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Fehler bei einlesen Daten Retourfahrt...",
                            Toast.LENGTH_LONG).show();
                } else {
                    etStartAddress.setText(sData[0]);
                    etStartLocation.setText(sData[1]);
                    etStartKm.setText("" + lastEndKm);
                    replyEndLocation = lastStartLocation;
                }
                Toast.makeText(
                        getApplicationContext(),
                        "Ankunftdaten letzte Fahrt übernommen...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //LASTKM BUTTON CLICK
        btLastKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastEndKm > 0) {
                    etStartKm.setText(""+lastEndKm);
                }
            }
        });

        findViewById(R.id.ET_StartAddress).requestFocus();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etStartDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
