package com.example.bookstorechain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class UpdateBookActivity extends AppCompatActivity {
    RequestQueue requestQueue;
     Button updateBookButton;
     TextView bookId;
    EditText author;
    EditText title;
    EditText description;
    EditText thumbnailUrl;
    EditText price;
    private TextView getBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        requestQueue = Volley.newRequestQueue(this);
        Bundle b = getIntent().getExtras();
        String bookId = b.getString("bookId");
        populateOneBooks(this, bookId);

        getBackImage = (TextView) findViewById(R.id.backimage);
        getBackImage.bringToFront();
        getBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageBookActivity.class));
            }
        });
        updateBookButton = (Button) findViewById(R.id.updateBook);
        updateBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });

    }

    public void populateOneBooks(Context c, String bookIdpassed) {
        final Context cReceived = c;
        String url = UrlConstants.BOOKS_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + '/' + bookIdpassed,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonArray = response;
                        try {
                            bookId = (TextView) findViewById(R.id.bookId);
                            author = (EditText) findViewById(R.id.author);
                            title = (EditText) findViewById(R.id.title);
                            description = (EditText) findViewById(R.id.description);
                            thumbnailUrl = (EditText) findViewById(R.id.thumbnailUrl);
                            price = (EditText) findViewById(R.id.price);

                            bookId.setText(response.getString("id"));
                            author.setText(response.getString("author"));
                            title.setText(response.getString("title"));
                            description.setText(response.getString("description"));
                            thumbnailUrl.setText(response.getString("thumbnailUrl"));
                            price.setText(response.getString("price"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void updateBook() {
        String url = UrlConstants.BOOKS_URL;
        bookId = (TextView) findViewById(R.id.bookId);
        author = (EditText) findViewById(R.id.author);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        thumbnailUrl = (EditText) findViewById(R.id.thumbnailUrl);
        price = (EditText) findViewById(R.id.price);

        url = url + '/' + bookId.getText().toString();
        JSONObject book = new JSONObject();
        try {
            book.put("author", author.getText().toString());
            book.put("title", title.getText().toString());
            book.put("description", description.getText().toString());
            book.put("thumbnailUrl", thumbnailUrl.getText().toString());
            book.put("price", price.getText().toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String mRequestBody = book.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE", response);
                startActivity(new Intent(getApplicationContext(), ManageBookActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(stringRequest);


    }
}


