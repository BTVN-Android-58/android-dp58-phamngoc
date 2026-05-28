package com.rsui.rs_network.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rsui.rs_network.R;
import com.rsui.rs_network.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), currencyFormat);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProduct;
        private final TextView textName;
        private final TextView textDescription;
        private final TextView textPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imgProduct);
            textName = itemView.findViewById(R.id.tvProductName);
            textDescription = itemView.findViewById(R.id.tvProductDescription);
            textPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        public void bind(Product product, NumberFormat currencyFormat) {
            textName.setText(product.getName());
            textDescription.setText(product.getDescription().isEmpty() ? "No description" : product.getDescription());
            textPrice.setText(currencyFormat.format(product.getPrice()));

            Glide.with(itemView.getContext())
                    .load(product.getProductImage())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imageProduct);
        }
    }
}
