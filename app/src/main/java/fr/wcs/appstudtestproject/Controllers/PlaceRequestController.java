package fr.wcs.appstudtestproject.Controllers;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import fr.wcs.appstudtestproject.R;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static com.android.volley.VolleyLog.TAG;

public class PlaceRequestController extends Observable {
    private static PlaceRequestController mInstance;

    //Controller who deals with the request and warns of changes

    private LinkedHashMap<Place, String> mPlaceMap = new LinkedHashMap<>();
    private boolean isFirst20Results;
    private int count;
    private int responseCount;
    private LinkedHashMap<Integer, Place> mPlaceMapId = new LinkedHashMap<>();
    private String photoUrl;


    private PlaceRequestController() {
    }

    public static PlaceRequestController getInstance() {
        if (mInstance == null) {
            mInstance = new PlaceRequestController();
        }
        return mInstance;
    }

    public void requestPlaces(double latitude, double longitude, final Activity activity) {
        mPlaceMap.clear();
        mPlaceMapId.clear();
        isFirst20Results = true;
        new NRPlaces.Builder() //Build the http request via a library
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {

                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(final List<Place> places) {
                        if (isFirst20Results) {
                            count = 0;
                            for (final Place place : places) {
                                mPlaceMapId.put(count, place);
                                count++;
                            }
                        }
                        isFirst20Results = false;
                    }

                    @Override
                    public void onPlacesFinished () {
                        loadPhotos(activity);
                    }
                })
                .key("AIzaSyCUABM-ygLPx3O99rIDxBVuvigWYUPnxLA")
                .latlng(latitude, longitude)
                .rankby("distance")
                .type(PlaceType.BAR)
                .build()
                .execute();
    }

    public LinkedHashMap<Place, String> getPlaceMap() {
        return mPlaceMap;
    }



    public void loadPhotos(final Activity activity){
        responseCount = 0;
        for (Place place : mPlaceMapId.values()) {
            RequestQueue queue = Volley.newRequestQueue(activity);
            String url ="https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                    place.getPlaceId() +
                    "&key=AIzaSyCUABM-ygLPx3O99rIDxBVuvigWYUPnxLA";  //Http request Place details
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                final String photoRef = jsonObject.
                                        getJSONObject("result").
                                        getJSONArray("photos").
                                        getJSONObject(0).
                                        getString("photo_reference");
                                photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                                        photoRef +
                                        "&key=AIzaSyCUABM-ygLPx3O99rIDxBVuvigWYUPnxLA"; //photo Url for the glide
                                mPlaceMap.put(mPlaceMapId.get(responseCount), photoUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                photoUrl = "https://www.stagephotoparis.fr/wp-content/uploads/2016/02/stage-placeholder.jpg";
                                mPlaceMap.put(mPlaceMapId.get(responseCount), photoUrl);
                            }

                            responseCount++;
                            if (mPlaceMapId.size() == responseCount) { //Verify that the first map equals counter to notify
                                setChanged(); //Finish loading, notify all observers that data has changed
                                notifyObservers();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
            }
        }
    }

