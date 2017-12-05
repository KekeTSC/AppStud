package fr.wcs.appstudtestproject.Models;


import android.graphics.Bitmap;

public class PlaceModel {

    private Bitmap placeBitmap;
    private String placeName;

    public PlaceModel() {
    }

    public PlaceModel(Bitmap placeBitmap, String placeName) {
        this.placeBitmap = placeBitmap;
        this.placeName = placeName;
    }

    public Bitmap getPlaceBitmap() {
        return placeBitmap;
    }

    public void setPlaceBitmap(Bitmap placeBitmap) {
        this.placeBitmap = placeBitmap;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
