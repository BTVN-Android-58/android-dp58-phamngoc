package com.codewithngoc.day05pinterestscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentTransaction;

import com.codewithngoc.day05pinterestscreen.fragment.ForYouFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TextView> tabButtons;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make status bar content dark (black icons)
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);

        setContentView(R.layout.activity_main);

        // Tab TextViews
        TextView tabForYou = findViewById(R.id.tab_for_you);
        TextView tabPopular = findViewById(R.id.tab_popular);
        TextView tabDesign = findViewById(R.id.tab_design);
        TextView tabTech = findViewById(R.id.tab_tech);
        TextView tabArt = findViewById(R.id.tab_art);
        TextView tabFood = findViewById(R.id.tab_food);

        tabButtons = Arrays.asList(tabForYou, tabPopular, tabDesign, tabTech, tabArt, tabFood);

        // Set click listeners for each tab
        for (int i = 0; i < tabButtons.size(); i++) {
            final int index = i;
            tabButtons.get(i).setOnClickListener(v -> {
                setTabActive(index);
                loadFragment(index);
            });
        }

        // Bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) return true;
            if (itemId == R.id.nav_search) return true;
            if (itemId == R.id.nav_create) return true;
            if (itemId == R.id.nav_notification) return true;
            if (itemId == R.id.nav_profile) return true;
            return false;
        });

        // Default: load For You tab and select home nav
        setTabActive(0);
        loadFragment(0);
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void loadFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ForYouFragment());
        transaction.commit();
    }

    private void setTabActive(int activePosition) {
        for (int i = 0; i < tabButtons.size(); i++) {
            TextView btn = tabButtons.get(i);
            if (i == activePosition) {
                btn.setBackgroundResource(R.drawable.chip_selected_bg);
                btn.setTextColor(getResources().getColor(R.color.chip_text_selected));
            } else {
                btn.setBackgroundResource(R.drawable.chip_unselected_bg);
                btn.setTextColor(getResources().getColor(R.color.chip_text_unselected));
            }
        }
    }
}