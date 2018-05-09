package com.example.mrunmayeeshirodkar.placesearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter {
    private Context mcontext;
    public Bitmap[] myPhotos = MyAdapter.myPhotos;

    public ImageAdapter(Context c){
        mcontext = c;
    }

    @Override
    public int getCount() {
        return myPhotos.length;
    }

    @Override
    public Object getItem(int position) {
        return myPhotos[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mcontext);
        int hri = myPhotos[position].getHeight() * (1500/myPhotos[position].getWidth());
        Bitmap b2 = Bitmap.createScaledBitmap(myPhotos[position],1350,hri,false);
        imageView.setImageBitmap(b2);
        return imageView;
    }
}
