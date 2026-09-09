package com.renzoleguia.itanes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private final List<PlaceEntity> places = new ArrayList<>();
    private final OnPlaceClickListener listener;

    public interface OnPlaceClickListener {
        void onPlaceClick(int placeId);
    }

    public PlaceAdapter(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPlaces(List<PlaceEntity> newPlaces) {
        places.clear();
        if (newPlaces != null) {
            places.addAll(newPlaces);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceEntity place = places.get(position);
        holder.bind(place, listener);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagePlace;
        private final TextView textPlaceName;
        private final TextView textPlaceDescription;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlace = itemView.findViewById(R.id.imagePlace);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            textPlaceDescription = itemView.findViewById(R.id.textPlaceDescription);
        }

        public void bind(PlaceEntity place, OnPlaceClickListener listener) {
            textPlaceName.setText(place.getName());
            textPlaceDescription.setText(place.getShortDescription());

            Glide.with(itemView.getContext())
                    .load(place.getImageUrl())
                    .placeholder(R.drawable.ic_place_placeholder)
                    .error(R.drawable.ic_place_placeholder)
                    .centerCrop()
                    .into(imagePlace);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlaceClick(place.getId());
                }
            });
        }
    }
}
