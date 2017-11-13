package com.example.ehu.recodevoiceinbackgroud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    ToggleButton toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggle = (ToggleButton) findViewById(R.id.recordButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("onCheckedChanged", "R.id.toggleServiceButton is enabled");

                    startService(new Intent(getBaseContext(),RecordVoiceService.class));
                } else {
                    Log.d("onCheckedChanged", "R.id.toggleServiceButton is disable");
                    stopService(new Intent(getBaseContext(),RecordVoiceService.class));
                }
            }
        });
    }
}
