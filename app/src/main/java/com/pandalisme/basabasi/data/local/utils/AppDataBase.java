package com.pandalisme.basabasi.data.local.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pandalisme.basabasi.data.local.dao.ChatDao;
import com.pandalisme.basabasi.data.local.model.ChatML;

/**
 * Created by kartubi on 20/12/2017.
 */

@Database(entities = {ChatML.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase{

    private static AppDataBase INSTANCE;

    public abstract ChatDao chatDao();

    public static AppDataBase getDataDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "chat")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
