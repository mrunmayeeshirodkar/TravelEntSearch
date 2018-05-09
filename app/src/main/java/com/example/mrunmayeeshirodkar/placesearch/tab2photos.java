package com.example.mrunmayeeshirodkar.placesearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class tab2photos extends Fragment {

    private Bitmap[] myPhotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2pphotos, container, false);
        myPhotos = MyAdapter.myPhotos;
        GridView gridView = (GridView) rootView.findViewById(R.id.photosview);
        TextView txt = rootView.findViewById(R.id.no_photos);
        if(myPhotos == null){
            gridView.setVisibility(View.INVISIBLE);
            txt.setText("No Photos");
            Log.d("MainActivity","No Photos");
        } else {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txt.getLayoutParams();
            params.height = 0;
            txt.setLayoutParams(params);
            gridView.setAdapter(new ImageAdapter(rootView.getContext()));
        }
        Log.d("MainActivity",   "Showing the view ");
        return rootView;
    }
}
