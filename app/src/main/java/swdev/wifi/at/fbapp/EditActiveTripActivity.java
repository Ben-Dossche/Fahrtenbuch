package swdev.wifi.at.fbapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import swdev.wifi.at.fbapp.db.Trip;

public class EditActiveTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String EXTRA_REPLY_TRIPID = "id";
    public static final String EXTRA_REPLY_TRIPSTARTLOC = "startloc";
    public static final String EXTRA_REPLY_TRIPSTARTDATETIME = "startdt";
    public static final String EXTRA_REPLY_TRIPSTARTKM = "startkm";
    public static final String EXTRA_REPLY_TRIPSTARTNOTE = "startnote";
    public static final String EXTRA_REPLY_TRIPSTARTCAT = "startcat";

    public static final String EXTRA_REPLY_ENDLOCATION = "endlocation";
    public static final String EXTRA_REPLY_ENDKM = "endkm";
    public static final String EXTRA_REPLY_ENDDATETIME = "enddatetime";
    public static final String EXTRA_REPLY_ENDNOTE = "endnote";
    public static final String EXTRA_REPLY_ENDCAT = "endcat";
    public static final String EXTRA_REPLY_ENDTRIPID = "endtripid";


    private int tripId;
    TextView TV_Info1;
    TextView TV_Info2;
    TextView TV_Info3;
    private EditText etEndLocation;
    private EditText etEndDate;
    private EditText etEndTime;
    private EditText etEndKm;
    private EditText etNote;
    private EditText etEndAddress;
    private Switch swCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_active_trip);

        TV_Info1 = findViewById(R.id.TV_ActiveInfo2);
        TV_Info2 = findViewById(R.id.TV_ActiveInfo3);
        TV_Info3 = findViewById(R.id.TV_ActiveInfo4);
        etEndLocation = findViewById(R.id.ET_EndLocation);
        etEndDate = findViewById(R.id.ET_EndDate);
        etEndTime = findViewById(R.id.ET_EndTime);
        etEndKm = findViewById(R.id.ET_EndKm);
        etNote = findViewById(R.id.ET_EndNote);
        swCat = findViewById(R.id.SW_EndCategory);
        etEndAddress = findViewById(R.id.ET_EndAddress);

        tripId = getIntent().getExtras().getInt(EXTRA_REPLY_TRIPID);
        TV_Info2.setText(getIntent().getExtras().getString(EXTRA_REPLY_TRIPSTARTLOC));
        TV_Info1.setText(getIntent().getExtras().getString(EXTRA_REPLY_TRIPSTARTDATETIME));
        TV_Info2.setText(getIntent().getExtras().getString(EXTRA_REPLY_TRIPSTARTLOC));
        TV_Info3.setText(getIntent().getExtras().getInt(EXTRA_REPLY_TRIPSTARTKM) + " km");

        //FILL IN CURRENT DATE & TIME BY DEFAULT
        Date d1 = new Date();
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
        final DateFormat tf = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        etEndDate.setText(df.format(d1));
        etEndTime.setText(tf.format(d1));

        //FILL IN NOTE AND CAT VALUES
        etNote.setText(getIntent().getExtras().getString(EXTRA_REPLY_TRIPSTARTNOTE));
        swCat.setChecked(getIntent().getExtras().getInt(EXTRA_REPLY_TRIPSTARTCAT) == 1);

        //SAVE BUTTON CLICK
        final Button button = findViewById(R.id.BT_EndTrip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyintent = new Intent();
                Date dstart;

                //obligatory fields must have data
                if ((TextUtils.isEmpty(etEndLocation.getText())) ||
                        (TextUtils.isEmpty(etEndAddress.getText())) ||
                        (TextUtils.isEmpty(etEndDate.getText())) ||
                        (TextUtils.isEmpty(etEndTime.getText())) ||
                        (TextUtils.isEmpty(etEndKm.getText()))) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Bitte zuerst pflichtfelder eintragen...",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                replyintent.putExtra(EXTRA_REPLY_ENDTRIPID, tripId);
                replyintent.putExtra(EXTRA_REPLY_ENDLOCATION, etEndAddress.getText().toString() + " - " + etEndLocation.getText().toString());
                replyintent.putExtra(EXTRA_REPLY_ENDKM, etEndKm.getText().toString());
                if (!TextUtils.isEmpty(etNote.getText())) {
                    replyintent.putExtra(EXTRA_REPLY_ENDNOTE, etNote.getText().toString());
                }
                if (swCat.isChecked()) {
                    replyintent.putExtra(EXTRA_REPLY_ENDCAT, "beruflich");
                } else {
                    replyintent.putExtra(EXTRA_REPLY_ENDCAT, "privat");
                }
                //we store date and time as a long
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMAN);
                    dstart = dtf.parse(etEndDate.getText().toString() + " " + etEndTime.getText().toString());
                    replyintent.putExtra(EXTRA_REPLY_ENDDATETIME, dstart.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                setResult(RESULT_OK, replyintent);

                finish();
            }
        });


        final ImageButton btDate = findViewById(R.id.BT_EndCalenderDlg);
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
                    date = dtf.parse(etEndDate.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditActiveTripActivity.this, EditActiveTripActivity.this, year, month, day);
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        findViewById(R.id.ET_EndAddress).requestFocus();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etEndDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
