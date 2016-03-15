package edu.dartmouth.nishacs.cs65project.Controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.dartmouth.nishacs.cs65project.R;
/*
* Created by Nisha
* This activity displays the map and captures the location information as choosen by the user
* Inspired by the I_AM_HERE example code of CS56/165 Dartmouth.
* */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    public Marker mCurrentLocation;
    boolean markerClicked;
    private Button mSave, mCancel;
    private TextView mTextProgress;
    public static LatLng mPosition;
    public static Integer radius = 10;
    private SeekBar mBar; // declare seekbar object variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_maps_toolbar);
        setSupportActionBar(myToolbar);

        setUpMapIfNeeded();

        mBar = (SeekBar) findViewById(R.id.location_seekBar); // make seekbar object
        mTextProgress = (TextView) findViewById(R.id.seekBar_progress);


        mTextProgress.setText("" + mBar.getProgress());
        mBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTextProgress.setText("" + progress);
            }
        });

        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
        Log.d("Network provider",provider);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        final Location l = locationManager.getLastKnownLocation(provider);
        final LatLng currentPosition = new LatLng(l.getLatitude(),
                l.getLongitude());

        LatLng latlng = fromLocationToLatLng(l);


             // Zoom in
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                17));

        updateWithNewLocation(l);

        locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng arg0) {
                if (mCurrentLocation != null) {
                    mCurrentLocation.remove();
                }
                mCurrentLocation = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(arg0.latitude,
                                        arg0.longitude))
                        .draggable(true).visible(true));
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                if (mCurrentLocation != null) {
                    mCurrentLocation.remove();
                }
                mCurrentLocation = mMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .snippet(
                                "Lat:" + l.getLatitude() + "Lng:"
                                        + l.getLongitude())
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("ME"));
                Log.e("lat", "" + point);

            }
        });

        /*-----------Setting button listeners---------------------------------*/
        //setting the location button and its listener
        mSave = (Button)findViewById(R.id.btnMapSave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the marker position and saving it
                if (mCurrentLocation != null) {
                    mPosition = mCurrentLocation.getPosition(); //
                    radius = mBar.getProgress();
                }
                finish();
        }
        });


        mCancel = (Button)findViewById(R.id.btnMapCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                // Configure the map display options

            }
        }
    }

    private void setUpMap() {
        //  mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //checking permisisons
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }


    public void onMapClick(LatLng point) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));

        markerClicked = false;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hanover = new LatLng(43.6842373, -72.2956465);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hanover));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

        mMap.animateCamera(zoom);

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {

        String latLongString = "No location found";
        String addressString = "No address found";

        if (location != null) {
            // Update the map location.

            LatLng latlng = fromLocationToLatLng(location);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                    17));

            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder gc = new Geocoder(this, Locale.getDefault());

            if (!Geocoder.isPresent())
                addressString = "No geocoder available";
            else {
                try {
                    List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                            sb.append(address.getAddressLine(i)).append("\n");

                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                    }
                    addressString = sb.toString();
                } catch (IOException e) {
                    Log.d("MapsActivity", "IO Exception", e);
                }
            }
        }

    }

}
