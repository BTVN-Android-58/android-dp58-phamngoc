package com.codewithngoc.day08apptakenote;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithngoc.day08apptakenote.data.NoteEntity;
import com.codewithngoc.day08apptakenote.databinding.ItemNoteBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteActionListener {
        void onNoteClick(NoteEntity note);

        void onMoreClick(NoteEntity note, View anchor);
    }

    private static final int[] AVATAR_COLORS = {
            Color.parseColor("#FFE7EE"),
            Color.parseColor("#E8EEFF"),
            Color.parseColor("#FFF2E7"),
            Color.parseColor("#E8FAF1"),
            Color.parseColor("#F4E9FF")
    };

    private final List<NoteEntity> notes = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
    private final OnNoteActionListener listener;

    public NoteAdapter(OnNoteActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<NoteEntity> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(notes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final ItemNoteBinding binding;

        public NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NoteEntity note, int position) {
            binding.tvAvatar.setText(note.getTitle().substring(0, 1).toUpperCase(Locale.getDefault()));
            GradientDrawable background = (GradientDrawable) binding.tvAvatar.getBackground().mutate();
            background.setColor(AVATAR_COLORS[position % AVATAR_COLORS.length]);
            binding.tvTitle.setText(note.getTitle());
            binding.tvContent.setText(note.getContent());
            binding.tvMeta.setText(dateFormat.format(note.getUpdatedAt()));
            binding.tvTag.setText(note.getTag().isEmpty() ? "General" : note.getTag());
            binding.tvPinned.setVisibility(note.isPinned() ? View.VISIBLE : View.GONE);

            binding.getRoot().setOnClickListener(v -> listener.onNoteClick(note));
            binding.btnMore.setOnClickListener(v -> listener.onMoreClick(note, v));
        }
    }
}
