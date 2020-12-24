package com.example.s1;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String username;
    private String token;
    private TextView username_textView;
    private GoogleMap Map;
    private LocationManager locationManager;
    private String url = "http://cars.areas.su/cars";
    private HttpURLConnection connection;
    private JSONArray jsonArray;
    private AppBarConfiguration AppBarConfig;
    private ArrayList<Trip> trips = new ArrayList();
    private ListView tripsList;
    private TripAdapter tripAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceTrip) {
        super.onCreate(savedInstanceTrip);
        setContentView(R.layout.activity_map);

        /* tripsList = findViewById(R.id.listView);
        trips.add(new Trip ("Kia Rio", "15 min", "15$"));
        trips.add(new Trip ("Kia Rio", "10 min", "10$"));
        trips.add(new Trip ("Kia Rio", "60 min", "50$"));
        tripAdapter = new TripAdapter(this, R.layout.list_item, trips);
        tripsList.setAdapter((ListAdapter) tripAdapter); */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Bundle arguments = getIntent().getExtras();
        username = arguments.getString("username");
        token = arguments.getString("token");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine = "";
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }

                    jsonArray = new JSONArray(response.toString());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        AppBarConfig = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, AppBarConfig);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;
        Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng myPosition = null;
        try {
            myPosition = new LatLng(Double.parseDouble(jsonArray.getJSONObject(1).get("lat").toString()) - 0.015, Double.parseDouble(jsonArray.getJSONObject(1).get("lon").toString()) - 0.015);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My position"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(13)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        Map.animateCamera(cameraUpdate);

        updateCars(Map);
    }


    public void updateCars(GoogleMap googleMap) {
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                int id = Integer.parseInt((String) jsonArray.getJSONObject(i).get("id"));
                String model = String.valueOf(jsonArray.getJSONObject(i).get("model"));
                double lat = Double.parseDouble((String) jsonArray.getJSONObject(i).get("lat"));
                double lon = Double.parseDouble((String) jsonArray.getJSONObject(i).get("lon"));
                Map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(model).snippet(String.valueOf(id)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, AppBarConfig)
                || super.onSupportNavigateUp();

    }

}