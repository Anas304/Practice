package com.abc.room_practice.Room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Listing entities here will create tables in the database.
@Database(entities = {TaskEntry.class} , version = 1 , exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DB extends RoomDatabase {

    //Member constants for this class
    private  static final  String LOG_TAG = DB.class.getSimpleName();
    //The Object below is used to implement Singleton Pattern to instantiate this class only once.
    private static final Object LOCK = new Object();
    private  static final String DB_NAME = "todolist";
    private  static DB sInstance;
    /**What is heck is the Context and how to use it ?
     * An App is a collection of different components working together.
     *  Simplest answer is Context is the bridge between components.
     * You use it to communicate between components, instantiate components or recall components
     * and access components.
     * There are two ways for components to communicate, using Context and Intent.
     * Latter also depends on context to play its role.*/
public static DB getInstance(Context context){
    if (sInstance == null){
        synchronized (LOCK){
            Log.i(LOG_TAG,"Creating new database instance");
            sInstance = Room.databaseBuilder(context.getApplicationContext(),DB.class,DB.DB_NAME)
//                    .addMigrations(new Migration(1, 2) {
//                        @Override
//                        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//                            database.execSQL("");
//
//                        }
//                    })
                    .build();
        }
    }
    Log.i(LOG_TAG,"Getting new database instance");
    return sInstance;
}
public abstract TaskDao taskDao();

}
