package com.katherine.cpgfinal;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RutaIntentService extends IntentService {

    private Parcelable miReciver;
    private LatLng origen;
    private String url;
    private JSONObject miJson;
    private ResultReceiver mReceiver;
    private ArrayList<String> guia = new ArrayList<>();
    private LocationManager miLocationManager;
    private LatLng virrey;
    private LatLng titan;
    private LatLng carulla;
    private LatLng corferias;
    private int choice;
    private String origin;
    private String destination;
    private String language;

    public RutaIntentService() {
        super("RutaIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            mReceiver = intent.getParcelableExtra("receiver");
            choice = intent.getIntExtra("choice",0);

            miLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }


            Location ubicacion = miLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            origen = new LatLng(ubicacion.getLatitude(),ubicacion.getLongitude());

            virrey = new LatLng(4.673154, -74.054201);
            titan = new LatLng(4.6948656,-74.0872929);
            carulla = new LatLng(4.6696011,-74.0579961);
            corferias = new LatLng(4.6305533,-74.0942267);


            final String URL_BASE = "https://maps.googleapis.com/maps/api/directions/json?";
            final String ORIGIN = "origin";
            final String DESTINATION = "destination";
            final String KEY = "key";
            final String LANGUAGE = "language";

            if(choice == 1) {
                origin = String.valueOf(origen.latitude) + "," + String.valueOf(origen.longitude);
                destination = String.valueOf(virrey.latitude) + "," + String.valueOf(virrey.longitude);
                language = "es";
            }else if (choice == 2){
                origin = String.valueOf(origen.latitude) + "," + String.valueOf(origen.longitude);
                destination = String.valueOf(titan.latitude) + "," + String.valueOf(titan.longitude);
                language = "es";
            }else if (choice == 3){
                origin = String.valueOf(origen.latitude) + "," + String.valueOf(origen.longitude);
                destination = String.valueOf(carulla.latitude) + "," + String.valueOf(carulla.longitude);
                language = "es";
            }else if (choice == 4){
                origin = String.valueOf(origen.latitude) + "," + String.valueOf(origen.longitude);
                destination = String.valueOf(corferias.latitude) + "," + String.valueOf(corferias.longitude);
                language = "es";
            }
            Uri.Builder builder = new Uri.Builder();

            builder.scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("directions")
                    .appendPath("json")
                    .appendQueryParameter(ORIGIN, origin)
                    .appendQueryParameter(DESTINATION, destination)
                    .appendQueryParameter(LANGUAGE,language)
                    .appendQueryParameter(KEY, "AIzaSyCLMiBZXLDO2WCQoyi66Q2uxgRKNrRalmg");

            url = builder.build().toString();

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            StringBuilder response = new StringBuilder();

            if (networkInfo != null && networkInfo.isConnected()) {

                try {
                    URL Url = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();

                    String jsonOutput = response.toString();

                    miJson = new JSONObject(jsonOutput);


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONArray rutasArray;
            JSONArray legsArray;
            JSONObject ruta;
            JSONObject leg;
            JSONArray stepsArray;
            List<String> steps = new ArrayList<>();
            List<List<LatLng>> polylines = new ArrayList<>();
            List<LatLng> listaPoints = new ArrayList<>();
            List<LatLng> points = new ArrayList<>();

            if (miJson != null) {

                try {
                    rutasArray = miJson.getJSONArray("routes");
                    ruta = rutasArray.getJSONObject(0);
                    legsArray = ruta.getJSONArray("legs");

                    for (int i = 0; i < legsArray.length(); i++) {

                        leg = legsArray.getJSONObject(i);
                        stepsArray = leg.getJSONArray("steps");

                        for (int j = 0; j < stepsArray.length(); j++) {
                            JSONObject step = stepsArray.getJSONObject(j);
                            guia.add(step.getString("html_instructions"));
                            listaPoints = PolyUtil.decode(step.getJSONObject("polyline").getString("points"));
                            points.addAll(listaPoints);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("guia", guia);
            bundle.putParcelableArrayList("points", (ArrayList<? extends Parcelable>) points);
            mReceiver.send(0, bundle);


        }


    }

}
