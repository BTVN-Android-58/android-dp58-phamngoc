package com.codewithngoc.android58day06;

    import android.content.SharedPreferences;
    import android.os.Bundle;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnItemSelectedListener(item -> {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    transaction.replace(R.id.fragment_container, new HomeFragment());
                } else if (itemId == R.id.nav_menu) {
                    transaction.replace(R.id.fragment_container, new CartFragment());
                } else if (itemId == R.id.nav_favorite) {
                    transaction.replace(R.id.fragment_container, new HomeFragment());
                } else if (itemId == R.id.nav_profile) {
                    transaction.replace(R.id.fragment_container, new HomeFragment());
                }

                transaction.commit();
                return true;
            });

            // Load home fragment by default
            if (savedInstanceState == null) {
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        }
    }