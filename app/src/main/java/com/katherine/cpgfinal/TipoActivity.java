package com.katherine.cpgfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class TipoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo);





    }

    public void enviar(View view){
        Intent intent = new Intent(this,CheckBoxes.class);
        startActivity(intent);

    }



}
