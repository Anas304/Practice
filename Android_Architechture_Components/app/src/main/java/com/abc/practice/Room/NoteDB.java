package com.abc.practice.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class NoteDB extends RoomDatabase {
    //Our DB instance
    private static NoteDB instance;
    //Our Dao instance
    public abstract NoteDao noteDao();

    public static synchronized NoteDB getInstance(Context context){
        if (instance == null ){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDB.class,"NoteDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(RoomCAllBacks)
                    .build();
        }
        return instance;
    }


/**If we want our Table to have some Default data we use RoomDatabase Callbacks to pre-Populate our
 * database */
private static RoomDatabase.Callback RoomCAllBacks = new RoomDatabase.Callback(){
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        new PopolateAsyncTask(instance).execute();
    }
};
private static class PopolateAsyncTask extends AsyncTask<Void,Void,Void>{
    private NoteDao noteDao;
    public PopolateAsyncTask(NoteDB db){
        noteDao = db.noteDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        noteDao.insert(new Note("TITLE 1","DESCRIPTION 1",1));
        noteDao.insert(new Note("TITLE 2","DESCRIPTION 2",2));
        noteDao.insert(new Note("TITLE 3","DESCRIPTION 3",3));
        noteDao.insert(new Note("TITLE 4","DESCRIPTION 4",4));
        noteDao.insert(new Note("TITLE 5","DESCRIPTION 5",5));
        return null;
    }
}
}
