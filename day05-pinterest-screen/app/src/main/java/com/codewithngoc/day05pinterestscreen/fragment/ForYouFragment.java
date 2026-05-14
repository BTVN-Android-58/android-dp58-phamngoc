package com.codewithngoc.day05pinterestscreen.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codewithngoc.day05pinterestscreen.R;
import com.codewithngoc.day05pinterestscreen.adapter.PinAdapter;
import com.codewithngoc.day05pinterestscreen.model.PinModel;

import java.util.ArrayList;
import java.util.List;

public class ForYouFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private PinAdapter adapter;
    private List<PinModel> pinList;

    public ForYouFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_you, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        // Set up staggered grid layout with 2 columns
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(manager);

        // Generate mock data
        pinList = generateMockData();
        adapter = new PinAdapter(pinList);
        recyclerView.setAdapter(adapter);

        // Swipe to refresh
        swipeRefresh.setColorSchemeResources(R.color.pinterest_pink);
        swipeRefresh.setOnRefreshListener(() -> {
            // Simulate refresh
            swipeRefresh.postDelayed(() -> {
                pinList.clear();
                pinList.addAll(generateMockData());
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }, 1500);
        });
    }

    private List<PinModel> generateMockData() {
        List<PinModel> pins = new ArrayList<>();
        
        // Pinterest-style mock data with varying heights
        int[] heights = {350, 250, 300, 200, 320, 280, 360, 220, 340, 260, 300, 240};
        
        String[] titles = {
            "Beautiful sunset over the ocean",
            "Modern minimalist living room design",
            "Homemade pasta with fresh ingredients",
            "Japanese cherry blossom garden",
            "Abstract art painting technique",
            "Cozy coffee shop corner",
            "Travel photography tips for beginners",
            "DIY home decoration ideas",
            "Healthy breakfast bowl recipe",
            "Mountain hiking adventure guide",
            "Interior design inspiration 2024",
            "Street photography in Tokyo"
        };
        
        String[] authors = {
            "Emma Photography",
            "Design Studio",
            "Chef Recipes",
            "Nature Lover",
            "Art Gallery",
            "Coffee Daily",
            "Travel Blog",
            "Home DIY",
            "Healthy Life",
            "Adventure Pro",
            "Interior Style",
            "Street Snap"
        };

        // Use placeholder colors as images since we don't have real images
        int[] imageResources = {
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background
        };

        for (int i = 0; i < 12; i++) {
            pins.add(new PinModel(
                    imageResources[i % imageResources.length],
                    titles[i % titles.length],
                    authors[i % authors.length],
                    R.drawable.ic_launcher_background, // avatar placeholder
                    dpToPx(heights[i % heights.length])
            ));
        }
        return pins;
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}