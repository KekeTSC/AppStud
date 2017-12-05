package fr.wcs.appstudtestproject.UI.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import fr.wcs.appstudtestproject.Controllers.LocationController;
import fr.wcs.appstudtestproject.Controllers.PlaceRequestController;
import fr.wcs.appstudtestproject.R;
import fr.wcs.appstudtestproject.UI.Adapters.RecyclerPlaceAdapter;

public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
Observer{

    private RecyclerPlaceAdapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PlaceRequestController mPlaceRequestController;
    private LocationController mLocationController;
    private RecyclerView mRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecycler = view.findViewById(R.id.recycler_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLocationController = LocationController.getInstance();
        mPlaceRequestController = PlaceRequestController.getInstance();
        mPlaceRequestController.addObserver(this);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerPlaceAdapter(mPlaceRequestController.getPlaces());
        mRecycler.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onRefresh() { //Send a new request when list is pulled to refresh
        mSwipeRefreshLayout.setRefreshing(true);
        Location currentLocation = mLocationController.getLocation();
        mPlaceRequestController.requestPlaces(currentLocation.getLatitude(),
                                                currentLocation.getLongitude());
    }

    @Override
    public void update(Observable observable, Object o) { //Change adapter when result is returned after the new request
        if (observable instanceof PlaceRequestController) {
            mPlaceRequestController = (PlaceRequestController) observable;
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter = new RecyclerPlaceAdapter(mPlaceRequestController.getPlaces());
            mRecycler.setAdapter(mAdapter);
            mAdapter.updateAdapter(mPlaceRequestController.getPlaces());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlaceRequestController.deleteObserver(this);
    }
}