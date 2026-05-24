package com.codewithngoc.day08apptakenote.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    public interface NotesCallback {
        void onResult(List<NoteEntity> notes);
    }

    public interface TagsCallback {
        void onResult(List<String> tags);
    }

    private final NoteDao noteDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public NoteRepository(Context context) {
        noteDao = NoteDatabase.getInstance(context).noteDao();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getNotes(String query, String tag, NotesCallback callback) {
        executorService.execute(() -> {
            List<NoteEntity> notes = noteDao.getNotes(query.trim(), tag);
            mainHandler.post(() -> callback.onResult(notes));
        });
    }

    public void getTags(TagsCallback callback) {
        executorService.execute(() -> {
            List<String> tags = noteDao.getTags();
            mainHandler.post(() -> callback.onResult(tags));
        });
    }

    public void insert(NoteEntity note, Runnable onDone) {
        executorService.execute(() -> {
            noteDao.insert(note);
            postDone(onDone);
        });
    }

    public void update(NoteEntity note, Runnable onDone) {
        executorService.execute(() -> {
            noteDao.update(note);
            postDone(onDone);
        });
    }

    public void delete(NoteEntity note, Runnable onDone) {
        executorService.execute(() -> {
            noteDao.delete(note);
            postDone(onDone);
        });
    }

    public void seedIfEmpty(Runnable onDone) {
        executorService.execute(() -> {
            if (noteDao.countNotes() == 0) {
                long now = System.currentTimeMillis();
                List<NoteEntity> samples = new ArrayList<>();
                samples.add(new NoteEntity(
                        "UX/UI Design Tutorial",
                        "Review wireframes and summarize the core user flow before the next design meeting.",
                        "Design",
                        now - 86400000L * 5,
                        now - 600000L,
                        true
                ));
                samples.add(new NoteEntity(
                        "Digital Marketing Journey",
                        "Draft social media hooks and collect campaign metrics for the weekly report.",
                        "Marketing",
                        now - 86400000L * 3,
                        now - 3600000L,
                        false
                ));
                samples.add(new NoteEntity(
                        "Progress in the profession",
                        "List three backend topics to practice this month and schedule one mock interview.",
                        "Career",
                        now - 86400000L * 2,
                        now - 7200000L,
                        false
                ));
                for (NoteEntity sample : samples) {
                    noteDao.insert(sample);
                }
            }
            postDone(onDone);
        });
    }

    private void postDone(Runnable onDone) {
        if (onDone != null) {
            mainHandler.post(onDone);
        }
    }
}
