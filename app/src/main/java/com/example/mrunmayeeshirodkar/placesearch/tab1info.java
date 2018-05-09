package com.example.mrunmayeeshirodkar.placesearch;

import android.app.ActionBar;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class tab1info extends Fragment {

    private JSONObject responsePladeDet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1iinfo, container, false);
        responsePladeDet = MyAdapter.responsePladeDet;



        try {

            TableLayout table = (TableLayout) rootView.findViewById(R.id.table);
            //checking for address
            if(!responsePladeDet.getString("formatted_address").equals("NULL")){
                TextView address = rootView.findViewById(R.id.address);
                address.setText(responsePladeDet.getString("formatted_address"));
            } else {
                TableRow row1 = (TableRow) rootView.findViewById(R.id.row1);
                table.removeView(row1);
            }

            //checking for phone number
            if(!responsePladeDet.getString("formatted_phone_number").equals("NULL")){
                TextView phone_number = rootView.findViewById(R.id.phone_number);
                phone_number.setPaintFlags(phone_number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                phone_number.setText(responsePladeDet.getString("formatted_phone_number"));
            } else {
                TableRow row2 = (TableRow) rootView.findViewById(R.id.row2);
                table.removeView(row2);
            }

            //checking for Price Level
            if(!responsePladeDet.getString("price_level").equals("NULL")){
                TextView price_level = rootView.findViewById(R.id.price_level);
                String price = "";
                for(int i = 0;i<Integer.parseInt(responsePladeDet.getString("price_level"));i++){
                    price = price + "$";
                }
                price_level.setText(price);
            } else {
                TableRow row3 = (TableRow) rootView.findViewById(R.id.row3);
                table.removeView(row3);
            }

            //checking for rating
            if(!responsePladeDet.getString("rating").equals("NULL")){
                RatingBar rating = rootView.findViewById(R.id.ratingBar);
                rating.setRating(Float.parseFloat(responsePladeDet.getString("rating")));
            } else {
                TableRow row4 = (TableRow) rootView.findViewById(R.id.row4);
                table.removeView(row4);
            }

            //checking for Google Link
            if(!responsePladeDet.getString("google_page").equals("NULL")){
                TextView google_page = rootView.findViewById(R.id.google_page);
                google_page.setPaintFlags(google_page.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                google_page.setText(responsePladeDet.getString("google_page"));
            } else {
                TableRow row5 = (TableRow) rootView.findViewById(R.id.row5);
                table.removeView(row5);
            }

            //checking for Website
            if(!responsePladeDet.getString("website").equals("NULL")){
                TextView website = rootView.findViewById(R.id.website);
                website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                website.setText(responsePladeDet.getString("website"));
            } else {
                TableRow row6 = (TableRow) rootView.findViewById(R.id.row6);
                table.removeView(row6);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
