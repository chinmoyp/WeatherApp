package com.chinmoyxyz.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;


public class MainActivity extends ActionBarActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;
    ArrayList<String> resultList = null;
    private Location currentLocation;
    private GoogleApiClient mGoogleApiClient;
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.summaryLabel)  TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    GPSTracker gps;
    private Location mLastLocation;
//    @InjectView(R.id.locationChooser)
//    Button mLocationChooser;
//    @InjectView(R.id.mainView)  RelativeLayout MainView;
//    @InjectView(R.id.locationview) LinearLayout LocationView;
//    @InjectView(R.id.autocomplete)  AutoCompleteTextView autocompleteView;
//    @InjectView(R.id.locationTextView)  TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        mLastLocation =location;
                    }
                });
        if (mLastLocation != null) {
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getForecast(latitude, longitude);
                }
            });
            getForecast(latitude, longitude);
            Log.v(TAG, "Main Ui is running");
//        mLocationChooser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*dialog = new Dialog(getActivity());
//                dialog.setContentView(R.layout.locationdialog);
//                dialog.setTitle("Enter Your Location");
//                dialog.show();*/
//                        MainView.setVisibility(View.GONE);
//                        LocationView.setVisibility(View.VISIBLE);
//                        autocompleteView.setText("");
//                        autocompleteView.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//                                String input = autocompleteView.getText().toString();
//                                if(input.length() >= 2){
//                                    getAutoSuggestions(input);
//                                }
//                            }
//                        });
//
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, resultList);
//
//                        autocompleteView.setAdapter(arrayAdapter);
//
//                        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                String description = (String) parent.getItemAtPosition(position);
//                                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
//                                getLocationFromAddress(description);
//                                MainView.setVisibility(View.VISIBLE);
//                                LocationView.setVisibility(View.GONE);
//
//                            }
//                        });
//            }
//        });
//    }
//    private void getAutoSuggestions(String input){
//        RequestParams requestParams = new RequestParams();
//        requestParams.add("input", input);
//        requestParams.add("key", "AIzaSyBA6sVLI88KqUqIjper5nVX9zvVmJOBiXQ");
//        NetworkClient.autoComplete(this, requestParams, this);
//    }
//    public ArrayList<String> getPredictions(String responseJson) {
//        try {
//            Log.e("FULL", responseJson);
//            JSONObject object = new JSONObject(responseJson);
//            JSONArray jsonArray = object.getJSONArray("predictions");
//            resultList = new ArrayList<>(jsonArray.length());
//            for(int i=0; i<jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
////                Log.e("Address", jsonObject.getString("description"));
//                resultList.add(jsonObject.getString("description"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultList);
//        autocompleteView.setAdapter(arrayAdapter);
//        return resultList;
//
//    }
//    public LatLng getLocationFromAddress(String straddress){
//
//        Geocoder coder = new Geocoder(this);
//
//        List<Address> address;
//        LatLng latLng = null;
//
//        try{
//
//            address = coder.getFromLocationName(straddress, 5);
//            if(address == null){
//                return null;
//            }
//            Address location = address.get(0);
//            double latitudevalue = location.getLatitude();
//            double longitudevalue = location.getLongitude();
//
//
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(
//                    latitudevalue, longitudevalue, 2);
//            if(addresses != null){
//                try{
//                    setAddress(addresses.get(1));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//
//        } catch(Exception e){
//
//            e.printStackTrace();
//
//        }
//
//        return latLng;
//
//    }
//    public void getAddress() {
////        if (!CommonUtils.isNetworkAvailable(activity)) {
////            alertDialogHandler.showEnableInternetAlertDialog(activity, true);
////            return;
////        } else if (!CommonUtils.isLocationEnabled(activity)) {
////            alertDialogHandler.showEnableLocationAlertDialog(activity, false);
////            return;
////        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
//                && !Geocoder.isPresent()) {
//            return;
//        }
//        if (servicesConnected()) {
//            currentLocation = mLocationClient.getLastLocation();
//            new GetAddressTask(LocationServices.this).execute(currentLocation);
//        }
//    }
//    public void setAddress(Address address) {
//        if (address != null && locationTextView != null) {
//            // locationTextView.setText(address.getSubLocality() + ", "
//            // + address.getLocality());
//            locationTextView.setText(address.getSubLocality());
//        } else {
////            getAddress();
//        }
//    }
//
//    private boolean servicesConnected() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (ConnectionResult.SUCCESS == resultCode) {
//            return true;
//        } else {
//            return false;
//        }
        }
        }


    private void getForecast(double latitude,double longitude) {
        String apiKey = "d980259d2284b442574a4a55ffc53b81";

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude +"," + longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleRefresh();
                            }
                        });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleRefresh();
                            }
                        });
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                            Log.v(TAG, jsonData);
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Exception Caught", e);

                    }
                }
            });
        }else{
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getmTemperature()+"");
        mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        mHumidityValue.setText(mCurrentWeather.getmHumidity()+"");
        mPrecipValue.setText(mCurrentWeather.getmPrecipChance()+"");
        mSummaryLabel.setText(mCurrentWeather.getmSummary());
        mLocationLabel.setText(mCurrentWeather.getmTimeZone());
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From JSON :" +timezone);
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setmHumidity(currently.getDouble("humidity"));
        currentWeather.setmTime(currently.getLong("time"));
        currentWeather.setmIcon(currently.getString("icon"));
        currentWeather.setmSummary(currently.getString("summary"));
        currentWeather.setmTemperature(currently.getDouble("temperature"));
        currentWeather.setmTimeZone(timezone);
        Log.d(TAG,currentWeather.getFormattedTime());
        return currentWeather;


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
