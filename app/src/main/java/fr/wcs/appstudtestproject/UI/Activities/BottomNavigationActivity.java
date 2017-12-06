package fr.wcs.appstudtestproject.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import fr.wcs.appstudtestproject.Controllers.LocationController;
import fr.wcs.appstudtestproject.Controllers.PlaceRequestController;
import fr.wcs.appstudtestproject.R;
import fr.wcs.appstudtestproject.UI.Fragments.ListFragment;
import fr.wcs.appstudtestproject.UI.Fragments.MapsFragment;
import fr.wcs.appstudtestproject.Utils.PermissionUtils;

public class BottomNavigationActivity extends AppCompatActivity {

    private ArrayList<Fragment> mPagerFragments = new ArrayList<>();
    private Fragment mNewFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private LocationController mLocationController;
    private LocationManager mLocationManager = null;
    private LocationListener mLocationListener = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mNewFragment = mPagerFragments.get(0);
                    break;
                case R.id.navigation_dashboard:
                    mNewFragment = mPagerFragments.get(1);
                    break;
                default:
                    mNewFragment = mPagerFragments.get(0);
                    break;
            }
            fragmentTransaction.replace(R.id.content_frame, mNewFragment);
            fragmentTransaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mLocationController = LocationController.getInstance();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Defining a listener that responds to location updates
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLocationController.setLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        enableMyLocation();

        if (mLocationController.getLocation() != null) {
            Location currentLocation = mLocationController.getLocation();
            PlaceRequestController placeRequestController = PlaceRequestController.getInstance();
            placeRequestController.requestPlaces(currentLocation.getLatitude(),
                                                currentLocation.getLongitude(), this);
        }

        mPagerFragments.add(Fragment.instantiate(this, MapsFragment.class.getName()));
        mPagerFragments.add(Fragment.instantiate(this, ListFragment.class.getName()));

        BottomNavigationView navigation = findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mNewFragment = mPagerFragments.get(0);
        fragmentTransaction.replace(R.id.content_frame, mNewFragment);
        fragmentTransaction.commit();
    }

    private void enableMyLocation() { //Check permissions every time application is launched
        //Request permissions if needed
        if (ContextCompat.checkSelfPermission(BottomNavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(BottomNavigationActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        //Get last location if permission is granted
        String provider = mLocationManager.getBestProvider(new Criteria(), false);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            mLocationController.setLocation(location);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            finish();
            startActivity(getIntent());
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}

