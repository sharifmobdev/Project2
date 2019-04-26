package android.sharif.ir.courseproj2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class CommentAdapter extends BaseAdapter {
    private final JSONArray mResponse;

    static class ViewHolder {
        TextView name;
        TextView body;
    }

    private Context mContext;

    @Override
    public int getCount() {
        return mResponse.length();
    }

    public CommentAdapter(Context c, JSONArray response) {
        mContext = c;
        mResponse = response;

    }

    @Override

    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            // You should fetch the LayoutInflater once in your constructor
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    mContext.getResources().getDimensionPixelSize(R.dimen.thumbnail_height)));
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.text1);
            holder.body = convertView.findViewById(R.id.text2);
            convertView.setTag(holder);

            // Initialize ViewHolder here
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.name.setText(mResponse.getJSONObject(i).getString("name"));
            holder.body.setText(mResponse.getJSONObject(i).getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
