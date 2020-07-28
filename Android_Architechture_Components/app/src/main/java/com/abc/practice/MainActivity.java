package com.abc.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abc.practice.Room.Note;
import com.abc.practice.viewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE__RESQUEST = 1;
    public static final int EDIT_NOTE__RESQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton ButtonAddNotes = findViewById(R.id.button_add_notes);
        ButtonAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNotesActivity.class);
                startActivityForResult(intent,ADD_NOTE__RESQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

          final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //viewModel Reference
        noteViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Update RecyclerView
                adapter.setNotes(notes);
            }
        });

        //Swipe to Delete implementations
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListerner(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intentEdit = new Intent(MainActivity.this, AddEditNotesActivity.class);
                //Now we are in AddNoteActivity we need all the fields to perform required action
                /**Room need to know the id of the note that is updated So we need another code for
                 * id i.e Primary Key */
                intentEdit.putExtra(AddEditNotesActivity.EXTRA_ID,note.getId());
                intentEdit.putExtra(AddEditNotesActivity.EXTRA_TITLE,note.getTitle());
                intentEdit.putExtra(AddEditNotesActivity.EXTRA_DECRIPTION,note.getDescription());
                intentEdit.putExtra(AddEditNotesActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intentEdit,EDIT_NOTE__RESQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE__RESQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditNotesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNotesActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNotesActivity.EXTRA_PRIORITY,1);

            Note notes = new Note(title,description,priority);
            noteViewModel.insert(notes);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_NOTE__RESQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditNotesActivity.EXTRA_ID,-1);

            if (id == -1){
                Toast.makeText(this, "Note can't be" +
                        "Updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNotesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNotesActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNotesActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);

            //Without  note.setId(id); the update operation wont work cuz with other PrimaryKey
            //Room cannot identify this entry.Here Room will know which entry is updated using
            //setId method.
            note.setId(id);

            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();


        }
            else {
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.DeleteAllNotes();
                Toast.makeText(this, "All notes Deleted", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}