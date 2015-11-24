package com.katherine.cpgfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CheckBoxes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_boxes);
    }

    public void siguiente(View view){
        Intent intent = new Intent(this,Mapa.class);
        startActivity(intent);

    }

}
