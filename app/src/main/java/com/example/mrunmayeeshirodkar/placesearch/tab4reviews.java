package com.example.mrunmayeeshirodkar.placesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class tab4reviews extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab4rreviews, container, false);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.reviews);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position) == 0){
                    getGoogleRev(container);
                }
                else if(parent.getItemIdAtPosition(position) == 1){
                    getYelpRev(container);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner order = (Spinner) rootView.findViewById(R.id.order_reviews);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Spinner spinner = (Spinner) rootView.findViewById(R.id.reviews);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(rootView.getContext(),parent.getItemIdAtPosition(position)+ " is selected", Toast.LENGTH_SHORT).show();
                if(parent.getItemIdAtPosition(position) == 0){
                    if(spinner.getSelectedItem().equals("Google Reviews")){
                        getGoogleRev(container);
                    } else {
                        getYelpRev(container);
                    }
                } else if(parent.getItemIdAtPosition(position) == 1){

                    if(spinner.getSelectedItem().equals("Google Reviews")){
                        getGoogleRevHRate(container);
                    } else {
                        getYelpRevHRate(container);
                    }

                } else if(parent.getItemIdAtPosition(position) == 2){

                    if(spinner.getSelectedItem().equals("Google Reviews")){
                        getGoogleRevLRate(container);
                    } else {
                        getYelpRevLRate(container);
                    }

                } else if(parent.getItemIdAtPosition(position) == 3){

                    if(spinner.getSelectedItem().equals("Google Reviews")){
                        getGoogleRevMRec(container);
                    } else {
                        getYelpRevMRec(container);
                    }

                } else if(parent.getItemIdAtPosition(position) == 4){
                    if(spinner.getSelectedItem().equals("Google Reviews")){
                        getGoogleRevLRec(container);
                    } else {
                        getYelpRevLRec(container);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private List<reviewItem> listItems;
    private RecyclerView.Adapter adapter;

    private void getYelpRev(ViewGroup parent) {

        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        JSONObject gRev = MyAdapter.yelpRev;
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);
        Spinner spinner6 = (Spinner) parent.findViewById(R.id.order_reviews);
        if(spinner6.getSelectedItemPosition() == 0){
            try {
                JSONArray myJsonArray = gRev.getJSONArray("reviews");
                for (int i = 0; i < myJsonArray.length(); i++) {
                    txtv.setHeight(0);
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    String imageUrl;
                    Log.d("MainActivity","" +myobj.getJSONObject("user").getString("image_url") );

                    reviewItem listItem = new reviewItem(myobj.getJSONObject("user").getString("image_url"), myobj.getJSONObject("user").getString("name"), myobj.getString("rating"), myobj.getString("time_created"), myobj.getString("text"),myobj.getString("url"));
                    listItems.add(listItem);
                }
                adapter = new ReviewAdapter(listItems, parent.getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                params.height = 1720;
                txtv.setLayoutParams(params);

            }
        } else if(spinner6.getSelectedItemPosition() == 1){
            getYelpRevHRate(parent);
        } else if(spinner6.getSelectedItemPosition() == 2){
            getYelpRevLRate(parent);
        } else if(spinner6.getSelectedItemPosition() == 3){
            getYelpRevMRec(parent);
        } else if(spinner6.getSelectedItemPosition() == 4){
            getYelpRevLRec(parent);
        }



    }

    private void getGoogleRev(ViewGroup parent) {

        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        JSONObject gRev = MyAdapter.responsePladeDet;
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);
        Spinner spinner6 = (Spinner) parent.findViewById(R.id.order_reviews);

        if(spinner6.getSelectedItemPosition() == 0){
            try {
                if(!gRev.getString("results").isEmpty()){
                    try {
                        JSONArray myJsonArray = gRev.getJSONArray("results");
                        for (int i = 0; i < myJsonArray.length(); i++) {
                            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                            params.height = 0;
                            txtv.setLayoutParams(params);
                            JSONObject myobj = myJsonArray.getJSONObject(i);
                            Date d = new Date(Integer.parseInt(myobj.getString("time")) * 1000L);
                            //SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD HH-MM-SS");

                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                            formatter.setTimeZone(TimeZone.getTimeZone("Los Angeles"));

                            reviewItem listItem = new reviewItem(myobj.getString("profile_photo_url"), myobj.getString("author_name"), myobj.getString("rating"),formatter.format(d) , myobj.getString("text"),myobj.getString("author_url"));
                            listItems.add(listItem);

                        }
                        adapter = new ReviewAdapter(listItems, parent.getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            } catch (JSONException e) {
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                params.height = 1720;
                txtv.setLayoutParams(params);
                e.printStackTrace();
            }
        } else if(spinner6.getSelectedItemPosition() == 1){
            getGoogleRevHRate(parent);
        } else if(spinner6.getSelectedItemPosition() == 2){
            getGoogleRevLRate(parent);
        } else if(spinner6.getSelectedItemPosition() == 3){
            getGoogleRevMRec(parent);
        } else if(spinner6.getSelectedItemPosition() == 4){
            getGoogleRevLRec(parent);
        }



    }

    private void getYelpRevLRec(View parent) {
        JSONObject gRev = MyAdapter.yelpRev;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        try {
            JSONArray myJsonArray = gRev.getJSONArray("reviews");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("time_created");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });


            for (int i = 0; i < myJsonArray.length(); i++) {
                txtv.setHeight(0);
                JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));

                reviewItem listItem = new reviewItem(myobj.getJSONObject("user").getString("image_url"), myobj.getJSONObject("user").getString("name"), myobj.getString("rating"), myobj.getString("time_created"), myobj.getString("text"),myobj.getString("url"));
                listItems.add(listItem);
            }
            adapter = new ReviewAdapter(listItems, parent.getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getGoogleRevLRec(ViewGroup parent) {

        JSONObject gRev = MyAdapter.responsePladeDet;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        JSONArray myJsonArray = null;
        try {
            myJsonArray = gRev.getJSONArray("results");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("time");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });

            if(!gRev.getString("results").isEmpty()){
                try {
                    for (int i = 0; i < myJsonArray.length(); i++) {
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 0;
                        txtv.setLayoutParams(params);
                        JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));
                        Date d = new Date(Integer.parseInt(myobj.getString("time")) * 1000L);
                        //SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD HH-MM-SS");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("Los Angeles"));

                        reviewItem listItem = new reviewItem(myobj.getString("profile_photo_url"), myobj.getString("author_name"), myobj.getString("rating"),formatter.format(d) , myobj.getString("text"),myobj.getString("author_url"));
                        listItems.add(listItem);

                    }
                    adapter = new ReviewAdapter(listItems, parent.getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }


    private void getYelpRevMRec(View parent) {
        JSONObject gRev = MyAdapter.yelpRev;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        try {
            JSONArray myJsonArray = gRev.getJSONArray("reviews");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("time_created");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });


            for (int i = myJsonArray.length()-1; i >= 0; i--) {
                txtv.setHeight(0);
                JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));

                reviewItem listItem = new reviewItem(myobj.getJSONObject("user").getString("image_url"), myobj.getJSONObject("user").getString("name"), myobj.getString("rating"), myobj.getString("time_created"), myobj.getString("text"),myobj.getString("url"));
                listItems.add(listItem);
            }
            adapter = new ReviewAdapter(listItems, parent.getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getGoogleRevMRec(ViewGroup parent) {

        JSONObject gRev = MyAdapter.responsePladeDet;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        JSONArray myJsonArray = null;
        try {
            myJsonArray = gRev.getJSONArray("results");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("time");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });

            if(!gRev.getString("results").isEmpty()){
                try {
                    for (int i = myJsonArray.length()-1; i >= 0; i--) {
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 0;
                        txtv.setLayoutParams(params);
                        JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));
                        Date d = new Date(Integer.parseInt(myobj.getString("time")) * 1000L);
                        //SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD HH-MM-SS");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("Los Angeles"));

                        reviewItem listItem = new reviewItem(myobj.getString("profile_photo_url"), myobj.getString("author_name"), myobj.getString("rating"),formatter.format(d) , myobj.getString("text"),myobj.getString("author_url"));
                        listItems.add(listItem);

                    }
                    adapter = new ReviewAdapter(listItems, parent.getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getYelpRevLRate(View parent) {
        JSONObject gRev = MyAdapter.yelpRev;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        try {
            JSONArray myJsonArray = gRev.getJSONArray("reviews");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("rating");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });


            for (int i = 0; i < myJsonArray.length() ; i++) {
                txtv.setHeight(0);
                JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));

                reviewItem listItem = new reviewItem(myobj.getJSONObject("user").getString("image_url"), myobj.getJSONObject("user").getString("name"), myobj.getString("rating"), myobj.getString("time_created"), myobj.getString("text"),myobj.getString("url"));
                listItems.add(listItem);
            }
            adapter = new ReviewAdapter(listItems, parent.getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getYelpRevHRate(View parent) {
        JSONObject gRev = MyAdapter.yelpRev;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        try {
            JSONArray myJsonArray = gRev.getJSONArray("reviews");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("rating");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });


            for (int i = myJsonArray.length()-1; i >= 0; i--) {
                txtv.setHeight(0);
                JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));

                reviewItem listItem = new reviewItem(myobj.getJSONObject("user").getString("image_url"), myobj.getJSONObject("user").getString("name"), myobj.getString("rating"), myobj.getString("time_created"), myobj.getString("text"),myobj.getString("url"));
                listItems.add(listItem);
            }
            adapter = new ReviewAdapter(listItems, parent.getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getGoogleRevHRate(ViewGroup parent) {

        JSONObject gRev = MyAdapter.responsePladeDet;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        JSONArray myJsonArray = null;
        try {
            myJsonArray = gRev.getJSONArray("results");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("rating");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });

            if(!gRev.getString("results").isEmpty()){
                try {
                    for (int i = myJsonArray.length()-1; i >= 0; i--) {
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 0;
                        txtv.setLayoutParams(params);
                        JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));
                        Date d = new Date(Integer.parseInt(myobj.getString("time")) * 1000L);
                        //SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD HH-MM-SS");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("Los Angeles"));

                        reviewItem listItem = new reviewItem(myobj.getString("profile_photo_url"), myobj.getString("author_name"), myobj.getString("rating"),formatter.format(d) , myobj.getString("text"),myobj.getString("author_url"));
                        listItems.add(listItem);

                    }
                    adapter = new ReviewAdapter(listItems, parent.getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

    private void getGoogleRevLRate(ViewGroup parent) {

        JSONObject gRev = MyAdapter.responsePladeDet;
        listItems = new ArrayList<>();
        RecyclerView recyclerView;
        TextView txtv = (TextView) parent.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) parent.findViewById(R.id.reviews_recycle);

        JSONArray myJsonArray = null;
        try {
            myJsonArray = gRev.getJSONArray("results");
            String[][] data = new String[myJsonArray.length()][2];
            for(int i=0;i<myJsonArray.length();i++){
                for(int j=0;j<1;j++){
                    JSONObject myobj = myJsonArray.getJSONObject(i);
                    data[i][j] = myobj.getString("rating");
                    data[i][j+1] = "" + i;
                }
            }

            Arrays.sort(data, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });

            if(!gRev.getString("results").isEmpty()){
                try {
                    for (int i = 0; i < myJsonArray.length(); i++) {
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
                        params.height = 0;
                        txtv.setLayoutParams(params);
                        JSONObject myobj = myJsonArray.getJSONObject(Integer.parseInt(data[i][1]));
                        Date d = new Date(Integer.parseInt(myobj.getString("time")) * 1000L);
                        //SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD HH-MM-SS");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("Los Angeles"));

                        reviewItem listItem = new reviewItem(myobj.getString("profile_photo_url"), myobj.getString("author_name"), myobj.getString("rating"),formatter.format(d) , myobj.getString("text"),myobj.getString("author_url"));
                        listItems.add(listItem);

                    }
                    adapter = new ReviewAdapter(listItems, parent.getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txtv.getLayoutParams();
            params.height = 1720;
            txtv.setLayoutParams(params);
            e.printStackTrace();
        }

    }

}
