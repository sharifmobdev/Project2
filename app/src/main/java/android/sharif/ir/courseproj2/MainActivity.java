package android.sharif.ir.courseproj2;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    boolean isInCommentsPage = false;
    private PostAdapter postAdapter;
    private GridView gridview;
    private TextView title_text;
    private AppDatabase db;
    private int FIVE_MINS_MILLIS;

    @Override
    public void onBackPressed() {
        if (isInCommentsPage) {
            gridview.setAdapter(postAdapter);
            isInCommentsPage = false;
            title_text.setText(postAdapter.getCount() + " posts");

        } else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

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
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                if (isInCommentsPage) return;
                final PostAdapter.ViewHolder holder = (PostAdapter.ViewHolder) view.getTag();
                final String url = "https://jsonplaceholder.typicode.com/posts/" + holder.id + "/comments";
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final JSONResponse jsonResponse = db.jsonResponseDao().findByURL(url);
                        if (!isCacheValid(jsonResponse)) {

                            JsonArrayRequest ExampleRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(final JSONArray response) {
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            JSONResponse jsonResponse = new JSONResponse();
                                            jsonResponse.response = response.toString();
                                            jsonResponse.url = url;
                                            jsonResponse.fetch_time = System.currentTimeMillis();
                                            db.jsonResponseDao().insertAll(jsonResponse);
                                        }
                                    });
                                    setupCommentsPage(response, holder.id);
                                }

                            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("Sharif", "onErrorResponse: error" + error.toString());
                                    if (jsonResponse != null) {
                                        setupCommentsPage(jsonResponse.getJsonArray(), holder.id);
                                        return;
                                    }
                                    new AlertDialog.Builder(view.getContext())
                                            .setTitle("Error")
                                            .setMessage("Sorry could not load post comments :(")
                                            .setPositiveButton(android.R.string.ok, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });
                            ExampleRequestQueue.add(ExampleRequest);
                        } else {
                            setupCommentsPage(jsonResponse.getJsonArray(), holder.id);
                            Log.i("Sharif", "Using db cache");
                        }

                    }
                });

            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String url = "https://jsonplaceholder.typicode.com/posts";
                final JSONResponse jsonResponse = db.jsonResponseDao().findByURL(url);
                if (!isCacheValid(jsonResponse)) {
                    JsonArrayRequest ExampleRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    JSONResponse jsonResponse = new JSONResponse();
                                    jsonResponse.response = response.toString();
                                    jsonResponse.url = "https://jsonplaceholder.typicode.com/posts";
                                    jsonResponse.fetch_time = System.currentTimeMillis();
                                    db.jsonResponseDao().insertAll(jsonResponse);
                                }
                            });
                            setupMainPage(response);
                        }

                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Sharif", "onErrorResponse: error" + error.toString());
                            if (jsonResponse != null) {
                                setupMainPage(jsonResponse.getJsonArray());
                                return;
                            }
                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("Error")
                                    .setMessage("Sorry could not posts :(")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });

                    ExampleRequestQueue.add(ExampleRequest);
                } else {
                    setupMainPage(jsonResponse.getJsonArray());
                    Log.i("Sharif", "Using db cache");
                }

            }
        });

    }

    private void setupMainPage(final JSONArray response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter = new PostAdapter(MainActivity.this, response);
                gridview.setAdapter(postAdapter);
                title_text.setText(postAdapter.getCount() + " posts");
            }
        });
    }

    private boolean isCacheValid(JSONResponse jsonResponse) {
        if (jsonResponse == null)
            return false;
        FIVE_MINS_MILLIS = 5 * 60 * 1000;
        if (System.currentTimeMillis() - jsonResponse.fetch_time > FIVE_MINS_MILLIS)
            return false;
        return true;
    }

    private void setupCommentsPage(final JSONArray response, final int postId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isInCommentsPage = true;
                gridview.setAdapter(new CommentAdapter(MainActivity.this, response));
                title_text.setText("Post #" + postId + ", " + response.length() + " Comments");

            }
        });
    }
}
