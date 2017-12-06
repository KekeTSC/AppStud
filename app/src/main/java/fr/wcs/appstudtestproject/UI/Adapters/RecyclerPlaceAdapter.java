package fr.wcs.appstudtestproject.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import fr.wcs.appstudtestproject.R;
import noman.googleplaces.Place;

public class RecyclerPlaceAdapter extends RecyclerView.Adapter<RecyclerPlaceAdapter.PlaceViewHolder> {

    private LinkedHashMap<Place, String> mPlacesMap;

    //Classic RecyclerView Adapter
    private List<Place> mPlaceList;
    private List<String> mPlacePhotos;
    private Context mContext;

    public RecyclerPlaceAdapter(LinkedHashMap<Place, String> placeMap, Context context) {
        mPlacesMap = placeMap;
        mPlaceList = new ArrayList<>(mPlacesMap.keySet());
        mPlacePhotos = new ArrayList<>(mPlacesMap.values());
        mContext = context;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        mPlaceList = new ArrayList<>(mPlacesMap.keySet());
        mPlacePhotos = new ArrayList<>(mPlacesMap.values());
        final Place placeModel = mPlaceList.get(position);
        Glide.with(mContext)
                .load(mPlacePhotos.get(position))
                .into(holder.placePhoto);
        holder.placeName.setText(placeModel.getName());
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public void updateAdapter(LinkedHashMap<Place, String> newPlaceMap) {
        mPlacesMap = newPlaceMap;
        notifyDataSetChanged();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView placePhoto;
        TextView placeName;

        PlaceViewHolder(View view) {
            super(view);
            placePhoto = view.findViewById(R.id.placePhotoImage);
            placeName = view.findViewById(R.id.placeNameText);
        }
    }
}

