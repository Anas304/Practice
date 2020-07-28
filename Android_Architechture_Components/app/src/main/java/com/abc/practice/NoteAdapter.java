package com.abc.practice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.practice.Room.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    /**This is where we take care of getting the data from the single "Note java objects"
     * into the Views of our NoteHolder  */
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        //Here we need Note Object Reference
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * In our MainActivity we observe the liveData and in onChanged CallBack we pass the list of notes
     * and we need a way to get these list of notes in our RecyclerView and this is what this method is
     * for
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    //this is used to get the positon of the Note Object So we can Swipe to delete the note at that
    //Exact position.
    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            //Calling method on the itemView that holds the views.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    //Making cards Clickable
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListerner(OnItemClickListener listerner) {
        this.listener = listerner;

    }


}
