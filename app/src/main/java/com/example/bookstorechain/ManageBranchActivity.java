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


public class ManageBranchActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    Button delete_branchButton;
    Button edit_branchButton;
    Button add_branchButton;
    List<String> branchList = new ArrayList<String>();
    List<String> branchIdList = new ArrayList<String>();
    private TextView textView;
    private TextView getBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_branches);
        requestQueue = Volley.newRequestQueue(this);
        populateAllBranches(this);
        getBackImage = (TextView) findViewById(R.id.backimage);
        getBackImage.bringToFront();
        getBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            }
        });

        delete_branchButton = (Button) findViewById(R.id.delete_branch);
        edit_branchButton = (Button) findViewById(R.id.edit_branch);
        add_branchButton = (Button) findViewById(R.id.add_branch);

        delete_branchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner branchId = (Spinner) findViewById(R.id.branchIdDropdown);
                deleteBranch(branchId.getSelectedItem().toString());
            }
        });
        edit_branchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateBranchActivity.class);
                Spinner branchId = (Spinner) findViewById(R.id.branchIdDropdown);

                i.putExtra("branchId", branchId.getSelectedItem().toString());
                startActivity(i);
            }
        });
        add_branchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBranchActivity.class));
            }
        });
    }

    public void populateAllBranches(Context c) {
        final Context cReceived = c;
        String url = UrlConstants.BRANCHES_URL;
        branchList.add("BRANCH ID " + "--" + "BRANCH NAME");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = response;
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                String branchId = jo.getString("id");
                                String branchName = jo.getString("branchName");
                                branchList.add(branchId + "       -------       " + branchName);
                                branchIdList.add(branchId);
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(cReceived,
                                    R.layout.activity_branch_list, branchList);
                            ListView listView = (ListView) findViewById(R.id.branchlist);
                            listView.setAdapter(adapter);
                            Spinner dropdown = findViewById(R.id.branchIdDropdown);
                            ArrayAdapter<String> adapterDropDown = new ArrayAdapter<String>(cReceived, android.R.layout.simple_spinner_dropdown_item, branchIdList);
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

    public void deleteBranch(String branchId) {
        String url = UrlConstants.BRANCHES_URL + '/' + branchId;
        JSONObject branch = new JSONObject();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE", response);
                startActivity(new Intent(getApplicationContext(), ManageBranchActivity.class));
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


