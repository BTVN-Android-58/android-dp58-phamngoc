package com.codewithngoc.android58day06;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> cartItems;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(List<Product> cartItems, OnQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.quantityChangeListener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productImage.setImageResource(product.getImageResId());
        holder.ratingBar.setRating(product.getRating());
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        holder.decreaseBtn.setOnClickListener(v -> {
            if (product.getQuantity() > 0) {
                product.setQuantity(product.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(product.getQuantity()));
                double totalPrice = product.getQuantity() * product.getPrice();
                holder.productPrice.setText("$" + String.format("%.2f", totalPrice));
                CartManager.getInstance().saveProductQuantity(product.getId(), product.getQuantity());
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged();
                }
            }
        });

        holder.increaseBtn.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(product.getQuantity()));
            double totalPrice = product.getQuantity() * product.getPrice();
            holder.productPrice.setText("$" + String.format("%.2f", totalPrice));
            CartManager.getInstance().saveProductQuantity(product.getId(), product.getQuantity());
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged();
            }
        });

        double initialTotal = product.getQuantity() * product.getPrice();
        holder.productPrice.setText("$" + String.format("%.2f", initialTotal));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantityText;
        RatingBar ratingBar;
        ImageButton increaseBtn, decreaseBtn;

        CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_product_image);
            productName = itemView.findViewById(R.id.cart_product_name);
            productPrice = itemView.findViewById(R.id.cart_product_price);
            ratingBar = itemView.findViewById(R.id.cart_rating_bar);
            quantityText = itemView.findViewById(R.id.cart_quantity);
            increaseBtn = itemView.findViewById(R.id.cart_increase_btn);
            decreaseBtn = itemView.findViewById(R.id.cart_decrease_btn);
        }
    }
}