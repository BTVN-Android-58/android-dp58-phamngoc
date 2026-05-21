package com.codewithngoc.day07apporderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithngoc.day07apporderfood.R;
import com.codewithngoc.day07apporderfood.databinding.ItemFoodBinding;
import com.codewithngoc.day07apporderfood.model.FoodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem foodItem);
    }

    private final List<FoodItem> items = new ArrayList<>();
    private final OnFoodClickListener onFoodClickListener;

    public FoodAdapter(OnFoodClickListener onFoodClickListener) {
        this.onFoodClickListener = onFoodClickListener;
    }

    public void submitItems(List<FoodItem> foods) {
        items.clear();
        if (foods != null) {
            items.addAll(foods);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FoodViewHolder(ItemFoodBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(items.get(position), onFoodClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodBinding binding;

        FoodViewHolder(ItemFoodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FoodItem foodItem, OnFoodClickListener listener) {
            Context context = binding.getRoot().getContext();
            binding.tvTitle.setText(foodItem.getTitle());
            binding.tvTime.setText(context.getString(R.string.recipe_time, foodItem.getDurationMinutes()));
            binding.tvRating.setText(String.format(Locale.getDefault(), "%.1f", foodItem.getRating()));
            binding.tvEmoji.setText(foodItem.getThumbnailEmoji());
            binding.imageBadge.setCardBackgroundColor(ContextCompat.getColor(context, foodItem.getThumbnailBackgroundColorRes()));
            binding.ivFavorite.setVisibility(foodItem.isFavorite() ? View.VISIBLE : View.GONE);
            binding.getRoot().setOnClickListener(v -> listener.onFoodClick(foodItem));
        }
    }
}
