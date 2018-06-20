package swdev.wifi.at.fbapp.db;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import swdev.wifi.at.fbapp.R;

public class ChartActivity extends AppCompatActivity {

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;

    ArrayList<BarEntry> group1 ;
    ArrayList<BarEntry> group2 ;


    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarDataSet Bardataset1 ;
    BarDataSet Bardataset2 ;
    ArrayList<BarDataSet> dataSets;


    BarData BARDATA ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chart = (BarChart) findViewById(R.id.chart1);

        //X-AXIS labels with past 12 months
        ArrayList<String> labels = new ArrayList<String>();
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int month = c.get(Calendar.MONTH);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.GERMANY);
        String[] germanyMonths = dfs.getMonths();
        for (int i = month + 1; i < germanyMonths.length; i++) {
            labels.add(germanyMonths[i]);
        }
        for (int i = 0; i <= month; i++) {
            labels.add(germanyMonths[i]);
        }


        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        bargroup1.add(new BarEntry(8f, 0));
        bargroup1.add(new BarEntry(2f, 1));
        bargroup1.add(new BarEntry(5f, 2));
        bargroup1.add(new BarEntry(20f, 3));
        bargroup1.add(new BarEntry(15f, 4));
        bargroup1.add(new BarEntry(19f, 5));

        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
        bargroup2.add(new BarEntry(6f, 0));
        bargroup2.add(new BarEntry(10f, 1));
        bargroup2.add(new BarEntry(5f, 2));
        bargroup2.add(new BarEntry(25f, 3));
        bargroup2.add(new BarEntry(4f, 4));
        bargroup2.add(new BarEntry(17f, 5));

        // creating dataset for Bar Group1
        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "berufliche Fahrten");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        barDataSet1.setColors(ColorTemplate.);

        // creating dataset for Bar Group 2
        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "private Fahrten");

        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        // initialize the Bardata with argument labels and dataSets
        BarData data = new BarData(labels, dataSets);
        chart.setData(data);

        chart.animateY(3000);
    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));

        group1.add(new BarEntry(2f, 0));
        group1.add(new BarEntry(4f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(8f, 3));
        group1.add(new BarEntry(7f, 4));
        group1.add(new BarEntry(3f, 5));

        group2.add(new BarEntry(1f, 0));
        group2.add(new BarEntry(2f, 1));
        group2.add(new BarEntry(2f, 2));
        group2.add(new BarEntry(1f, 3));
        group2.add(new BarEntry(4f, 4));
        group2.add(new BarEntry(2f, 5));
    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January 2017");
        BarEntryLabels.add("February 2017");
        BarEntryLabels.add("March 2017");
        BarEntryLabels.add("April 2017");
        BarEntryLabels.add("May 2017");
        BarEntryLabels.add("June 2017");
        BarEntryLabels.add("July 2017");
        BarEntryLabels.add("August 2017");
        BarEntryLabels.add("September 2017");
        BarEntryLabels.add("October 2017");
        BarEntryLabels.add("November 2017");
        BarEntryLabels.add("December 2017");
        BarEntryLabels.add("January 2018");
        BarEntryLabels.add("February 2018");
        BarEntryLabels.add("March 2018");
        BarEntryLabels.add("April 2018");
        BarEntryLabels.add("May 2018");
        BarEntryLabels.add("June 2018");
        BarEntryLabels.add("July 2018");
        BarEntryLabels.add("August 2018");
        BarEntryLabels.add("September 2018");
        BarEntryLabels.add("October 2018");
        BarEntryLabels.add("November 2018");
        BarEntryLabels.add("December 2018");

    }
}