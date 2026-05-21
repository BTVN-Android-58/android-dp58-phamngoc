package com.codewithngoc.day07apporderfood.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.codewithngoc.day07apporderfood.R;
import com.codewithngoc.day07apporderfood.databinding.FragmentDetailBinding;
import com.codewithngoc.day07apporderfood.databinding.ItemIngredientBinding;
import com.codewithngoc.day07apporderfood.databinding.ViewDetailInfoCardBinding;
import com.codewithngoc.day07apporderfood.model.FoodItem;
import com.codewithngoc.day07apporderfood.model.IngredientItem;
import com.codewithngoc.day07apporderfood.viewmodel.FoodViewModel;

public class FragmentDetail extends Fragment {
    private FragmentDetailBinding binding;
    private FoodViewModel foodViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);

        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.btnFavorite.setOnClickListener(v -> foodViewModel.toggleFavorite());

        foodViewModel.getSelectedFood().observe(getViewLifecycleOwner(), this::bindFood);
    }

    private void bindFood(FoodItem foodItem) {
        if (foodItem == null) {
            return;
        }

        binding.tvHeroEmoji.setText(foodItem.getHeroEmoji());
        binding.tvTitle.setText(foodItem.getTitle());
        binding.tvDescription.setText(foodItem.getDescription());
        binding.tvIngredientsTitle.setText(getString(R.string.ingredients_title, foodItem.getIngredients().size()));
        binding.btnFavorite.setSelected(foodItem.isFavorite());

        bindInfoCard(binding.cardTime, R.drawable.ic_clock, R.string.label_cooking_time,
                getString(R.string.recipe_time, foodItem.getDurationMinutes()), true);
        bindInfoCard(binding.cardDifficulty, R.drawable.ic_chef_hat, R.string.label_difficulty,
                foodItem.getDifficulty(), true);
        bindInfoCard(binding.cardServings, R.drawable.ic_servings, R.string.label_servings,
                getString(R.string.people_count, foodItem.getServings()), false);

        int favoriteColor = ContextCompat.getColor(requireContext(),
                foodItem.isFavorite() ? R.color.accent_red : R.color.text_primary);
        binding.ivFavorite.setColorFilter(favoriteColor);

        binding.ingredientsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (IngredientItem ingredient : foodItem.getIngredients()) {
            ItemIngredientBinding ingredientBinding = ItemIngredientBinding.inflate(inflater, binding.ingredientsContainer, false);
            ingredientBinding.tvIngredientEmoji.setText(ingredient.getEmoji());
            ingredientBinding.tvIngredientName.setText(ingredient.getName());
            ingredientBinding.tvIngredientAmount.setText(ingredient.getAmount());
            ingredientBinding.ingredientBadge.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), ingredient.getBackgroundColorRes())
            );

            ingredientBinding.tagsContainer.removeAllViews();
            for (int i = 0; i < ingredient.getTags().size(); i++) {
                TextView textView = new TextView(requireContext());
                textView.setText(ingredient.getTags().get(i));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
                textView.setTextSize(12);
                textView.setPadding(dp(12), dp(6), dp(12), dp(6));
                textView.setBackgroundResource(i % 3 == 0 ? R.drawable.bg_tag_one : i % 3 == 1
                        ? R.drawable.bg_tag_two : R.drawable.bg_tag_three);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMarginStart(dp(8));
                textView.setLayoutParams(params);
                ingredientBinding.tagsContainer.addView(textView);
            }

            binding.ingredientsContainer.addView(ingredientBinding.getRoot());
        }
    }

    private int dp(int value) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }

    private void bindInfoCard(ViewDetailInfoCardBinding cardBinding, int iconRes, int labelRes, String value, boolean addEndMargin) {
        cardBinding.ivInfoIcon.setImageResource(iconRes);
        cardBinding.tvInfoLabel.setText(labelRes);
        cardBinding.tvInfoValue.setText(value);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardBinding.getRoot().getLayoutParams();
        params.setMarginEnd(addEndMargin ? dp(10) : 0);
        cardBinding.getRoot().setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
