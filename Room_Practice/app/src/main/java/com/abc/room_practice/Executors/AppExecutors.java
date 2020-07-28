package com.abc.room_practice.Executors;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;


    public AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }
    public static AppExecutors getInstance(){
        if (sInstance == null){
            synchronized (LOCK){
                /**diskIO is a SingleThreadExecutor.this ensures that our database transaction are
                 * done in order so we do not have "race conditions".
                 * networkIO is a Pool of three threads.This allows us to run different network calls
                 * simultaneously while controlling the number of threads that we have.
                 * Finally,mainThread executor uses the mainThreadExecutor class which essentially
                 * will post runnables using a handle associated with main looper.
                 * Moreover,
                 * when we are in an activity we do not need this main thread executor because we can
                 * use the run on UI thread method.But when we are in a different class and we do
                 * not have run on UI thread, we can access the main thread using this last executor
                 * */
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }
        return sInstance;
    }
    public Executor diskIO (){return diskIO;}
    public Executor mainThread (){return mainThread;}
    public Executor networkIO (){return networkIO;}

    private static class MainThreadExecutor implements Executor {
        private android.os.Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }



}
