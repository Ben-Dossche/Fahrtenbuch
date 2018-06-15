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
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExportActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_EXPORTFROM = "fromdate";
    public static final String EXTRA_REPLY_EXPORTTILL = "tilldate";
    public static final String EXTRA_REPLY_EXPORTCATEGORY = "category";
    public static final String EXTRA_REPLY_EMAIL = "email";

    private EditText etFrom;
    private EditText etTill;
    private EditText etEmail;
    private Spinner spCat;
    private Button btExport;
    private ImageButton btDateStart;
    private ImageButton btDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        etFrom = findViewById(R.id.ET_ExportStartDate);
        etTill = findViewById(R.id.ET_ExportEndDate);
        etEmail = findViewById(R.id.ET_ExportEmail);
        spCat = findViewById(R.id.SP_ExportSpin);
        btExport = findViewById(R.id.BT_ExportStart);
        btDateEnd = findViewById(R.id.BT_ExportEndCalenderDlg);
        btDateStart = findViewById(R.id.BT_ExportStartCalenderDlg);

        //BY DEFAULT: FILL IN CURRENT DATE AS TILL DATE
        Date d1 = new Date();
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
        etTill.setText(df.format(d1));

        //EXPORT BUTTON CLICK
        btExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyintent = new Intent();
                Date dstart;

                //obligatory fields must have data
                if ((TextUtils.isEmpty(etFrom.getText())) ||
                        (TextUtils.isEmpty(etTill.getText())) ||
                        (TextUtils.isEmpty(etEmail.getText())) ||
                        (TextUtils.isEmpty(spCat.getSelectedItem().toString()))) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Bitte zuerst alle Felder eintragen...",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // TODO: 15.06.2018 check till date is after from date



            }
        });

        //START DATE BUTTON CLICK
        final DatePickerDialog.OnDateSetListener from_dateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
                etFrom.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };
        btDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
                    if (!TextUtils.isEmpty(etFrom.getText())) {
                        date = dtf.parse(etFrom.getText().toString());
                    } else {
                        date = new Date();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ExportActivity.this, from_dateListener, year, month, day);
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //END DATE BUTTON CLICK
        final DatePickerDialog.OnDateSetListener till_dateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
                etTill.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };
        btDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                try {
                    DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
                    if (!TextUtils.isEmpty(etTill.getText())) {
                        date = dtf.parse(etTill.getText().toString());
                    } else {
                        date = new Date();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ExportActivity.this, till_dateListener, year, month, day);
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
