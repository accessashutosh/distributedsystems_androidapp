package com.example.bookstorechain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ManageBookActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    Button delete_bookButton;
    Button edit_bookButton;
    Button add_bookButton;
    List<String> bookList = new ArrayList<String>();
    List<String> bookIdList = new ArrayList<String>();
    private TextView textView;
    private TextView getBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_books);
        requestQueue = Volley.newRequestQueue(this);
        populateAllBooks(this);
        getBackImage = (TextView) findViewById(R.id.backimage);
        getBackImage.bringToFront();
        getBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            }
        });

        delete_bookButton = (Button) findViewById(R.id.delete_book);
        edit_bookButton = (Button) findViewById(R.id.edit_book);
        add_bookButton = (Button) findViewById(R.id.add_book);

        delete_bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner bookId = (Spinner) findViewById(R.id.bookIdDropdown);
                deleteBook(bookId.getSelectedItem().toString());
            }
        });
        edit_bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateBookActivity.class);
                Spinner bookId = (Spinner) findViewById(R.id.bookIdDropdown);

                i.putExtra("bookId", bookId.getSelectedItem().toString());
                startActivity(i);
            }
        });
        add_bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
            }
        });
    }

    public void populateAllBooks(Context c) {
        final Context cReceived = c;
        String url = UrlConstants.BOOKS_URL;
        bookList.add("BOOK ID " + "--" + "  BOOK TITLE");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = response;
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                String bookId = jo.getString("id");
                                String bookTitle = jo.getString("title");
                                bookList.add(bookId + "       -------       " + bookTitle);
                                bookIdList.add(bookId);
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(cReceived,
                                    R.layout.activity_book_list, bookList);
                            ListView listView = (ListView) findViewById(R.id.booklist);
                            listView.setAdapter(adapter);
                            Spinner dropdown = findViewById(R.id.bookIdDropdown);
                            ArrayAdapter<String> adapterDropDown = new ArrayAdapter<String>(cReceived, android.R.layout.simple_spinner_dropdown_item, bookIdList);
                            dropdown.setAdapter(adapterDropDown);
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
        requestQueue.add(jsonArrayRequest);
    }

    public void deleteBook(String bookId) {
        String url = UrlConstants.BOOKS_URL + '/' + bookId;
        JSONObject book = new JSONObject();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
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


