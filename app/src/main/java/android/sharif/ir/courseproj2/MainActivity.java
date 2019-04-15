package android.sharif.ir.courseproj2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    boolean isInCommentsPage = false;
    private PostAdapter postAdapter;
    private GridView gridview;
    private TextView title_text;

    @Override
    public void onBackPressed() {
        if (isInCommentsPage) {
            gridview.setAdapter(postAdapter);
            isInCommentsPage = false;
            title_text.setText("");

        } else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);
        findViewById(R.id.about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("About us")
                        .setMessage("Members: \n Mohammad Amin Khashkhashi Moghaddam")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
        ((Switch) findViewById(R.id.grid_list_switch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridview.setNumColumns(3 - gridview.getNumColumns());
            }
        });
        final RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        title_text = findViewById(R.id.title_text);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PostAdapter.ViewHolder holder = (PostAdapter.ViewHolder) view.getTag();
                String url = "https://jsonplaceholder.typicode.com/posts/" + holder.id + "/comments";

                JsonArrayRequest ExampleRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        isInCommentsPage = true;
                        gridview.setAdapter(new CommentAdapter(MainActivity.this, response));
                        title_text.setText("Post #" + holder.id + ", " + response.length() + " Comments");
                    }

                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                        Log.i("Sharif", "onErrorResponse: error" + error.toString());
                    }
                });
                ExampleRequestQueue.add(ExampleRequest);
            }
        });

        String url = "https://jsonplaceholder.typicode.com/posts";


        JsonArrayRequest ExampleRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                postAdapter = new PostAdapter(MainActivity.this, response);
                gridview.setAdapter(postAdapter);
            }

        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.i("Sharif", "onErrorResponse: error" + error.toString());
            }
        });
        ExampleRequestQueue.add(ExampleRequest);

    }
}
