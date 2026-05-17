package com.codewithngoc.android58day06;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView cartBadge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        EditText searchBox = view.findViewById(R.id.search_box);
        ImageButton btnCart = view.findViewById(R.id.btn_cart);

        // Navigate to CartFragment when cart icon is clicked
        btnCart.setOnClickListener(v -> {
            if (getActivity() != null) {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    getActivity().findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.nav_menu);
            }
        });

        // Initialize CartManager with context
        CartManager.getInstance().init(getContext());

        // Initialize product list
        productList = getSampleProducts();

        // Store products in CartManager (will load saved data from SharedPreferences)
        CartManager.getInstance().setAllProducts(productList);

        // Setup RecyclerView
        productAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(productAdapter);

        // Setup cart badge
        cartBadge = view.findViewById(R.id.cart_badge);
        updateCartBadge();

        // Update badge when cart changes
        productAdapter.setOnCartChangedListener(this::updateCartBadge);

        return view;
    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Mixed Black Coffee", 12.00, R.drawable.hot_chocolate_1, 4.5f));
        products.add(new Product(2, "Mixed Black Coffee", 22.00, R.drawable.ice_coffee, 4.5f));
        products.add(new Product(3, "Mixed Black Coffee", 40.00, R.drawable.ice_coffee_1, 4.5f));
        products.add(new Product(4, "Hot Chocolate", 16.00, R.drawable.hot_chocolate_1, 4.5f));
        products.add(new Product(5, "Caramel Frappucino", 35.00, R.drawable.caramel_frappucino, 4.7f));
        products.add(new Product(6, "Caramel Frappucino", 45.00, R.drawable.caramel_frappucino1, 4.7f));
        products.add(new Product(7, "Espresso", 50.00, R.drawable.espresso, 4.3f));
        products.add(new Product(8, "Espresso", 30.00, R.drawable.espresso1, 4.3f));


        return products;
    }

    private void updateCartBadge() {
        int totalItems = 0;
        for (Product p : productList) {
            totalItems += p.getQuantity();
        }
        if (totalItems > 0) {
            cartBadge.setVisibility(View.VISIBLE);
            cartBadge.setText(String.valueOf(totalItems));
        } else {
            cartBadge.setVisibility(View.GONE);
        }
    }
}
