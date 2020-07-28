package com.abc.practice.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abc.practice.Repository.NoteRepository;
import com.abc.practice.Room.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> AllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        AllNotes = repository.getAllnote();
    }

    //Calling database Operations from NoteRepository in ViewModel as discussed in NoteRespository
    // class
    public void insert(Note note){
        repository.insert(note);
    }
    public void update(Note note){
        repository.update(note);
    }
    public void delete(Note note){
        repository.delete(note);
    }
    public void DeleteAllNotes(){
        repository.DeleteAll();
    }

    public LiveData<List<Note>> getAllNotes(){
        return AllNotes;
    }

   // public void DeleteAll(){}

}
