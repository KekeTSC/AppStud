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
import java.util.List;

import fr.wcs.appstudtestproject.R;
import noman.googleplaces.Place;

public class RecyclerPlaceAdapter extends RecyclerView.Adapter<RecyclerPlaceAdapter.PlaceViewHolder> {

    //Classic RecyclerView Adapter
    private List<Place> mPlaceList;

    public RecyclerPlaceAdapter(List<Place> placeList) {
        mPlaceList = new ArrayList<>(placeList);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        final Place placeModel = mPlaceList.get(position);
        //TODO Glide with result URL of http request
        holder.placeName.setText(placeModel.getName());
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public void updateAdapter(List<Place> newPlaceList) { //Notify adapter when list is changed every refresh
        mPlaceList = newPlaceList;
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

