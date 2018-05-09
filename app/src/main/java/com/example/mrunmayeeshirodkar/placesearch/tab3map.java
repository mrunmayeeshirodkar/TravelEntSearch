package com.example.mrunmayeeshirodkar.placesearch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class tab3map extends Fragment implements OnMapReadyCallback {

    public View rootView;
    SupportMapFragment mapFragment;
    AutoCompleteTextView myAutoCompleteTextView;
    private MapPlaceAutocompleteAdapter mAdapter;
    protected GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    RequestQueue myrequestQueue;
    Boolean mapdrive =false;
    LatLng origin;
    String originName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.tab3mmap, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        myAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView2);

        LatLng center = new LatLng(MainActivity.lati, MainActivity.longi);
        double radiusDegrees = 0.03;
        LatLng northEast = new LatLng(center.latitude + radiusDegrees, center.longitude + radiusDegrees);
        LatLng southWest = new LatLng(center.latitude - radiusDegrees, center.longitude - radiusDegrees);
        LatLngBounds BOUNDS_MOUNTAIN_VIEW = LatLngBounds.builder().include(northEast).include(southWest).build();

        myAutoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        mAdapter = new MapPlaceAutocompleteAdapter(getContext(), mGeoDataClient, BOUNDS_MOUNTAIN_VIEW, null);

        myAutoCompleteTextView.setAdapter(mAdapter);

        myAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                showmap();
            }
        });

        return rootView;
    }

    private void showmap() {

        mapdrive = false;

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView2);

        if(autoCompleteTextView.getText().toString().replaceAll(" ","").isEmpty()){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("MainActivity", "hdnsbhjnsbhjmnsbjkjcnbdnsjknbcdnskcnd snkcnd snkcndnckcdnnckcmdnnk  Autocomplete item selected: " + primaryText);

            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getContext(), "Clicked: " + primaryText,
                    //Toast.LENGTH_SHORT).show();
            Log.i("MainActivity", "Called getPlaceById to get Place details for " + item.toString());
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            Log.d("MainActivity","Atleast inside");
            try {
                PlaceBufferResponse places = task.getResult();
                Place myPlace = places.get(0);
                Log.d("MainActivity", "sdfgbfdsasdfghngfdsasdfghnjhgfdxv chjdbcjkshbajxnsjknaxjbsn " + myPlace.getAddress().toString() + "");

                final CharSequence thirdPartyAttribution = places.getAttributions();

                final Place myOtherPlace = myPlace;

                Log.d("MainActivity","Inside here");

                Spinner spinner = (Spinner) rootView.findViewById(R.id.trans_mode);

                final AutoCompleteTextView AutoCompleteTextView2 = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView2);
                String loc_name = AutoCompleteTextView2.getText().toString();
                origin = myOtherPlace.getLatLng();
                originName = myOtherPlace.getName().toString();
                final String checkloc = loc_name.replaceAll(" ","");
                JSONObject gRev = MyAdapter.responsePladeDet;
                String destination = null;

                try {
                    destination = gRev.getString("location");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final  String dest = destination;

                Log.d("MainActivity",spinner.getSelectedItem() + "vhgcgccghuhgbvghvghvhgvghvgnhnhbhjbhjnbvghvhvjhvhjvbhjvhjvhjbhjbjknjkjkjkbjkbjkbjkn");

                if(spinner.getSelectedItem().equals("Driving")){

                    showDirections(rootView, origin, destination, "DRIVING");
                } else if(spinner.getSelectedItem().equals("Bicycling")){
                    showDirections(rootView, origin, destination, "BICYCLING");
                } else if(spinner.getSelectedItem().equals("Transit")){
                    showDirections(rootView, origin, destination, "TRANSIT");
                } else if(spinner.getSelectedItem().equals("Walking")){
                    showDirections(rootView, origin, destination, "WALKING");
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Log.d("MainActivity","Inside here");
                        AutoCompleteTextView m = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView2);
                        Log.d("MainActivity","Inside this thats why showing directions" + m.getText().toString());
                        if(!m.getText().toString().replaceAll(" ","").isEmpty()){

                            if(parent.getItemIdAtPosition(position) == 0){

                                showDirections(rootView, origin, dest, "DRIVING");

                            } else if(parent.getItemIdAtPosition(position) == 1){

                                showDirections(rootView, origin, dest, "BICYCLING");

                            } else if(parent.getItemIdAtPosition(position) == 2){

                                showDirections(rootView, origin, dest, "TRANSIT");

                            } else {

                                showDirections(rootView, origin, dest, "WALKING");

                            }

                        } else {
                            origin = null;
                        }
                        //AIzaSyAs1L-2CpNqQ52yEp2_R_oWFZhM8kjglbE
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e("MainActivity", "Place query did not complete.", e);
                return;
            }
        }
    };

    private static String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static String GOOGLE_API_KEY = "AIzaSyBCtWQrNbs5Du7c9SKETSz-fa60a7r8aOE";

    private void showDirections(View rootView, LatLng origin, String destination, String driving_mode) {

        if(!destination.isEmpty()){
            try{
                String dest = destination.substring(1,destination.length()-1);
                String[] latlong2= dest.split(",");
                double latitude2 = Double.parseDouble(latlong2[0]);
                double longitude2 = Double.parseDouble(latlong2[1]);

                if(driving_mode.equals("DRIVING")){
                    GoogleDirection.withServerKey("AIzaSyBCtWQrNbs5Du7c9SKETSz-fa60a7r8aOE")
                            .from(origin)
                            .to(new LatLng(latitude2,longitude2))
                            .transportMode(TransportMode.DRIVING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        Log.d("MainActivity","3rd Message : " + rawBody);
                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(rawBody);
                                            showthelines(jsonObj);
                                            mapdrive = true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        // Do something
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });
                } else if(driving_mode.equals("BICYCLING")){
                    GoogleDirection.withServerKey("AIzaSyBCtWQrNbs5Du7c9SKETSz-fa60a7r8aOE")
                            .from(origin)
                            .to(new LatLng(latitude2,longitude2))
                            .transportMode(TransportMode.BICYCLING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        Log.d("MainActivity","3rd Message : " + rawBody);
                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(rawBody);
                                            showthelines(jsonObj);
                                            mapdrive = true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        // Do something
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });
                } else if(driving_mode.equals("TRANSIT")){
                    GoogleDirection.withServerKey("AIzaSyBCtWQrNbs5Du7c9SKETSz-fa60a7r8aOE")
                            .from(origin)
                            .to(new LatLng(latitude2,longitude2))
                            .transportMode(TransportMode.TRANSIT)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        Log.d("MainActivity","3rd Message : " + rawBody);
                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(rawBody);
                                            showthelines(jsonObj);
                                            mapdrive = true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        // Do something
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });
                } else if(driving_mode.equals("WALKING")){
                    GoogleDirection.withServerKey("AIzaSyBCtWQrNbs5Du7c9SKETSz-fa60a7r8aOE")
                            .from(origin)
                            .to(new LatLng(latitude2,longitude2))
                            .transportMode(TransportMode.WALKING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if(direction.isOK()) {
                                        Log.d("MainActivity","3rd Message : " + rawBody);
                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(rawBody);
                                            showthelines(jsonObj);
                                            mapdrive = true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        // Do something
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    // Do something
                                }
                            });
                }




            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    PolylineOptions options;



    private void showthelines(JSONObject response) {
        try {
            JSONArray first = response.getJSONArray("routes");
            JSONObject sec = first.getJSONObject(0);
            JSONObject third = sec.getJSONObject("overview_polyline");

            String four = third.getString("points");

            List<LatLng> polyLineList = decodePoly(four);
            Log.d("MainActivity","1st    " + four.toString());

            Log.d("MainActivity", "2nd     " + polyLineList.toString());

            options = new PolylineOptions().width(25).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < polyLineList.size(); z++) {
                LatLng point = polyLineList.get(z);
                options.add(point);
            }

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.map, mapFragment).commit();

            mapFragment.getMapAsync(this);


            //Polyline line = googleMap.addPolyline(options);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private List<LatLng> decodePoly(String encoded) {

        Log.i("Location", "String received: "+encoded);
        int len = encoded.length();

        final List<LatLng> path = new ArrayList<LatLng>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encoded.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encoded.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
    GoogleMap googleMap;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Marker mPerth = null;
        Marker start = null;
        String loc;
        LatLng location = null;
        String name = null;

        JSONObject gRev = MyAdapter.responsePladeDet;
        Log.d("MainActivity","1st fgcvhjbhgvfcgvhjhgvhbjhbvgbhjkhgvhjhbvghjhujhghjihbvghjhujhjhbjhjhjikjhbghujihbhjhjhhjihbhujhbhjhhjkh " + gRev.toString());

        if(mapdrive){


            try {
                loc = gRev.getString("location");
                loc = loc.substring(1,loc.length()-1);
                String[] latlong =  loc.split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                location = new LatLng(latitude, longitude);
                name = gRev.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            googleMap.addPolyline(options).isVisible();
            //googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

            mPerth = googleMap.addMarker(new MarkerOptions()
                    .position(location));

            start = googleMap.addMarker(new MarkerOptions()
                    .title(originName)
                    .position(origin));

            start.showInfoWindow();

        } else {
            try {
                loc = gRev.getString("location");
                loc = loc.substring(1,loc.length()-1);
                String[] latlong =  loc.split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                location = new LatLng(latitude, longitude);
                name = gRev.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            mPerth = googleMap.addMarker(new MarkerOptions()
                    .title(name)
                    .position(location));
        }

        mPerth.showInfoWindow();
    }

}
