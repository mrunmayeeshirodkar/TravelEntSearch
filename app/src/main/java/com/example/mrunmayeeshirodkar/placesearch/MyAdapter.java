package com.example.mrunmayeeshirodkar.placesearch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.squareup.picasso.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

import static com.example.mrunmayeeshirodkar.placesearch.MainActivity.lati;
import static com.example.mrunmayeeshirodkar.placesearch.MainActivity.progressDialog;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private String placeID;
    private ProgressDialog myprogressDialog;
    RequestQueue myrequestQueue;
    public static JSONObject responsePladeDet;
    public static JSONObject yelpRev;
    private GoogleApiClient mGoogleApiClient;
    Intent intent;
    GeoDataClient mGeoDataClient;
    public static PlacePhotoMetadataBuffer photoMetadataBuffer;
    public static Bitmap[] myPhotos;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.placeName.setText(listItem.getPlaceName());
        holder.placeAddress.setText(listItem.getPlaceAddress());
        holder.placeId.setText(listItem.getPlaceId());

        SharedPreferences prefs =  context.getSharedPreferences("Place_Id_Array", context.MODE_PRIVATE);
        if(prefs.contains(listItem.getPlaceId())){
            holder.heartImage.setImageResource(R.drawable.red_filled_heart);
        } else {
            holder.heartImage.setImageResource(R.drawable.heart_outline_black);
        }

        holder.heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), listItem.getPlaceName() + " was added to favorites." ,Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = v.getContext().getSharedPreferences("Place_Id_Array", v.getContext().MODE_PRIVATE);
                String placeID = listItem.getPlaceId();
                String icon = listItem.getImageURL();
                String name = listItem.getPlaceName();
                String address = listItem.getPlaceAddress();
                ImageView imageH = (ImageView) v.findViewById(R.id.imageHeart);
                if(prefs.getAll().isEmpty()){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(placeID ,"0");
                    editor.commit();
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(placeID,0);
                    if(sharedPreferences.getAll().isEmpty()){
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("icon",icon);
                        editor1.putString("name",name);
                        editor1.putString("address",address);
                        editor1.commit();
                    }

                    Toast.makeText(v.getContext(),listItem.getPlaceName() + " was added to favorites.",Toast.LENGTH_SHORT).show();
                    imageH.setImageResource(R.drawable.red_filled_heart);

                    Log.d("MainActivity",sharedPreferences.getAll().toString());
                } else {
                    SharedPreferences.Editor editor = prefs.edit();


                    if(prefs.contains(placeID)){
                        editor.remove(placeID);
                        editor.commit();
                        @SuppressLint({"NewApi", "LocalSuppress"}) Boolean k = v.getContext().deleteSharedPreferences(placeID);
                        if(k){
                            Toast.makeText(v.getContext(),listItem.getPlaceName() + " was removed from favorites.",Toast.LENGTH_SHORT).show();
                        }
                        imageH.setImageResource(R.drawable.heart_outline_black);

                    } else {
                        editor.putString(placeID , prefs.getAll().size() + "");
                        editor.commit();
                        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(placeID,0);
                        if(sharedPreferences.getAll().isEmpty()){
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("icon",icon);
                            editor1.putString("name",name);
                            editor1.putString("address",address);
                            editor1.commit();
                        }
                        Toast.makeText(v.getContext(),listItem.getPlaceName() + " was added to favorites.",Toast.LENGTH_SHORT).show();
                        imageH.setImageResource(R.drawable.red_filled_heart);
                    }

                    Log.d("MainActivity",prefs.getAll().toString());
                }
            }
        });

        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    placeID = listItem.getPlaceId();
                    Log.d("MainActivity", "onClick: " + listItem.getPlaceId());
                    intent = new Intent(context,PlaceDetailsActivity.class);
                    myprogressDialog = new ProgressDialog(context);
                    myprogressDialog.setMessage("Fetching Details");
                    myprogressDialog.show();
                    new BackGroundJob().execute();

            }
        });

        Picasso.with(context).load(listItem.getImageURL()).into(holder.imageView);
    }


    class BackGroundJob extends AsyncTask<JSONObject,Void,Void> {


        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            myrequestQueue = Volley.newRequestQueue(context);
            String url = "http://newphp-env2.us-east-2.elasticbeanstalk.com/place.php?arg1=" + placeID;
            JsonObjectRequest myjsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    Log.d("MainActivity","Response Recieved");
                    responsePladeDet = response;
                    Log.d("MainActivity",response.toString());
                    getPhotos();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("MainActivity","Error Recieved");
                    Log.d("MainActivity",error.toString());
                    myprogressDialog.cancel();
                    Toast.makeText(context, "Please check your internet connection." , Toast.LENGTH_SHORT).show();

                }
            });
            myrequestQueue.add(myjsonRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void getPhotos() {
        mGeoDataClient = Places.getGeoDataClient(context, null);
        Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeID);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                try{
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    photoMetadataBuffer = photos.getPhotoMetadata();

                    Log.d("MainActivity",  " Number of photos " + photoMetadataBuffer.getCount());


                    // Get the first photo in the list.
                    if(photoMetadataBuffer.getCount() == 0){
                        getYelpReviews();

                        myPhotos = null;
                    }
                    else{
                        myPhotos = new Bitmap[photoMetadataBuffer.getCount()];
                        Log.d("MainActivity",  " Number of photos " + photoMetadataBuffer.getCount());

                        int i = 0;
                        for(i=0;i<photoMetadataBuffer.getCount();i++){
                            PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                            // Get a full-size bitmap for the photo.
                            Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);

                            final int finalI = i;
                            photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                    PlacePhotoResponse photo = task.getResult();
                                    myPhotos[finalI] = photo.getBitmap();
                                    Log.d("MainActivity","Completed" + finalI);
                                    if(finalI == photoMetadataBuffer.getCount()-1){
                                        getYelpReviews();

                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e){
                 Log.d("MainActivity","Some Error Occured" + e.toString());
                }

            }
        });
    }

    public void getYelpReviews(){

        myrequestQueue = Volley.newRequestQueue(context);
        String url = "http://newphp21-env.us-east-2.elasticbeanstalk.com/place.php?myToken=mDmd6N8X3NdOMirwAXR-6f8XW3O38KiqrqsaW9cEt2qCsD_wPHw3CNvXprAOKfcmnSR8l16U6ZcbMuLQ72F8Eya7ulF8EI5sSdLwCQGFXyWFde5Ft4vj9LIR78a8WnYx";
        try {
            String nameofplace = responsePladeDet.getString("name").replaceAll(" ","+");
            url = url + "&nameOfPlace=" + nameofplace;
            Log.d("MainActivity","Name of place " + nameofplace);

            if(responsePladeDet.getString("location") != null){
                String loc = responsePladeDet.getString("location");
                url = url + "&location=" + loc;
                Log.d("MainActivity","Location " + loc);
            }


            if(responsePladeDet.getString("vicinity") != null){
                String vici = responsePladeDet.getString("vicinity").replaceAll(" ","+");
                url = url + "&address=" + vici;
                Log.d("MainActivity","Vicinity " + vici);
            }

            if(responsePladeDet.getString("city_rev") != null){
                String adr = responsePladeDet.getString("city_rev");

                adr = adr.replaceAll("<span class=","");
                adr = adr.replaceAll("</span>, ",",");
                adr = adr.replaceAll("</span>",",");
                adr = adr.replaceAll(">",",");
                adr = adr.substring(0,adr.length()-1);
                Log.d("MainActivity","Vicinity " + adr.toString());
                String[] addr = adr.split(",");
                for(int i = 0;i<addr.length;i++){
                    if(addr[i].equals("locality")){
                        url = url + "&city=" + addr[i+1];
                    }
                    if(addr[i].equals("region")){
                        url = url + "&state=" + addr[i+1];
                    }
                }
            }


            url = url.replaceAll("'", "%27");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myjsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                yelpRev = response;

                Log.d("MainActivity",yelpRev.toString());

                startActivityMainK();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("MainActivity","Error Recieved");
                Log.d("MainActivity",error.toString());
                myprogressDialog.cancel();
                Toast.makeText(context, "Please check your internet connection." , Toast.LENGTH_SHORT).show();

            }
        });
        myrequestQueue.add(myjsonRequest);

    }

    public void startActivityMainK(){
        myprogressDialog.cancel();
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView placeName;
        public TextView placeAddress;
        public ImageView imageView;
        ConstraintLayout myLayout;
        public TextView placeId;
        public ImageView heartImage;

        public ViewHolder(View itemView) {
            super(itemView);

            myLayout = (ConstraintLayout) itemView.findViewById(R.id.parent_layout);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeAddress = (TextView) itemView.findViewById(R.id.placeAddress);
            imageView = (ImageView) itemView.findViewById(R.id.imageCompany);
            placeId = (TextView) itemView.findViewById(R.id.placeId);
            heartImage = (ImageView) itemView.findViewById(R.id.imageHeart);

        }
    }

}
