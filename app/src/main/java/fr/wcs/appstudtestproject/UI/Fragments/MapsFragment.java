package fr.wcs.appstudtestproject.UI.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;

import fr.wcs.appstudtestproject.Controllers.LocationController;
import fr.wcs.appstudtestproject.Controllers.PlaceRequestController;
import fr.wcs.appstudtestproject.R;
import noman.googleplaces.Place;

public class MapsFragment extends Fragment implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        Observer{

    private GoogleMap mMap;
    private PlaceRequestController mPlaceRequestController;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mPlaceRequestController = PlaceRequestController.getInstance();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
        }

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(getActivity().getString(R.string.wait_please));
        mProgressDialog.setMessage(getActivity().getString(R.string.searching_bars));
        mProgressDialog.show();
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {
                        mPlaceRequestController.addObserver(MapsFragment.this);
                        mMap = googleMap;
                        boolean isLocationPermissionsGranted = ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                        mMap.setMyLocationEnabled(isLocationPermissionsGranted);
                        LocationController locationController = LocationController.getInstance();
                        if (locationController.getLocation() != null) {
                            Location location = locationController.getLocation();
                            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 14f));
                        }
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        mMap.setOnMyLocationButtonClickListener(MapsFragment.this);
                        if (mPlaceRequestController.getPlaces() != null) {
                            placeMarkersOnMap();
                        }
                    }
                }
            });
        }
        return view;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    public void placeMarkersOnMap() {
        mMap.clear();
        for (Place place : mPlaceRequestController.getPlaces()) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getLatitude(), place.getLongitude()))
                    //TODO  Change marker icon to CircleImageView w/ black border
            );
        }
        mProgressDialog.dismiss();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof PlaceRequestController) {
            mPlaceRequestController = (PlaceRequestController) observable;
            placeMarkersOnMap();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlaceRequestController.deleteObserver(this);
    }
}
