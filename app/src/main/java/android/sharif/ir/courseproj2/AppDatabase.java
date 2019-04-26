package android.sharif.ir.courseproj2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.sharif.ir.courseproj2.JSONResponse;
import android.sharif.ir.courseproj2.JSONResponseDao;

@Database(entities = {JSONResponse.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JSONResponseDao jsonResponseDao();
}