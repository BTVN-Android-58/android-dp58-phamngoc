package com.codewithngoc.android58day06;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;
    private OnCartChangedListener cartChangedListener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setOnCartChangedListener(OnCartChangedListener listener) {
        this.cartChangedListener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productImage.setImageResource(product.getImageResId());
        holder.ratingBar.setRating(product.getRating());

        holder.favoriteBtn.setImageResource(
            product.isFavorite() ? R.drawable.tym_red : R.drawable.ic_heart_filled
        );

        holder.favoriteBtn.setOnClickListener(v -> {
            product.setFavorite(!product.isFavorite());
            CartManager.getInstance().saveProductFavorite(product.getId(), product.isFavorite());
            // When favorited, add to cart (set quantity to 1 if not already in cart)
            if (product.isFavorite() && product.getQuantity() == 0) {
                product.setQuantity(1);
                CartManager.getInstance().saveProductQuantity(product.getId(), product.getQuantity());
            }
            notifyItemChanged(position);
            if (cartChangedListener != null) cartChangedListener.onCartChanged();
        });

        holder.decreaseBtn.setOnClickListener(v -> {
            if (product.getQuantity() > 0) {
                product.setQuantity(product.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(product.getQuantity()));
                double totalPrice = product.getQuantity() * product.getPrice();
                holder.productPrice.setText("$" + String.format("%.2f", totalPrice));
                CartManager.getInstance().saveProductQuantity(product.getId(), product.getQuantity());
                if (cartChangedListener != null) cartChangedListener.onCartChanged();
            }
        });

        holder.increaseBtn.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(product.getQuantity()));
            double totalPrice = product.getQuantity() * product.getPrice();
            holder.productPrice.setText("$" + String.format("%.2f", totalPrice));
            CartManager.getInstance().saveProductQuantity(product.getId(), product.getQuantity());
            if (cartChangedListener != null) cartChangedListener.onCartChanged();
        });

        holder.quantityText.setText(String.valueOf(product.getQuantity()));
        double initialTotal = product.getQuantity() * product.getPrice();
        holder.productPrice.setText("$" + String.format("%.2f", initialTotal));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantityText;
        RatingBar ratingBar;
        ImageButton favoriteBtn, increaseBtn, decreaseBtn;

        ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
            quantityText = itemView.findViewById(R.id.quantity);
            increaseBtn = itemView.findViewById(R.id.increase_btn);
            decreaseBtn = itemView.findViewById(R.id.decrease_btn);
        }
    }
}