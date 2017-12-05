package fr.wcs.appstudtestproject.Controllers;

import android.location.Location;


public class LocationController {

    //Location singleton to get one unique instance of the object location who's updated every change

    private static volatile LocationController mInstance;

    private Location mLocation;

    private LocationController() {
    }

    public static LocationController getInstance() {
        if (mInstance == null) {
            synchronized (LocationController.class) {
                if (mInstance == null) {
                    mInstance = new LocationController();
                }
            }
        }
        return mInstance;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
