
package com.example.mrunmayeeshirodkar.placesearch;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This is taken from a github repository
 * Adapter that handles Autocomplete requests from the Places Geo Data Client.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 */
public class PlaceAutocompleteAdapter
        extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> mResultList;

    /**
     * Handles autocomplete requests.
     */
    private GeoDataClient mGeoDataClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter mPlaceFilter;

    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see android.widget.ArrayAdapter#ArrayAdapter(android.content.Context, int)
     */
    public PlaceAutocompleteAdapter(Context context, GeoDataClient geoDataClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, R.layout.listlayoutforsug, android.R.id.text1);
        mGeoDataClient = geoDataClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return mResultList.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        //if (convertView == null) {



            //view = inflater.inflate(R.layout.autocomplete_list_item, null);

        //Log.d("MainActivity","" + mResultList.size());

        //}
        //else {
        //    view = convertView;
        //}
        AutocompletePrediction item = getItem(position);
        if (position != (mResultList.size() - 1)) {
            TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
            textView1.setText(item.getFullText(null));
            TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView2.getLayoutParams();
            params.height = 0;
            textView2.setLayoutParams(params);

        }
        else {
            TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
            textView1.setText(item.getFullText(null));
            TextView textView2 = (TextView) row.findViewById(android.R.id.text2);

            Drawable drawable = row.getResources().getDrawable(R.drawable.powered_by_google_light);
            drawable.setBounds(0,0,500,55);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString("                       ");
            spannableString.setSpan(imageSpan,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView2.getLayoutParams();
            params.height = 150;
            textView2.setLayoutParams(params);
            textView2.setPadding(200,20,0,50);
            textView2.setText(spannableString);
        }




        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // We need a separate list to store the results, since
                // this is run asynchronously.
                ArrayList filterData = new ArrayList<>();

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = getAutocomplete(constraint.toString());
                    Log.d("MainActivity", filterData.toString());
                    //filterData.add("footer");
                }

                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mResultList = (ArrayList<AutocompletePrediction>) results.values;
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see GeoDataClient#getAutocompletePredictions(String, LatLngBounds, AutocompleteFilter)
     * @see AutocompletePrediction#freeze()
     */
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        Log.i("MainActivity", "Starting autocomplete query for: " + constraint);

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.

        AutocompleteFilter mPlaceFilter = new AutocompleteFilter.Builder()
                .setCountry("USA")
                //.setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();
        Task<AutocompletePredictionBufferResponse> results =
                mGeoDataClient.getAutocompletePredictions(constraint.toString(), mBounds,
                        mPlaceFilter);

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        try {
            AutocompletePredictionBufferResponse autocompletePredictions = results.getResult();

            Log.i("MainActivity", "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        } catch (RuntimeExecutionException e) {
            // If the query did not complete successfully return null
            Toast.makeText(getContext(), "Error contacting API: " + e.toString(),
                    Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error getting autocomplete prediction API call", e);
            return null;
        }
    }
}
