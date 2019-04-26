package android.sharif.ir.courseproj2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface JSONResponseDao {

    @Query("SELECT * FROM jsonresponse WHERE url=:url LIMIT 1")
    JSONResponse findByURL(String url);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(JSONResponse... users);

    @Delete
    void delete(JSONResponse user);
}
