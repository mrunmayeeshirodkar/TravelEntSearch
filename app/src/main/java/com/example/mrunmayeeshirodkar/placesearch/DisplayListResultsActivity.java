package com.example.mrunmayeeshirodkar.placesearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayListResultsActivity extends AppCompatActivity{


    RequestQueue myrequestQueue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static List<ListItem> listItems;
    public static int currentPageNo = 0;
    public int previousPageNo = 0;
    public int nextPageNo = 0;
    public String next_page_token = null;
    public String[] nextPageToken = new String[20];
    public JSONObject[] jsonResponse = new JSONObject[20];
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        jsonResponse = new JSONObject[20];
        next_page_token = null;
        nextPageToken = new String[20];
        currentPageNo = 0;
        previousPageNo = 0;
        nextPageNo = 0;

        Log.d("MainActivity","sdfgfdsadfgbfdsdfgbngfdfvbgvfdsfvbgfdsfvbgfdsdfvbgfdsdfvbfdsdfvcfdesfvbgfdffvcbvfd" + MainActivity.responsef.toString());
        showResponse(MainActivity.responsef);
        jsonResponse[currentPageNo] = MainActivity.responsef;

    }


    private void showResponse(JSONObject response){

        Log.d("MainActivity","Current Page Number : " + currentPageNo + " " + nextPageToken[currentPageNo]);
        Log.d("MainActivity","Current Page Number : " + response.toString());
        Log.d("MainActivity","Latitude" + MainActivity.lati);

        listItems = new ArrayList<>();

        try {
            if(response.has("result")){
                Log.d("MainActivity","It has result shujndbhcjvnbdhvjvfnbdhvj vfndbcvh jkvmfdn bcnv jvmfdncvj kvmfd cbnvj vmfcd");
                try {
                    String myjson = response.getString("result");
                    Log.d("MainActivity","It has result shujndbhcjvnbdhvjvfnbdhvj vfndbcvh jkvmfdn bcnv jvmfdncvj kvmfd cbnvj vmfcd");
                    if(myjson.equals("ZERO_RESULTS")){
                        setContentView(R.layout.showlist);
                        Log.d("MainActivity","It has result shujndbhcjvnbdhvjvfnbdhvj vfndbcvh jkvmfdn bcnv jvmfdncvj kvmfd cbnvj vmfcd");
                        RecyclerView mrec = (RecyclerView) findViewById(R.id.recyclerView);
                        mrec.setVisibility(View.GONE);
                        Button b1 = (Button) findViewById(R.id.previousButton);
                        b1.setVisibility(View.GONE);
                        Button b2 = (Button) findViewById(R.id.nextButton);
                        b2.setVisibility(View.GONE);
                        TextView txtv = (TextView) findViewById(R.id.textView7);
                        txtv.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 2000;
                        txtv.setLayoutParams(params);

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                JSONArray myJsonArray = response.getJSONArray("results");
                for(int i=0;i<myJsonArray.length();i++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    ListItem listItem = new ListItem(myobj.getString("name"),myobj.getString("vicinity"),myobj.getString("icon"),myobj.getString("place_id"));
                    listItems.add(listItem);
                }
                adapter = new MyAdapter(listItems,this);
            }

        } catch (JSONException e) {

            JSONArray myJsonArray = null;

            e.printStackTrace();
        }
        if(response.has("results")){
            setContentView(R.layout.showlist);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(DisplayListResultsActivity.this));
            recyclerView.setAdapter(adapter);
            try {
                if(response.has("next_page_token")){
                    next_page_token = response.getString("next_page_token");
                } else {
                    next_page_token = null;
                }

                Button prevButton = (Button) findViewById(R.id.previousButton);
                Button nextButton = (Button) findViewById(R.id.nextButton);

                if(next_page_token != null) {

                    nextPageToken[currentPageNo] = next_page_token;
                    if(currentPageNo == 0){
                        prevButton.setEnabled(false);
                        nextButton.setEnabled(true);
                    }
                    else{
                        prevButton.setEnabled(true);
                        nextButton.setEnabled(true);
                    }

                } else {
                    if(currentPageNo == 0){
                        prevButton.setEnabled(false);
                    } else {
                        prevButton.setEnabled(true);
                    }
                    nextButton.setEnabled(false);
                }
            } catch (JSONException e) {

                try {
                    String myjson = response.getString("result");
                    if(myjson.equals("ZERO_RESULTS")){
                        RecyclerView mrec = (RecyclerView) findViewById(R.id.recyclerView);
                        mrec.setVisibility(View.GONE);
                        Button b1 = (Button) findViewById(R.id.previousButton);
                        b1.setVisibility(View.GONE);
                        Button b2 = (Button) findViewById(R.id.nextButton);
                        b2.setVisibility(View.GONE);
                        TextView txtv = (TextView) findViewById(R.id.textView7);
                        txtv.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 2000;
                        txtv.setLayoutParams(params);

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
                next_page_token = null;
            }
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyAdapter myAdapter = new MyAdapter(listItems,this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayListResultsActivity.this));
        recyclerView.setAdapter(myAdapter);

    }

    public void showPreviousPage(View v){

        Button prevButton = (Button) findViewById(R.id.previousButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);

        currentPageNo = currentPageNo - 1;
        previousPageNo = currentPageNo - 1;
        nextPageNo = currentPageNo + 1;

        Log.d("MainActivity","Current Page Number : " + currentPageNo + " " + nextPageToken[currentPageNo]);

        if(currentPageNo == 0){
            prevButton.setEnabled(false);

        } else {
            prevButton.setEnabled(true);
        }

        if(nextPageToken[currentPageNo] != null) {
            showResponse(jsonResponse[currentPageNo]);
        } else {
            nextButton.setEnabled(false);
        }

    }

    public void showNextPage(View v){

        Button prevButton = (Button) findViewById(R.id.previousButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        currentPageNo = currentPageNo + 1;
        nextPageNo = currentPageNo + 1;
        previousPageNo = currentPageNo - 1;

        Log.d("MainActivity","this is next page token of previous page " + nextPageToken[previousPageNo]);

        Log.d("MainActivity","Current Page Number : " + currentPageNo + " " + nextPageToken[currentPageNo]);

        if(currentPageNo == 0){
            prevButton.setEnabled(false);

        } else {
            prevButton.setEnabled(true);

        }

        if(jsonResponse[currentPageNo] != null){

            showResponse(jsonResponse[currentPageNo]);

        } else {
            if(nextPageToken[previousPageNo] != null){

                progressDialog = new ProgressDialog(DisplayListResultsActivity.this);
                progressDialog.setMessage("Fetching Next Page");
                progressDialog.show();

                new backProcess().execute();

            }
        }

    }

    class backProcess extends AsyncTask<JSONObject,Void,Void> {

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            myrequestQueue = Volley.newRequestQueue(DisplayListResultsActivity.this);
            String url = "http://newphp-env2.us-east-2.elasticbeanstalk.com/place.php?pagetoken=" + nextPageToken[previousPageNo];
            JsonObjectRequest myjsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MainActivity","Response Recieved");
                            jsonResponse[currentPageNo] = response;
                            startActivityMain();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("MainActivity","Error Recieved");
                    Log.d("MainActivity",error.toString());
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Please check your internet connection." , Toast.LENGTH_SHORT).show();

                }
            });
            myrequestQueue.add(myjsonRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void startActivityMain(){
        progressDialog.cancel();
        showResponse(jsonResponse[currentPageNo]);
    }

}
