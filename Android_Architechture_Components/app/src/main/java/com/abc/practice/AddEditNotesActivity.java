package com.abc.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.practice.Room.Note;

import static com.abc.practice.R.id.save_notes;
import static com.abc.practice.R.id.text_view_title;

public class AddEditNotesActivity extends AppCompatActivity {
    //Keys constants for intentExtras
    public static final String EXTRA_TITLE =
            "com.abc.practice.EXTRA_TITLE";
    public static final String EXTRA_DECRIPTION =
            "com.abc.practice.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.abc.practice.EXTRA_PRIORITY";
    public static final String EXTRA_ID =
            "com.abc.practice.EXTRA_ID";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        //NumberPicker Min and Max value
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        //LOGIC FOR AddEditNoteActivity
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DECRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }
        else {
            setTitle("Add Note");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_notes_menu, menu);
        return true;
    }
    /**
     * Below method info is in the MVVM tutorial part 7 "16:00"
     */
    private void Savenotes() {
        String title = editTextTitle.getText().toString();
        String descrption = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || descrption.trim().isEmpty()) {
            Toast.makeText(this, "Please insert something in the Title and " +
                    "the description", Toast.LENGTH_SHORT).show();
            return;
        }

        //Using intent to pass data and get this data from MainActivity
        Intent intentData = new Intent();
        intentData.putExtra(EXTRA_TITLE, title);
        intentData.putExtra(EXTRA_DECRIPTION, descrption);
        intentData.putExtra(EXTRA_PRIORITY, priority);

        //Here we need id of the result intent So our MainActivity can figure out which item needs
        //to be updated in the database
        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            intentData.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK, intentData);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_notes:
                Savenotes();
            default:
                return super.onOptionsItemSelected(item);
        }


    }


}