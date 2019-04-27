package android.sharif.ir.courseproj2;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentAdapter extends PostAdapter {

    public CommentAdapter(Context c, JSONArray response) {
        super(c, response);
    }

    protected void fillViewHolder(JSONObject jsonObject, ViewHolder holder) throws JSONException {
        holder.title.setText(jsonObject.getString("name"));
        holder.desc.setText(jsonObject.getString("body"));
        holder.id = jsonObject.getInt("id");
    }
}