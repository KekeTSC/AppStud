package fr.wcs.appstudtestproject.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class PlaceRequestController extends Observable implements PlacesListener{

    //Controller who deals with the request and warns of changes

    private static volatile PlaceRequestController mInstance;
    private List<Place> mPlaceList = new ArrayList<>();
    private boolean isFirst20Results;


    private PlaceRequestController() {
    }

    public static PlaceRequestController getInstance() {
        if (mInstance == null) {
            synchronized (PlaceRequestController.class) {
                if (mInstance == null) {
                    mInstance = new PlaceRequestController();
                }
            }
        }
        return mInstance;
    }

    public void requestPlaces(double latitude, double longitude){
        isFirst20Results = true;
        new NRPlaces.Builder() //Build the http request via a library
                .listener(this)
                .key("AIzaSyCUABM-ygLPx3O99rIDxBVuvigWYUPnxLA")
                .latlng(latitude, longitude)
                .rankby("distance")
                .type(PlaceType.BAR)
                .build()
                .execute();
    }

    public List<Place> getPlaces() {
        return mPlaceList;
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        if (isFirst20Results) { //Google Place API send a success every 20 results
            mPlaceList = places; //Take the first 20 results, nearests
        }
        isFirst20Results = false;
    }

    @Override
    public void onPlacesFinished() {
        //TODO Loading images via http request
        setChanged(); //Finish loading, notify all observers that data has changed
        notifyObservers();
    }
}
