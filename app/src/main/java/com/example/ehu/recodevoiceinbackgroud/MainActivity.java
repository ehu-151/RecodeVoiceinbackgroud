package com.example.ehu.recodevoiceinbackgroud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.recordButton) {
            Log.i("onCheckedChanged", "clicked R.id.toggleButton1");
        } else if (buttonView.getId() == R.id.recordButton) {
            Log.i("onCheckedChanged", "clicked R.id.toggleButton2");
        }
    }
}
