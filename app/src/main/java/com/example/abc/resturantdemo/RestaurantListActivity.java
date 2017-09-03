package com.example.abc.resturantdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.abc.resturantdemo.adapter.RestaurantAdapter;
import com.example.abc.resturantdemo.dialog.AlertDialogManager;
import com.example.abc.resturantdemo.util_class.ApplicationData;
import com.example.abc.resturantdemo.json_parser.JSONParser;
import com.example.abc.resturantdemo.model.PlaceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {


    AlertDialogManager alertDialogManager;

    //PLACES DETAILS
    public static List<PlaceItem> mItems;
    String latitutde, longitude;

    RecyclerView recyclerView;

    //TAGS
    public static String KEY_REFERENCE = "Reference";
    public static String KEY_NAME = "Name";
    public static String KEY_ADDRESS = "Address";
    public static String KEY_TYPE = "Type";
    public static String KEY_PHOTOREFERENCE = "PhotoReference";
    public static String KEY_DISTANCE = "Distance";
    public static String KEY_LATITUDE = "Latitude";
    public static String KEY_LONGITUDE = "Longitude";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        alertDialogManager = new AlertDialogManager();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //Intent Data
        latitutde = getIntent().getStringExtra("LATITUDE");
        longitude = getIntent().getStringExtra("LONGITUDE");


        new AsyncTaskFetchPlaces(this, recyclerView).execute(latitutde, longitude, "" + 10000, "restaurant");


    }


    // async task.....


    class AsyncTaskFetchPlaces extends AsyncTask<String, String, String> {

        private Context activityContext;
        private JSONParser jsonParser = new JSONParser();
        private JSONParser jsonParserDistance = new JSONParser();
        private ProgressDialog progressDialog = null;
        private AlertDialogManager alert = new AlertDialogManager();
        private List<PlaceItem> mItems = new ArrayList<PlaceItem>();

        RestaurantAdapter restaurantAdapter;
        private RecyclerView recyclerView;

        public AsyncTaskFetchPlaces(Context activityContext, RecyclerView recyclerView) {
            this.activityContext = activityContext;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activityContext);
                progressDialog.setMessage("Loading  Nearby Restaurants");
                progressDialog.setCancelable(false);

            } else {
                progressDialog.setMessage("Loading  Nearby Restaurants");
                progressDialog.setCancelable(false);


            }
            progressDialog.show();
        }

        protected String doInBackground(String... args) {
            String placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef, rating, contact_detail;
            HashMap<String, String> params = new HashMap<>();
            params.put("location", args[0] + "," + args[1]);
            params.put("key", ApplicationData.ServerAPI);
            params.put("radius", args[2]);
            if (args[3].equals("ALL")) {
                Log.i("Places API Request", "Searching for all places.");
            } else {
                params.put("type", args[3]);
            }
            JSONObject jsonObject = jsonParser.makeHttpRequest(ApplicationData.PLACES_SEARCH_URL, "GET", params);
            Log.d("Result", jsonObject.toString());

            try {
                if (jsonObject.getString("status").equals("OK")) {
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    Log.d("resultsArray", resultsArray.toString());
                    if (resultsArray.length() > 0) {
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject jObj = resultsArray.getJSONObject(i);
                            Log.d("resultsObject: " + i, jObj.toString());
                            //LAT AND LNG
                            String geometry = jObj.getString("geometry");
                            JSONObject geometryObject = new JSONObject(geometry);
                            String location = geometryObject.getString("location");
                            JSONObject locationObject = new JSONObject(location);
                            placeLat = locationObject.getString("lat");
                            placeLng = locationObject.getString("lng");
                            Log.d("Location", placeLat + ", " + placeLng);


                            placeName = jObj.getString("name");


                            Log.d("Name", placeName);

                            placeType = jObj.getString("types");
                            Log.d("Type", placeType);

                            rating = jObj.getString("rating");
                            contact_detail = "";


                            placeVicinity = jObj.getString("vicinity");
                            Log.d("Address", placeVicinity);

                            HashMap<String, String> par = new HashMap<>();
                            par.put("origins", args[0] + "," + args[1]);

                            par.put("destinations", placeLat + "," + placeLng);//TODO:Change

                            par.put("key", ApplicationData.ServerAPI);
                            par.put("units", "metric");

                            JSONObject jsonObjectDistance = jsonParserDistance.makeHttpRequest(ApplicationData.PLACES_DISTANCE_URL, "GET", par);
                            JSONArray distanceArray = jsonObjectDistance.getJSONArray("rows");
                            JSONObject distanceObject = distanceArray.getJSONObject(0);
                            JSONArray distanceElement = distanceObject.getJSONArray("elements");
                            JSONObject elementObject = distanceElement.getJSONObject(0);

                            if (elementObject.getString("status").equals("OK")) {
                                String distance = elementObject.getString("distance");
                                JSONObject finalObject = new JSONObject(distance);
                                placeDistance = finalObject.getString("text");
                            } else {
                                placeDistance = "NA";
                            }
                            Log.d("Distance", placeDistance);

                            boolean hasPhoto;
                            try {
                                JSONArray photoArray = jObj.getJSONArray("photos");
                                Log.d("Photo Array", photoArray.toString());
                                JSONObject photoObject = photoArray.getJSONObject(0);
                                placePhotoRef = photoObject.getString("photo_reference");
                                hasPhoto = true;
                            } catch (Exception e) {
                                hasPhoto = false;
                            }

                            if (hasPhoto) {
                                JSONArray photoArray = jObj.getJSONArray("photos");
                                Log.d("Photo Array", photoArray.toString());
                                JSONObject photoObject = photoArray.getJSONObject(0);
                                placePhotoRef = photoObject.getString("photo_reference");
                            } else {
                                placePhotoRef = "Not Available";
                            }

                            Log.d("Photo Ref", placePhotoRef);

                            mItems.add(new PlaceItem(placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef, rating, contact_detail));

                        }
                    }
                } else if (jsonObject.getString("status").equals("ZERO_RESULTS")) {
                    alert.showAlertDialog(activityContext, "Near Places", "Sorry no places found.", false);
                }
            } catch (JSONException je) {

            }
            return "";
        }

        protected void onPostExecute(String file_url) {
            if (progressDialog.isShowing()) {

                restaurantAdapter = new RestaurantAdapter(mItems);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(activityContext);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(restaurantAdapter);
                restaurantAdapter.notifyDataSetChanged();
              //  progressDialog.hide();
                progressDialog.dismiss();
            }
        }
    }

}
