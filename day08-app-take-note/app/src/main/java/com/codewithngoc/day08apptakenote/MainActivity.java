package com.codewithngoc.day08apptakenote;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codewithngoc.day08apptakenote.data.NoteEntity;
import com.codewithngoc.day08apptakenote.data.NoteRepository;
import com.codewithngoc.day08apptakenote.databinding.ActivityMainBinding;
import com.codewithngoc.day08apptakenote.databinding.DialogNoteBinding;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteActionListener {

    private ActivityMainBinding binding;
    private NoteAdapter noteAdapter;
    private NoteRepository repository;
    private String selectedTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new NoteRepository(this);
        noteAdapter = new NoteAdapter(this);

        setupRecyclerView();
        setupListeners();

        repository.seedIfEmpty(this::refreshData);
    }

    private void setupRecyclerView() {
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotes.setAdapter(noteAdapter);
    }

    private void setupListeners() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadNotes();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        View.OnClickListener addClickListener = v -> showNoteDialog(null);
        binding.fabAddNote.setOnClickListener(addClickListener);
        binding.btnQuickAdd.setOnClickListener(addClickListener);

        binding.btnClearFilter.setOnClickListener(v -> {
            selectedTag = "";
            binding.edtSearch.setText("");
            loadTags();
            loadNotes();
            showMessage(getString(R.string.filters_reset));
        });
    }

    private void refreshData() {
        loadTags();
        loadNotes();
    }

    private void loadNotes() {
        String query = binding.edtSearch.getText() == null
                ? ""
                : binding.edtSearch.getText().toString().trim();

        repository.getNotes(query, selectedTag, notes -> {
            noteAdapter.submitList(notes);
            binding.tvEmpty.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
            binding.rvNotes.setVisibility(notes.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    private void loadTags() {
        repository.getTags(tags -> renderTagChips(tags));
    }

    private void renderTagChips(List<String> tags) {
        if (!selectedTag.isEmpty() && !tags.contains(selectedTag)) {
            selectedTag = "";
        }

        binding.chipGroupTags.removeAllViews();
        binding.chipGroupTags.addView(createTagChip(getString(R.string.all_tags), ""));

        for (String tag : tags) {
            binding.chipGroupTags.addView(createTagChip(tag, tag));
        }
    }

    private Chip createTagChip(String label, String tagValue) {
        Chip chip = new Chip(this);
        chip.setText(label);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setChipMinHeight(getResources().getDisplayMetrics().density * 42);
        chip.setChipCornerRadius(getResources().getDisplayMetrics().density * 14);
        chip.setChecked(tagValue.equals(selectedTag));
        chip.setOnClickListener(v -> {
            selectedTag = tagValue;
            loadNotes();
        });
        return chip;
    }

    private void showNoteDialog(NoteEntity note) {
        DialogNoteBinding dialogBinding = DialogNoteBinding.inflate(getLayoutInflater());
        boolean isEditing = note != null;

        if (isEditing) {
            dialogBinding.edtTitle.setText(note.getTitle());
            dialogBinding.edtTag.setText(note.getTag());
            dialogBinding.edtContent.setText(note.getContent());
            dialogBinding.switchPinned.setChecked(note.isPinned());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEditing ? R.string.edit_note : R.string.create_note)
                .setView(dialogBinding.getRoot())
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String title = getTextValue(dialogBinding.edtTitle);
            String tag = capitalizeWords(getTextValue(dialogBinding.edtTag));
            String content = getTextValue(dialogBinding.edtContent);

            if (title.isEmpty()) {
                dialogBinding.edtTitle.setError(getString(R.string.title_required));
                return;
            }

            long now = System.currentTimeMillis();
            boolean pinned = dialogBinding.switchPinned.isChecked();

            if (isEditing) {
                note.setTitle(title);
                note.setTag(tag);
                note.setContent(content);
                note.setPinned(pinned);
                note.setUpdatedAt(now);
                repository.update(note, () -> {
                    refreshData();
                    showMessage(getString(R.string.note_saved));
                });
            } else {
                NoteEntity newNote = new NoteEntity(
                        title,
                        content,
                        tag,
                        now,
                        now,
                        pinned
                );
                repository.insert(newNote, () -> {
                    refreshData();
                    showMessage(getString(R.string.note_saved));
                });
            }

            dialog.dismiss();
        }));

        dialog.show();
    }

    private String getTextValue(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    private String capitalizeWords(String input) {
        if (input.isEmpty()) {
            return "";
        }

        String[] parts = input.toLowerCase(Locale.getDefault()).split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }

    @Override
    public void onNoteClick(NoteEntity note) {
        showNoteDialog(note);
    }

    @Override
    public void onMoreClick(NoteEntity note, View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenu().add(0, 1, 0, R.string.edit);
        popupMenu.getMenu().add(0, 2, 1, R.string.delete_note);
        popupMenu.setOnMenuItemClickListener(item -> handleMenuAction(item, note));
        popupMenu.show();
    }

    private boolean handleMenuAction(@NonNull MenuItem item, NoteEntity note) {
        if (item.getItemId() == 1) {
            showNoteDialog(note);
            return true;
        }
        if (item.getItemId() == 2) {
            confirmDelete(note);
            return true;
        }
        return false;
    }

    private void confirmDelete(NoteEntity note) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_note)
                .setMessage(R.string.delete_note_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> repository.delete(note, () -> {
                    refreshData();
                    showMessage(getString(R.string.note_deleted));
                }))
                .show();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
