package com.rsui.rs_network.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsui.rs_network.R;
import com.rsui.rs_network.model.User;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface onItemClickListener {
        void onItemClick(User user);
    }


    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }

        public void bind(User user, onItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imgAvatar);

            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            });
        }
    }
}
