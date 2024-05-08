package com.example.motif;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<String> notes;

    public NotesAdapter(List<String> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String note = notes.get(position);
        holder.noteText.setText(getPreview(note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private String getPreview(String fullText) {
        // Limit the preview to two lines
        String[] lines = fullText.split("\n", 3);
        return lines.length > 2 ? lines[0] + "\n" + lines[1] : fullText;
    }

    public void deleteNoteAt(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteText;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.noteText);
        }
    }
}
