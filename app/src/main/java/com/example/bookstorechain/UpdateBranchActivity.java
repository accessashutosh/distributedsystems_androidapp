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


public class UpdateBranchActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    Button delete_branchButton;
    Button updateBranchButton;
    Button edit_branchButton;
    Button add_branchButton;
    TextView branchId;
    EditText branchName;
    EditText address;
    EditText city;
    EditText state;
    EditText zip;
    EditText phone;
    List<String> branchList = new ArrayList<String>();
    List<String> branchIdList = new ArrayList<String>();
    private TextView textView;
    private TextView getBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_branch);
        requestQueue = Volley.newRequestQueue(this);
        Bundle b = getIntent().getExtras();
        String branchId = b.getString("branchId");
        populateOneBranches(this, branchId);

        getBackImage = (TextView) findViewById(R.id.backimage);
        getBackImage.bringToFront();
        getBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageBranchActivity.class));
            }
        });
        updateBranchButton = (Button) findViewById(R.id.updateBranch);
        updateBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBranch();
            }
        });

    }

    public void populateOneBranches(Context c, String branchIdpassed) {
        final Context cReceived = c;
        String url = UrlConstants.BRANCHES_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + '/' + branchIdpassed,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonArray = response;
                        try {
                            branchId = (TextView) findViewById(R.id.branchId);
                            branchName = (EditText) findViewById(R.id.branchName);
                            address = (EditText) findViewById(R.id.address);
                            city = (EditText) findViewById(R.id.city);
                            state = (EditText) findViewById(R.id.state);
                            zip = (EditText) findViewById(R.id.zip);
                            phone = (EditText) findViewById(R.id.phone);

                            branchId.setText(response.getString("id"));
                            branchName.setText(response.getString("branchName"));
                            address.setText(response.getString("address"));
                            city.setText(response.getString("city"));
                            state.setText(response.getString("state"));
                            zip.setText(response.getString("zip"));
                            phone.setText(response.getString("phone"));

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

    public void updateBranch() {
        String url = UrlConstants.BRANCHES_URL;
        branchId = (TextView) findViewById(R.id.branchId);
        branchName = (EditText) findViewById(R.id.branchName);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zip);
        phone = (EditText) findViewById(R.id.phone);

        url = url + '/' + branchId.getText().toString();
        JSONObject branch = new JSONObject();
        try {
            branch.put("branchName", branchName.getText().toString());
            branch.put("address", address.getText().toString());
            branch.put("city", city.getText().toString());
            branch.put("state", state.getText().toString());
            branch.put("zip", zip.getText().toString());
            branch.put("phone", phone.getText().toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String mRequestBody = branch.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
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


