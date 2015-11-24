package com.katherine.cpgfinal;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class MainActivity extends AppCompatActivity {


    private TextView bikeTextView;
    private TextView alarquinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bikeTextView = (TextView) findViewById(R.id.fuente);
        alarquinTextView = (TextView) findViewById(R.id.alarquin);


    }

    public void siguiente(View view) {
        Intent intent = new Intent(this, TipoActivity.class);
        startActivity(intent);
    }

}






