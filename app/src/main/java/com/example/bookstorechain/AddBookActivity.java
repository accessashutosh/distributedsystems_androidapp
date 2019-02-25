package com.example.bookstorechain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class AddBookActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    Button addBookButton;
    EditText author;
    EditText title;
    EditText description;
    EditText thumbnailUrl;
    EditText price;
    private TextView getBackImage;

    @Override
    protected void onCreate(Bundle savedInstancethumbnailUrl) {
        super.onCreate(savedInstancethumbnailUrl);
        setContentView(R.layout.activity_add_book);
        requestQueue = Volley.newRequestQueue(this);
        getBackImage = (TextView) findViewById(R.id.backimage);
        getBackImage.bringToFront();
        getBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageBookActivity.class));
            }
        });
        addBookButton  = (Button) findViewById(R.id.addBook);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

    }

    public void addBook() {
        String url = UrlConstants.BOOKS_URL;
        author = (EditText) findViewById(R.id.author);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        thumbnailUrl = (EditText) findViewById(R.id.thumbnailUrl);
        price = (EditText) findViewById(R.id.price);

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, book,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        startActivity(new Intent(getApplicationContext(), ManageBookActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {


                try {
                    String json = new String(
                            response.data,
                            "UTF-8"
                    );

                    if (json.length() == 0) {
                        return Response.success(
                                null,
                                HttpHeaderParser.parseCacheHeaders(response)
                        );
                    } else {
                        return super.parseNetworkResponse(response);
                    }
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}


