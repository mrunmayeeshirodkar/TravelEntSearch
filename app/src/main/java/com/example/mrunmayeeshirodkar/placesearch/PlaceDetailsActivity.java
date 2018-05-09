package com.example.mrunmayeeshirodkar.placesearch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.net.URI;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private SectionsPagerAdapter mySectionsPagerAdapter;
    private GeoDataClient mGeoDataClient;
    public Bitmap[] myPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        TabLayout details_tab = (TabLayout) findViewById(R.id.details_tabs);

        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_info, null);
        drawable1.setBounds(0, 0, 65, 65);
        ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BOTTOM);
        SpannableString spannableString1 = new SpannableString("  INFO");
        spannableString1.setSpan(imageSpan1, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        details_tab.getTabAt(0).setText(spannableString1);


        Drawable drawable2 = getResources().getDrawable(R.drawable.ic_photos, null);
        drawable2.setBounds(0, 0, 65, 65);
        ImageSpan imageSpan2 = new ImageSpan(drawable2);
        SpannableString spannableString2 = new SpannableString("  PHOTOS");
        spannableString2.setSpan(imageSpan2, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        details_tab.getTabAt(1).setText(spannableString2);

        Drawable drawable3 = getResources().getDrawable(R.drawable.ic_maps, null);
        drawable3.setBounds(0, 0, 65, 65);
        ImageSpan imageSpan3 = new ImageSpan(drawable3);
        SpannableString spannableString3 = new SpannableString("  MAP");
        spannableString3.setSpan(imageSpan3, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        details_tab.getTabAt(2).setText(spannableString3);

        Drawable drawable4 = getResources().getDrawable(R.drawable.ic_reviews, null);
        drawable4.setBounds(0, 0, 65, 65);
        ImageSpan imageSpan4 = new ImageSpan(drawable4);
        SpannableString spannableString4 = new SpannableString("  REVIEWS");
        spannableString4.setSpan(imageSpan4, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        details_tab.getTabAt(3).setText(spannableString4);

        for (int i = 0; i < 3; i++) {
            View root = details_tab.getChildAt(i);
            if (root instanceof LinearLayout) {
                ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                GradientDrawable drawable5 = new GradientDrawable();
                drawable5.setColor(getResources().getColor(R.color.lightgreen));
                drawable5.setSize(2, 1);
                ((LinearLayout) root).setDividerPadding(30);
                ((LinearLayout) root).setDividerDrawable(drawable5);
            }
        }


        Intent intent = getIntent();

        mySectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mySectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(details_tab));
        details_tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TextView txtview = (TextView) findViewById(R.id.place_name);
        ImageView imgH = (ImageView) findViewById(R.id.favorites);

        try {
            txtview.setText(MyAdapter.responsePladeDet.getString("name"));
            SharedPreferences prefs =  getSharedPreferences("Place_Id_Array", 0);
            if(prefs.contains(MyAdapter.responsePladeDet.getString("place_id"))){
                imgH.setImageResource(R.drawable.white_filled_star);
            } else {
                imgH.setImageResource(R.drawable.heart_outline_white);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sharethisitem(View v){

        String url = null;
        try {
            Log.d("MainActivity",MyAdapter.responsePladeDet.getString("website"));

            url = "https://twitter.com/share?text=Check%20out%20"+ Uri.encode(MyAdapter.responsePladeDet.getString("name")) + "%20located%20at%20" + Uri.encode(MyAdapter.responsePladeDet.getString("formatted_address")) + "%0AWebsite:%20" + Uri.encode(MyAdapter.responsePladeDet.getString("website"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Log.d("MainActivity",uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void addToFavorites(View v){

        SharedPreferences prefs = v.getContext().getSharedPreferences("Place_Id_Array", v.getContext().MODE_PRIVATE);
        ImageView heartImg = (ImageView) v.findViewById(R.id.favorites);
        String placeID = null;
        String name = null;
        String icon = null;
        String address = null;
        try {
            placeID = MyAdapter.responsePladeDet.getString("place_id");
            name = MyAdapter.responsePladeDet.getString("name");
            icon = MyAdapter.responsePladeDet.getString("icon");
            address = MyAdapter.responsePladeDet.getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(prefs.getAll().isEmpty()){
            try {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(MyAdapter.responsePladeDet.getString("place_id") ,"0");
                editor.commit();
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(placeID,0);
                if(sharedPreferences.getAll().isEmpty()){
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putString("icon",icon);
                    editor1.putString("name",name);
                    editor1.putString("address",address);
                    editor1.commit();
                }
                Toast.makeText(this,MyAdapter.responsePladeDet.getString("name") + " was added to favorites.",Toast.LENGTH_SHORT).show();
                heartImg.setImageResource(R.drawable.white_filled_star);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();

            if(prefs.contains(placeID)){
                editor.remove(placeID);
                editor.commit();
                try {
                    Toast.makeText(this,MyAdapter.responsePladeDet.getString("name") + " was removed from favorites.",Toast.LENGTH_SHORT).show();
                    @SuppressLint({"NewApi", "LocalSuppress"}) Boolean k = v.getContext().deleteSharedPreferences(placeID);
                    if(k){
                        Toast.makeText(v.getContext(),name + " was removed from favorites.",Toast.LENGTH_SHORT).show();
                    }
                    Integer indexofplace = -1;

                    if(MyFavAdapter.listItems != null){
                        List<FavListItem> list = MyFavAdapter.listItems;

                        for(int i =0; i<list.size();i++){
                            FavListItem listItemd = list.get(i);
                            if(listItemd.getPlaceId().equals(placeID)){
                                indexofplace = i;
                            }
                        }

                        if(indexofplace > -1){
                            Log.d("MainActivity",list.get(indexofplace) + "Deleted");
                            if(list.remove(list.get(indexofplace))){
                                //Log.d("MainActivity",listItems.get(indexofplace) + "This is Next Item");
                            }
                            MyFavAdapter.listItems = list;
                        }

                    }

                    heartImg.setImageResource(R.drawable.heart_outline_white);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    editor.putString(MyAdapter.responsePladeDet.getString("place_id") , prefs.getAll().size() + "");
                    editor.commit();
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(placeID,0);
                    if(sharedPreferences.getAll().isEmpty()){
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("icon",icon);
                        editor1.putString("name",name);
                        editor1.putString("address",address);
                        editor1.commit();
                    }
                    Toast.makeText(this,MyAdapter.responsePladeDet.getString("name") + " was added to favorites.",Toast.LENGTH_SHORT).show();
                    heartImg.setImageResource(R.drawable.white_filled_star);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.d("MainActivity",prefs.getAll().toString());
        }

    }

    public void onBackPressed(View v){
        onBackPressed();
    }

    public void startGooglePage(View v){
        TextView murl = (TextView)findViewById(R.id.google_page);
        Uri uri = Uri.parse(murl.getText().toString()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void startWebsite(View v){
        TextView murl = (TextView)findViewById(R.id.website);
        Uri uri = Uri.parse(murl.getText().toString()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void startDialIntent(View v){
        TextView mNumber = (TextView)findViewById(R.id.phone_number);
        String phone = mNumber.getText().toString();

        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", phone, null));
        startActivity(phoneIntent);
    }

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
                    tab1info tab1 = new tab1info();
                    return tab1;
                case 1:
                    tab2photos tab2 = new tab2photos();
                    return tab2;
                case 2:
                    tab3map tab3 = new tab3map();
                    return tab3;
                case 3:
                    tab4reviews tab4 = new tab4reviews();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }


    }

}
