package com.ww.gorrion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ww.gorrion.common.Util;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String contacto = "";
    private String direccion = "";
    private Double lat = 0.0;
    private Double lng = 0.0;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        contacto = getIntent().getStringExtra("contacto");
        setTitle(contacto);
        direccion = getIntent().getStringExtra("direccion");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings ui = mMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setCompassEnabled(true);
        /*Location loc = miPosicion();
        if (loc == null) {
            Toast.makeText(this, "POSITION NULL", Toast.LENGTH_LONG).show();
        }*/
        //mostrarPosicion(loc);
    }

    private void mostrarPosicion(Location loc) {
        LatLng wwLoc = null;
        if (loc != null) {
            wwLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(wwLoc).title("Aqui estoy").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        }

        LatLng clLoc = new LatLng(lat, lng);
        Marker markDestino = mMap.addMarker(new MarkerOptions().position(clLoc).title(contacto).snippet(direccion));
        markDestino.showInfoWindow();

        //if (wwLoc == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clLoc, 15));
        //} else {
            //LatLngBounds LIMA = new LatLngBounds(wwLoc, clLoc);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LIMA.getCenter(), 15));
        //}
        //LatLng myPos = new LatLng(loc.getLatitude(), loc.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 10));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(clLoc)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder*/
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private Location miPosicion() {
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        mMap.setMyLocationEnabled(true);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS desabilitado", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("El GPS esta Deshabilitado?")
                    .setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //onBackPressed();
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton("Continuar", null)
                    .show();
            //return null;
        }
        String provider = LocationManager.GPS_PROVIDER;
        Location myLocation = locManager.getLastKnownLocation(provider);
        if (myLocation == null) {
            Toast.makeText(this, "GPS NULL", Toast.LENGTH_LONG).show();
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);

            provider = locManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            //Log.e("miProvider0", provider);
            myLocation = locManager.getLastKnownLocation(provider);
        }
        List<String> listaProviders = locManager.getAllProviders();
        for (String listaProvider : listaProviders) {
            Log.e("miProvider1", listaProvider);
        }

        /*LocationProvider provider = locManager.getProvider(listaProviders.get(0));
        int precision = provider.getAccuracy();
        boolean obtieneAltitud = provider.supportsAltitude();
        int consumoRecursos = provider.getPowerRequirement();*/

        locManager.requestLocationUpdates(provider, 1000 * 1, 0, new LocationListener() {

            public void onLocationChanged(Location loc) {
                mostrarPosicion(loc);
            }

            public void onProviderDisabled(String provider) {
                //lblEstado.setText("Provider OFF");
            }

            public void onProviderEnabled(String provider) {
                //lblEstado.setText("Provider ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //lblEstado.setText("Provider Status: " + status);
            }
        });

        return myLocation;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onMapReady(mMap);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null) {
            //Toast.makeText(this, String.valueOf(loc.getLatitude()), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, String.valueOf(loc.getLongitude()), Toast.LENGTH_LONG).show();
            mostrarPosicion(loc);
        }else{
            Toast.makeText(this, "onConnected: NULL", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
