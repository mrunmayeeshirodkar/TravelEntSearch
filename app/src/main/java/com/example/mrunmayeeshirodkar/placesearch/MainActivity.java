package com.example.mrunmayeeshirodkar.placesearch;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static double lati;
    public static double longi;

    public static final String KEYWORD = "com.example.mrunmayeeshirodkar.placesearch.KEYWORD";
    public static final String CATEGORY = "com.example.mrunmayeeshirodkar.placesearch.CATEGORY";
    public static final String DISTANCE = "com.example.mrunmayeeshirodkar.placesearch.DISTANCE";
    boolean sendkReq = false;
    boolean sendlreq = false;
    private static final String TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameView;
    public  boolean firstTime = false;
    RequestQueue myrequestQueue;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    Intent intent;
    public static ProgressDialog progressDialog;
    public static JSONObject responsef;
    public EditText distances;
    public EditText txt1;
    public Spinner category;
    public AutoCompleteTextView myautoCompleteTextView;
    protected GeoDataClient mGeoDataClient;

    public void enableLoc(View v){
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        if(!firstTime){
            firstTime = true;
            //mNameView = (TextView) findViewById(R.id.name);

            LatLng center = new LatLng(lati, longi);
            double radiusDegrees = 0.03;
            LatLng northEast = new LatLng(center.latitude + radiusDegrees, center.longitude + radiusDegrees);
            LatLng southWest = new LatLng(center.latitude - radiusDegrees, center.longitude - radiusDegrees);
            BOUNDS_MOUNTAIN_VIEW = LatLngBounds.builder().include(northEast).include(southWest).build();


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();


            mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
            //mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_expandable_list_item_2,
              //      BOUNDS_MOUNTAIN_VIEW, null);

            mGeoDataClient = Places.getGeoDataClient(this, null);

            mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_MOUNTAIN_VIEW, null);

            mAutocompleteTextView.setAdapter(mAdapter);
        }
        mAutocompleteTextView.setEnabled(true);

    }

    public void disableLoc(View v){
        AutoCompleteTextView myAutoComptextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        myAutoComptextView.setText("");
        myAutoComptextView.setEnabled(false);
    }

    public void showAutoCompResult(){

    }

    public void clearAllFields(View v){
        TextView clear1 = (TextView)findViewById(R.id.textView);
        EditText clear2 = (EditText)findViewById(R.id.editText4);
        Spinner clear3 = (Spinner)findViewById(R.id.spinner_category);
        EditText clear4 = (EditText)findViewById(R.id.editText5);
        RadioButton clear5 = (RadioButton)findViewById(R.id.radioButton);
        RadioButton clear6 = (RadioButton)findViewById(R.id.radioButton2);
        TextView clear7 = (TextView)findViewById(R.id.textView8);
        AutoCompleteTextView myAutoComptextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        clear1.setText("");
        clear1.setHeight(0);
        clear7.setText("");
        clear7.setHeight(0);
        clear2.setText("");
        clear3.setSelection(0);
        clear4.setText("");
        clear5.setChecked(true);
        clear6.setChecked(false);
        myAutoComptextView.setText("");
        myAutoComptextView.setEnabled(false);
    }

    public void buttonOnClick(View v) {

        txt1 = (EditText)findViewById(R.id.editText4);

        TextView kworderror = (TextView)findViewById(R.id.textView);
        TextView locerror = (TextView)findViewById(R.id.textView8);
        myautoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        String result = txt1.getText().toString().replaceAll(" ", "");
        if(result.isEmpty()) {
            Toast toast = Toast.makeText(MainActivity.this,"Please fix all fields with errors",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,60);
            View vi = toast.getView();
            vi.getBackground().setColorFilter(Color.rgb(230,230,230), PorterDuff.Mode.SRC_IN);
            TextView txt = (TextView) vi.findViewById(android.R.id.message);
            txt.setTextColor(Color.BLACK);
            txt.setTextSize(14);
            toast.show();
            sendkReq = false;

        }
        else{
            //do something
            sendkReq = true;
        }
        RadioButton radioLocation = (RadioButton)findViewById(R.id.radioButton2);
        if(radioLocation.isChecked()){
            AutoCompleteTextView myAutoComptextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
            String resLoc = myAutoComptextView.getText().toString().replaceAll(" ", "");
            if(resLoc.isEmpty()) {
                Toast toast = Toast.makeText(MainActivity.this,"Please fix all fields with errors",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,60);
                View vi = toast.getView();
                vi.getBackground().setColorFilter(Color.rgb(230,230,230), PorterDuff.Mode.SRC_IN);
                TextView txt = (TextView) vi.findViewById(android.R.id.message);
                txt.setTextColor(Color.BLACK);
                txt.setTextSize(14);
                toast.show();
                sendlreq = false;

            }
            else{
                sendlreq = true;
            }
        } else {
            sendlreq = true;
        }
        if(!sendkReq && !sendlreq){
            kworderror.setText("Please Enter Mandatory Field");
            kworderror.setTextSize(14);
            kworderror.setTextColor(Color.RED);
            kworderror.setHeight(60);
            locerror.setText("Please Enter Mandatory Field");
            locerror.setTextSize(14);
            locerror.setTextColor(Color.RED);
            locerror.setHeight(60);
        } else if(!sendkReq && sendlreq){
            kworderror.setText("Please Enter Mandatory Field");
            kworderror.setTextSize(14);
            kworderror.setTextColor(Color.RED);
            kworderror.setHeight(60);
            locerror.setText("");
            locerror.setHeight(0);
        } else if(sendkReq && !sendlreq){
            kworderror.setText("");
            kworderror.setHeight(0);
            locerror.setText("Please Enter Mandatory Field");
            locerror.setTextSize(14);
            locerror.setTextColor(Color.RED);
            locerror.setHeight(60);
        } else if(sendlreq && sendkReq){
            kworderror.setText("");
            kworderror.setHeight(0);
            locerror.setText("");
            locerror.setHeight(0);

            intent = new Intent(MainActivity.this, DisplayListResultsActivity.class);

            category = (Spinner)findViewById(R.id.spinner_category);
            distances = (EditText)findViewById(R.id.editText5);
            intent.putExtra(KEYWORD,txt1.getText().toString());
            intent.putExtra(CATEGORY,category.getSelectedItem().toString());
            intent.putExtra(DISTANCE,distances.getText().toString());

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching Results");
            progressDialog.show();

            new backGroundJob().execute();
        }
    }

    class backGroundJob extends AsyncTask<JSONObject,Void,Void>{

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            String location = "hereloc";

            RadioButton rb = (RadioButton) findViewById(R.id.radioButton2);

            if(rb.isChecked()){
                location = myautoCompleteTextView.getText().toString();
                location = location.replaceAll(" ","+");
                location = location.replaceAll(",","");
            }

            Boolean dok = true;

            myrequestQueue = Volley.newRequestQueue(MainActivity.this);
            String distance = distances.getText().toString();
            if(distance.replaceAll(" ","").isEmpty()){
                distance = "10";
            } else if(Integer.parseInt(distance) < 0 || Integer.parseInt(distance) == 0){
                dok = false;
                Toast.makeText(getApplicationContext(),"Please enter valid distance value.", Toast.LENGTH_SHORT);
            }
            if(dok){
                String url = "http://newphp-env2.us-east-2.elasticbeanstalk.com/place.php?keword="+txt1.getText().toString()+"&category="+category.getSelectedItem().toString().toLowerCase().replaceAll(" ","_")+"&distance="+distance+"&location="+location+"&lat=" + lati + "&lng=" + longi;
                Log.d("MainActivity","Sending Request");
                JsonObjectRequest myjsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MainActivity","Response Recieved");
                        responsef = response;
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

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
    public void startActivityMain(){
        progressDialog.cancel();
        startActivity(intent);
    }

@Override
    protected void onCreate(Bundle savedInstanceState) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //textView = (TextView) findViewById(R.id.textView7);

                lati = location.getLatitude();
                longi = location.getLongitude();

                //textView.append("\n "+lati+" "+longi);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
                },10);
            } else {
                configureButton();
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Drawable drawable = getResources().getDrawable(R.drawable.white_filled_star,null) ;
        drawable.setBounds(0,0,70,70);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString("  FAVORITES");
        spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.getTabAt(1).setText(spannableString);

        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_search,null);
        drawable1.setBounds(0,0,70,70);
        ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BOTTOM);
        SpannableString spannableString1 = new SpannableString("  SEARCH");
        spannableString1.setSpan(imageSpan1,0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabLayout.getTabAt(0).setText(spannableString1);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable3= new GradientDrawable();
            drawable3.setColor(getResources().getColor(R.color.lightgreen));
            drawable3.setSize(1, 1);
            ((LinearLayout) root).setDividerPadding(20);
            ((LinearLayout) root).setDividerDrawable(drawable3);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
}

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    //Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        //mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("MissingPermission")
    private void configureButton() {
        locationManager.requestLocationUpdates("gps", 50000, 50, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
        }
    }


    //Deleted PlaceholderFragment containing a simple view
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //returning the tabs
            switch (position){
                case 0:
                    tab1search tab1 = new tab1search();
                    return tab1;
                case 1:
                    tab2favorites tab2 = new tab2favorites();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyFavAdapter myAdapter = new MyFavAdapter(MyFavAdapter.listItems,this);
        Log.d("MainActivity",MyFavAdapter.listItems.size() + "");
        if(MyFavAdapter.listItems.size() < 1){

            TextView txt = (TextView) findViewById(R.id.section_label);
            txt.setText("No Favorites");
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txt.getLayoutParams();
            params.height = 2000;
            txt.setLayoutParams(params);

        } else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favorites_rec);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(myAdapter);
        }
    }
}
