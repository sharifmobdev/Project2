package android.sharif.ir.courseproj2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

@Entity
public class JSONResponse {
    @PrimaryKey
    @NonNull
    public String url;

    @ColumnInfo(name = "response")
    public String response;

    @ColumnInfo(name = "time")
    public long fetch_time;

    public JSONArray getJsonArray() {
        try {
            return new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
