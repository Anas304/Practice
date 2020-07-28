package com.abc.practice.Repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.abc.practice.Room.Note;
import com.abc.practice.Room.NoteDB;
import com.abc.practice.Room.NoteDao;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> Allnote;

    public NoteRepository(Application application){
        //We use Application as context to create our database instance
        NoteDB database = NoteDB.getInstance(application);
        noteDao = database.noteDao();
        Allnote = noteDao.getAllNotes();
    }

    /**These methods below are the API that Repository exposes.So All ViewModels have to call
     * these methods insert,update,delete,DeleteAll and getAll.ViewModel does not have to worry
     * about where the data is coming from and how it is fetched.It just tell that data has been
     * fetched with the help of these AsyncTasks.Hence, this is the way we create Abstraction layer
     * between our DATA and ViewModel which we were talking about in the tutorial.
     * */
    //Methods for our database operations
    public void insert(Note note){
        new insertAsyncTask(noteDao).execute(note);
    }
    public void update(Note note){
        new updateAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note){
        new deleteAsyncTask(noteDao).execute(note);
    }
    public void DeleteAll(){
        new DeleteAllAsyncTask(noteDao).execute();
    }
    public LiveData<List<Note>> getAllnote() {
        return Allnote;
    }


    /**Class below has to be static So that it doesn't have reference to Repository itself*/
    private static class insertAsyncTask extends android.os.AsyncTask<Note,Void,Void> {
        //NoteDao to make database operations
        private NoteDao noteDao;
        /**Since our class is static we cannot access noteDao directly now we need to pass it in our
         * constructor */
        private insertAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    /**Class below has to be static So that it doesn't have reference to Repository itself*/
    private static class updateAsyncTask extends android.os.AsyncTask<Note,Void,Void> {
        //NoteDao to make database operations
        private NoteDao noteDao;
        /**Since our class is static we cannot access noteDao directly now we need to pass it in our
         * constructor */
        private updateAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    /**Class below has to be static So that it doesn't have reference to Repository itself*/
    private static class deleteAsyncTask extends android.os.AsyncTask<Note,Void,Void> {
        //NoteDao to make database operations
        private NoteDao noteDao;
        /**Since our class is static we cannot access noteDao directly now we need to pass it in our
         * constructor */
        private deleteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    /**Class below has to be static So that it doesn't have reference to Repository itself*/
    private static class DeleteAllAsyncTask extends android.os.AsyncTask<Void,Void,Void> {
        //NoteDao to make database operations
        private NoteDao noteDao;
        /**Since our class is static we cannot access noteDao directly now we need to pass it in our
         * constructor */
        private DeleteAllAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.DeleteAll();
            return null;
        }
    }
}
