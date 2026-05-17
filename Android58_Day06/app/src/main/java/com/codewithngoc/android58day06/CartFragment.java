package com.codewithngoc.android58day06;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView subTotalText, totalText;
    private View emptyStateContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.recycler_view_cart);
        subTotalText = view.findViewById(R.id.sub_total);
        totalText = view.findViewById(R.id.total);
        emptyStateContainer = view.findViewById(R.id.empty_state_container);

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    getActivity().findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        });

        // Get cart items from CartManager
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        // Setup RecyclerView
        cartAdapter = new CartAdapter(cartItems, this::updateTotals);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotals();
        updateCartUI();

        return view;
    }

    private void updateTotals() {
        double subTotal = 0;
        List<Product> cartItems = CartManager.getInstance().getCartItems();
        for (Product product : cartItems) {
            subTotal += product.getQuantity() * product.getPrice();
        }
        subTotalText.setText("$" + String.format("%.2f", subTotal));
        totalText.setText("$" + String.format("%.2f", subTotal));
        updateCartUI();
    }

    private void updateCartUI() {
        List<Product> cartItems = CartManager.getInstance().getCartItems();
        boolean isEmpty = true;
        for (Product p : cartItems) {
            if (p.getQuantity() > 0) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateContainer.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}