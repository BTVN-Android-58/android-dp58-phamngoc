package com.codewithngoc.day05pinterestscreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codewithngoc.day05pinterestscreen.R;
import com.codewithngoc.day05pinterestscreen.model.PinModel;

import java.util.List;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinViewHolder> {
    private List<PinModel> pinList;

    public PinAdapter(List<PinModel> pinList) {
        this.pinList = pinList;
    }

    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pin, parent, false);
        return new PinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PinViewHolder holder, int position) {
        PinModel pin = pinList.get(position);

        // Set dynamic height for staggered effect
        ViewGroup.LayoutParams imageParams = holder.pinImage.getLayoutParams();
        imageParams.height = pin.getImageHeight();
        holder.pinImage.setLayoutParams(imageParams);

        // Load pin image
        Glide.with(holder.itemView)
                .load(pin.getImageResId())
                .centerCrop()
                .into(holder.pinImage);
    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }

    static class PinViewHolder extends RecyclerView.ViewHolder {
        ImageView pinImage;

        PinViewHolder(View itemView) {
            super(itemView);
            pinImage = itemView.findViewById(R.id.ivPinImage);
        }
    }
}