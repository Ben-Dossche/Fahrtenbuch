package swdev.wifi.at.fbapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTripActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_STARTLOCATION = "startlocation";
    private EditText etStartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        etStartLocation = findViewById(R.id.ET_StartLocation);
        final Button button = findViewById(R.id.BT_StartTrip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyintent = new Intent();
                if (TextUtils.isEmpty(etStartLocation.getText())) {
                    setResult(RESULT_CANCELED, replyintent);
                } else {
                    String loc = etStartLocation.getText().toString();
                    replyintent.putExtra(EXTRA_REPLY_STARTLOCATION,loc);
                    setResult(RESULT_OK, replyintent);
                }
                finish();
            }
        });

    }
}
