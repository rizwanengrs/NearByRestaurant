package com.example.abc.resturantdemo.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abc.resturantdemo.R;
import com.example.abc.resturantdemo.model.PlaceItem;

import java.util.List;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private final List<PlaceItem> mValues;

    public RestaurantAdapter(List<PlaceItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placeitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.placeName.setText(holder.mItem.placeName);
        holder.placeAddress.setText(holder.mItem.placeVicinity);
        holder.placeDistance.setText(holder.mItem.placeDistance);
        holder.placeType.setText(holder.mItem.placeType);
        holder.place_rating.setText(holder.mItem.rating);
        holder.contact_detail.setText(holder.mItem.contact_detail);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView placeName;
        public final TextView placeAddress;
        public final TextView placeType;
        public final TextView placeDistance;
        public final TextView place_rating;
        public final TextView contact_detail;
        public PlaceItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            placeName = (TextView) view.findViewById(R.id.placeName);
            placeAddress = (TextView) view.findViewById(R.id.placeAddress);
            placeType = (TextView) view.findViewById(R.id.placeType);
            placeDistance = (TextView) view.findViewById(R.id.placeDistance);
            place_rating = (TextView) view.findViewById(R.id.place_rating);
            contact_detail = (TextView) view.findViewById(R.id.contact_detail);
        }

    }
}
