package com.mindflakes.andredmenu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AndRedMenuActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textview = (TextView) findViewById(R.id.TextView01);
        textview.setText("simple placeholder");
    }
}