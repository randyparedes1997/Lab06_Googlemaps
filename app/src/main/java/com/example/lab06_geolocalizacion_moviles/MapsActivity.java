package com.example.lab06_geolocalizacion_moviles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng casa, japon, italia, alemania, francia;
    private float zoom;
    private static final String TAG = "Estilo de mapa";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private Location location;
    private double latitudInicio = 0, longitudInicio = 0;
    private double latitudFin = 0, longitudFin = 0;
    double distance;
    DecimalFormat kilometro = new DecimalFormat("#.00");
    private int seleccion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        mapFragment.getMapAsync(this);
        casa = new LatLng(-17.979013, -70.2335163);
        zoom = 16;

        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitudInicio = location.getLatitude();
        longitudInicio = location.getLongitude();


    }
    private void seleccionMapa(int seleccion){
        alemania = new LatLng(52.516934,13.403190);
        francia = new LatLng(48.843489,2.355331);
        japon = new LatLng(35.680513,139.769051);
        italia = new LatLng(41.902609,12.494847);


        Location locationA = new Location("Lugar A");
        location.setLatitude(latitudInicio);
        location.setLongitude(longitudInicio);
        Location locationB = new Location("Lugar B");

        switch (seleccion) {

            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.addMarker(new MarkerOptions().position(japon).title("Japon"));
                latitudFin = 35.680513;
                longitudFin = 139.769051;
                locationB.setLatitude(latitudFin);
                locationB.setLongitude(longitudFin);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Japon a "+String.valueOf(kilometro.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(japon, R.drawable.punto);
                break;

            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(new MarkerOptions().position(italia).title("Italia"));
                latitudFin = 41.902609;
                longitudFin = 12.494847;
                locationB.setLatitude(latitudFin);
                locationB.setLongitude(longitudFin);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Italia a "+String
                        .valueOf(kilometro.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(italia, R.drawable.punto);
                break;


            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.addMarker(new MarkerOptions().position(alemania).title("Alemania"));
                latitudFin = 52.516934;
                longitudFin = 13.403190;
                locationB.setLatitude(latitudFin);
                locationB.setLongitude(longitudFin);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Alemania a "+String
                        .valueOf(kilometro.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(alemania, R.drawable.punto);
                break;


            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.addMarker(new MarkerOptions().position(francia).title("Francia"));
                latitudFin = 48.843489;
                longitudFin = 2.355331;
                locationB.setLatitude(latitudFin);
                locationB.setLongitude(longitudFin);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Francia a "+String
                        .valueOf(kilometro.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(francia, R.drawable.punto);
                break;

            default:
                break;

        }


    }








    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casa,zoom));

        GroundOverlayOptions casaOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mundo))
                .position(casa, 100);
        mMap.addGroundOverlay(casaOverlay);

        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();

        Spinner spiner = (Spinner)findViewById(R.id.spnTipoMapa);

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion = position;
                seleccionMapa(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    // Captura la posicion inicial
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.app_name))
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_BLUE)));
            }
        });
    }


    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();
                poiMarker.setTag("poi");
            }
        });
    }
    private void agregarIcono(LatLng location, int imagenNueva) {
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(location, 16);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(imagenNueva))
                .anchor(0.5f,0.8f).position(location));
        mMap.animateCamera(ubicacion);

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }


}