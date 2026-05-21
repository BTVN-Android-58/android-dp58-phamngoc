package com.codewithngoc.day07apporderfood.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codewithngoc.day07apporderfood.R;
import com.codewithngoc.day07apporderfood.adapter.FoodAdapter;
import com.codewithngoc.day07apporderfood.databinding.FragmentListBinding;
import com.codewithngoc.day07apporderfood.viewmodel.FoodViewModel;

import java.util.Arrays;
import java.util.List;

public class FragmentList extends Fragment {
    private FragmentListBinding binding;
    private FoodViewModel foodViewModel;
    private FoodAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);

        setupRecyclerView();
        setupCategoryTabs();
        setupSearch();
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new FoodAdapter(foodItem -> {
            foodViewModel.selectFood(foodItem);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentDetail())
                    .addToBackStack(FragmentDetail.class.getSimpleName())
                    .commit();
        });
        binding.recyclerFoods.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFoods.setAdapter(adapter);
    }

    private void setupCategoryTabs() {
        List<TextView> tabs = Arrays.asList(
                binding.chipBreakfast,
                binding.chipLunch,
                binding.chipDinner,
                binding.chipSnack
        );

        binding.chipBreakfast.setOnClickListener(v -> onCategorySelected("Breakfast", tabs));
        binding.chipLunch.setOnClickListener(v -> onCategorySelected("Lunch", tabs));
        binding.chipDinner.setOnClickListener(v -> onCategorySelected("Dinner", tabs));
        binding.chipSnack.setOnClickListener(v -> onCategorySelected("Snack", tabs));

        highlightSelectedTab(foodViewModel.getSelectedCategory(), tabs);
    }

    private void onCategorySelected(String category, List<TextView> tabs) {
        foodViewModel.selectCategory(category);
        highlightSelectedTab(category, tabs);
    }

    private void highlightSelectedTab(String selectedCategory, List<TextView> tabs) {
        for (TextView tab : tabs) {
            boolean isSelected = selectedCategory.equalsIgnoreCase((String) tab.getTag());
            tab.setSelected(isSelected);
        }
    }

    private void setupSearch() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                foodViewModel.updateSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void observeData() {
        foodViewModel.getVisibleFoods().observe(getViewLifecycleOwner(), foods -> {
            adapter.submitItems(foods);
            binding.tvEmpty.setVisibility(foods == null || foods.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
