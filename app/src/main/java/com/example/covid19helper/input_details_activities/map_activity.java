package com.example.covid19helper.input_details_activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.covid19helper.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class map_activity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int DEFAULT_ZOOM = 15;
    private final double DELHI_LAT= 28.644800;
    private final double DELHI_LNG= 77.216721;

    public static final int PERMISSION_REQUEST_CODE = 9001;
    private static final int PLAY_SERVICES_ERROR_CODE = 9002;
    public static final int GPS_REQUEST_CODE = 9003;
    public static final String TAG = "MapDebug";
    private boolean mLocationPermissionGranted;
    private CameraUpdate cameraUpdate;

    private ImageButton mBtnLocate;
    private GoogleMap mGoogleMap;
    private EditText mSearchAddress;
    private String totalAddress;
    private TextView mIsAddressCorrect;
    private Button mYes;
    private Button mNo;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private String mName;
    private String mCity;
    private String mContact;
    private String mCategory;
    private int mPosition;
    private int category_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);

        mSearchAddress = findViewById(R.id.et_address);
        mBtnLocate = findViewById(R.id.btn_locate);
        mIsAddressCorrect = findViewById(R.id.isAddressCorrect_tv);
        mYes = findViewById(R.id.yes_btn);
        mNo = findViewById(R.id.no_btn);

        mBtnLocate.setOnClickListener(this::geoLocate);

        initGoogleMap();

        Intent intent = getIntent();
        if(intent.getStringExtra("calling_activity").equals(com.example.covid19helper.input_details_activites.details_form.class.toString())){
            totalAddress = intent.getStringExtra("total_address");
            mSearchAddress.setText(totalAddress);
            mAddress = intent.getStringExtra("address");
            mName = intent.getStringExtra("name");
            mCity = intent.getStringExtra("city");
            mContact = intent.getStringExtra("contact_no");
            mCategory = intent.getStringExtra("category");
            category_no = intent.getIntExtra("category_no",0);

            mYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(map_activity.this, com.example.covid19helper.input_details_activites.details_form.class);
                    intent.putExtra("total_address",totalAddress);
                    intent.putExtra("address",mSearchAddress.getText().toString());
                    intent.putExtra("name",mName);
                    intent.putExtra("city",mCity);
                    intent.putExtra("contact_no",mContact);
                    intent.putExtra("category",mCategory);
                    intent.putExtra("category_no",category_no);
                    intent.putExtra("latitude",mLatitude);
                    intent.putExtra("longitude",mLongitude);
                    intent.putExtra("recheck",1);
                    startActivity(intent);
                }
            });
        }

        if(intent.getStringExtra("calling_activity").equals(postActivity.class.toString())){
            totalAddress = intent.getStringExtra("total_address");
            mSearchAddress.setText(totalAddress);

            mAddress = intent.getStringExtra("address");
            mName = intent.getStringExtra("name");
            mCity = intent.getStringExtra("city");
            mContact = intent.getStringExtra("contact_no");
            mCategory = intent.getStringExtra("category");
            mPosition = intent.getIntExtra("spinner_pos",0);

            mYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(map_activity.this,postActivity.class);
                    intent.putExtra("total_address",totalAddress);
                    intent.putExtra("address",mSearchAddress.getText().toString());
                    intent.putExtra("name",mName);
                    intent.putExtra("city",mCity);
                    intent.putExtra("contact_no",mContact);
                    intent.putExtra("category",mCategory);
                    intent.putExtra("latitude",mLatitude);
                    intent.putExtra("longitude",mLongitude);
                    intent.putExtra("recheck",1);
                    intent.putExtra("spinner_pos",mPosition);
                    startActivity(intent);
                }
            });
        }

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsAddressCorrect.setText("Correct the Address on search bar Or Mark on the map");
            }
        });
    }

    private void geoLocate(View view) {
        hideSoftKeyboard(view);

        String locationName = mSearchAddress.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                gotoLocation(address.getLatitude(), address.getLongitude());

                showMarker(address.getLatitude(), address.getLongitude());

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {

        }
    }

    private void showMarker(double lat, double lng) {
        mLatitude = lat;
        mLongitude = lng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        mGoogleMap.clear();
        mGoogleMap.addMarker(markerOptions);
        mIsAddressCorrect.setText("is the Address Correct Now..");
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initGoogleMap() {

        if (isServicesOk()) {
            if (isGPSEnabled()) {
                if (checkLocationPermission()) {
                    Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map_fragment_container);

                    supportMapFragment.getMapAsync(this);
                } else {
                    requestLocationPermission();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        gotoLocation(DELHI_LAT,DELHI_LNG);
        mBtnLocate.performClick();

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Geocoder geocoder = new Geocoder(map_activity.this, Locale.getDefault());
                double lat = latLng.latitude;
                double lon = latLng.longitude;
                try {
                    List<Address> addressList = geocoder.getFromLocation(lat,lon,1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);

                        mSearchAddress.setText(address.getAddressLine(address.getMaxAddressLineIndex()));

                        showMarker(address.getLatitude(), address.getLongitude());

                        Toast.makeText(map_activity.this, address.getLocality(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void gotoLocation(double lat,double lng){

        LatLng latLng = new LatLng(lat,lng);

        cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("GPS is required for this app to work. Please enable GPS.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }

        return false;
    }

    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isServicesOk() {

        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();

        int result = googleApi.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, task ->
                    Toast.makeText(this, "Dialog is cancelled by User", Toast.LENGTH_SHORT).show());
            dialog.show();
        } else {
            Toast.makeText(this, "Play services are required by this application", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnabled) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS not enabled. Unable to show user location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
