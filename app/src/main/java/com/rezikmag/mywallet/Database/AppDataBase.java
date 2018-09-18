package com.rezikmag.mywallet.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Transaction.class},version = 1,exportSchema = false)
@TypeConverters(TimeConverters.class)
public abstract class AppDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "transaction info";
    private static final Object LOCK = new Object();
    private static AppDataBase instanse;

    public static AppDataBase getInstanse(Context context) {
        if (instanse == null) {
            synchronized (LOCK) {
                Log.d("Tag", "Create database instance");
                instanse = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instanse;
    }

    public abstract TransactionDao transactionDao();
}
