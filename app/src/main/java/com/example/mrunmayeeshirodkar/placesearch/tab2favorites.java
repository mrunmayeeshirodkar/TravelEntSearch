package com.example.mrunmayeeshirodkar.placesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tab2favorites extends Fragment {

    public List<FavListItem> listItems;

    public RecyclerView recyclerView;
    public static View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2ffavorites, container, false);

        TextView section_label = (TextView) rootView.findViewById(R.id.section_label);
        Context context = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.favorites_rec);

            SharedPreferences prefs = context.getSharedPreferences("Place_Id_Array", context.MODE_PRIVATE);
            if(prefs.getAll().isEmpty()){
                section_label.setText("No Favorites");
            }
            else {
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) section_label.getLayoutParams();
                params.height = 0;
                section_label.setLayoutParams(params);
                showFavList(container);
            }

        return rootView;
    }

    public static void showText(){
        TextView txt = (TextView) rootView.findViewById(R.id.section_label);
        txt.setText("No Favorites");
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) txt.getLayoutParams();
        params.height = 1900;
        txt.setLayoutParams(params);
    }

    public void showFavList(ViewGroup container) {

    listItems = new ArrayList<>();
    Context context = container.getContext();
    SharedPreferences prefs =  context.getSharedPreferences("Place_Id_Array", context.MODE_PRIVATE);
    Map<String,?> keys = prefs.getAll();

    for(Map.Entry<String,?> entry : keys.entrySet()){
        SharedPreferences onepref =  context.getSharedPreferences(entry.getKey(), context.MODE_PRIVATE);
        String name = onepref.getString("name","null");
        String icon = onepref.getString("icon","null");
        String place_id = entry.getKey();
        String address = onepref.getString("address","null");
        FavListItem listItem = new FavListItem(name,address,icon,place_id);
        listItems.add(listItem);
    }

    MyFavAdapter adapter = new MyFavAdapter(listItems,container.getContext());
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(adapter);

    }

}