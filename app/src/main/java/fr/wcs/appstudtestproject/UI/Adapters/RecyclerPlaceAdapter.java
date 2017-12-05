package fr.wcs.appstudtestproject.UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.wcs.appstudtestproject.Models.PlaceModel;
import fr.wcs.appstudtestproject.R;

class RecyclerPlaceAdapter extends RecyclerView.Adapter<RecyclerPlaceAdapter.NewsViewHolder> {
    private List<PlaceModel> mNewsList;

    public RecyclerPlaceAdapter() {
        mNewsList = new ArrayList<>();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final PlaceModel placeModel = mNewsList.get(position);
        holder.placePhoto.setImageBitmap(placeModel.getPlaceBitmap());
        holder.placeName.setText(placeModel.getPlaceName());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    void updateAdapter(List<PlaceModel> newsList) {
        mNewsList = newsList;
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView placePhoto;
        TextView placeName;

        NewsViewHolder(View view) {
            super(view);
            placePhoto = view.findViewById(R.id.placePhotoImage);
            placeName = view.findViewById(R.id.placeNameText);
        }
    }
}

