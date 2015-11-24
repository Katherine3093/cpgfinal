package com.katherine.cpgfinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private RutaResultReceiver rutaResultReceiver;
    private GoogleMap miMap;
    private LatLng corferias;
    private LatLng virrey;
    private LatLng carulla;
    private LatLng titan;
    private int choice;
    private LocationManager miLocationManager;
    private LatLng origen;
    private ArrayList<String> guia;
    private ArrayList<LatLng> points;
    private Button virreyButton;
    private Button titanButton;
    private Button carullaButton;
    private Button corferiasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rutaResultReceiver = new RutaResultReceiver(new Handler());

        virrey = new LatLng(4.673154, -74.054201);
        titan = new LatLng(4.6948656,-74.0872929);
        carulla = new LatLng(4.6696011,-74.0579961);
        corferias = new LatLng(4.6305533,-74.0942267);

        miLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }


        virreyButton = (Button)findViewById(R.id.virrey);
        titanButton = (Button)findViewById(R.id.titan);
        carullaButton = (Button)findViewById(R.id.carulla);
        corferiasButton = (Button)findViewById(R.id.corferias);


        Location ubicacion = miLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        origen = new LatLng(ubicacion.getLatitude(),ubicacion.getLongitude());

    }

    public void virrey(View view){
        choice = 1;
        Intent intent = new Intent(this, RutaIntentService.class);
        intent.putExtra("choice",1 );
        intent.putExtra("receiver", rutaResultReceiver);
        startService(intent);

        virreyButton.setVisibility(View.GONE);
        titanButton.setVisibility(View.GONE);
        carullaButton.setVisibility(View.GONE);
        corferiasButton.setVisibility(View.GONE);
    }
    public void titan(View view){
        choice = 2;
        Intent intent = new Intent(this, RutaIntentService.class);
        intent.putExtra("choice",2 );
        intent.putExtra("receiver", rutaResultReceiver);
        startService(intent);
        virreyButton.setVisibility(View.GONE);
        titanButton.setVisibility(View.GONE);
        carullaButton.setVisibility(View.GONE);
        corferiasButton.setVisibility(View.GONE);
    }
    public void carulla(View view){
        choice = 3;
        Intent intent = new Intent(this, RutaIntentService.class);
        intent.putExtra("choice",3 );
        intent.putExtra("receiver", rutaResultReceiver);
        startService(intent);
        virreyButton.setVisibility(View.GONE);
        titanButton.setVisibility(View.GONE);
        carullaButton.setVisibility(View.GONE);
        corferiasButton.setVisibility(View.GONE);
    }
    public void corferias(View view){
        Intent intent = new Intent(this, RutaIntentService.class);
        choice = 4;
        intent.putExtra("choice",4 );
        intent.putExtra("receiver", rutaResultReceiver);
        startService(intent);
        virreyButton.setVisibility(View.GONE);
        titanButton.setVisibility(View.GONE);
        carullaButton.setVisibility(View.GONE);
        corferiasButton.setVisibility(View.GONE);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        miMap = googleMap;
        miMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origen.latitude, origen.longitude), 13));
        miMap.addMarker(new MarkerOptions().position(new LatLng(origen.latitude, origen.longitude)).title("A"));


    }

    private class RutaResultReceiver extends ResultReceiver {
        public RutaResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            guia = resultData.getStringArrayList("guia");
            points = resultData.getParcelableArrayList("points");

            Polyline ruta = miMap.addPolyline(new PolylineOptions().addAll(points));
            miMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origen.latitude, origen.longitude), 13));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(int k = 0;k<(ruta.getPoints().size())-1;k++){
                builder.include(ruta.getPoints().get(k));
            }


            if(choice == 1) {
                miMap.addMarker(new MarkerOptions().position(new LatLng(virrey.latitude, virrey.longitude)).title("Virrey"));
                miMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
               }else if(choice == 2){
                miMap.addMarker(new MarkerOptions().position(new LatLng(titan.latitude, titan.longitude)).title("Titan PLaza"));
                miMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            }else if(choice == 3){
                miMap.addMarker(new MarkerOptions().position(new LatLng(carulla.latitude, carulla.longitude)).title("Carulla 85"));
                miMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            }else if(choice == 4){
                miMap.addMarker(new MarkerOptions().position(new LatLng(corferias.latitude, corferias.longitude)).title("Corferias"));
                miMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            }
        }
    }


}
