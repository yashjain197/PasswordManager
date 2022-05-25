package com.passwordmanager.passwordmanager.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {passwordLocalDB.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    //DataBase Instance
    private static RoomDB database;

    //Database Name
    private static String DATABASE_NAME="passwordLocalDatabase";

    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            //Initialise database
            database= Room.databaseBuilder(context.getApplicationContext()
                    ,RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return database;
    }

    //Creating Dao
    public abstract PasswordDao passwordDao();

}
