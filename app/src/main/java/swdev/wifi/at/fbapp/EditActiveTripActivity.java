package swdev.wifi.at.fbapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditActiveTripActivity extends AppCompatActivity {

    public static final String EXTRA__REPLY_TRIPID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_active_trip);
    }
}
