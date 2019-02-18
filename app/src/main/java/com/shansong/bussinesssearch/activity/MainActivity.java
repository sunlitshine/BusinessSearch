package com.shansong.bussinesssearch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.shansong.bussinesssearch.R;
import com.shansong.bussinesssearch.adapter.BusinessListRecyclerViewAdapter;
import com.shansong.bussinesssearch.exception.CommunicationException;
import com.shansong.bussinesssearch.model.Business;
import com.shansong.bussinesssearch.model.SearchResponse;
import com.shansong.bussinesssearch.net.Http;
import com.shansong.bussinesssearch.utils.AppConstants;
import com.shansong.bussinesssearch.utils.AppUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        BusinessListRecyclerViewAdapter.RecyclerItemViewHolderClickListener, LocationListener {

    private final String TAG = MainActivity.class.getName();
    private AppCompatEditText mSearchEditText;
    private AppCompatButton mSearchButton;
    private HashMap<String, String> mRequestParams;
    private RecyclerView mBusiListRecyclerView;
    private BusinessListRecyclerViewAdapter mBusiListRecyclerViewAdapter;

    private LocationManager mLManager;
    private Location mLocation;
    private boolean mLocationPermissionGranted;

    private Business[] mBusinessList;
    private String mSearchWord;
    private int mTotalToSearch = AppConstants.SEARCH_LIMIT;
    private boolean showProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = findViewById(R.id.searchText);
        mSearchButton = findViewById(R.id.search_button);
        mBusiListRecyclerView = findViewById(R.id.busiListScreen_layout_recycler_view);
        setupRecyclerViewAdapter();

        mBusiListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //direction integers: -1 for up, 1 for down
                if (!recyclerView.canScrollVertically(1)) {
                    //if the current result is the same as the limit, we can continue the search,
                    // otherwise we consider there are no more result from the server
                    if(mBusiListRecyclerViewAdapter.getItemCount() == mTotalToSearch){
                        mTotalToSearch = mTotalToSearch + AppConstants.SEARCH_LIMIT;
                        showProgressBar = false;
                        startSearch();
                    }
                }
            }
        });

        mSearchButton.setOnClickListener(this);
        mLManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handleLocationPermission();
    }

    @Override
    public void onClick(View view) {
        final String searchContent = mSearchEditText.getText().toString();
        if(!TextUtils.isEmpty(searchContent)){

            mSearchWord = searchContent;
            mTotalToSearch = AppConstants.SEARCH_LIMIT;
            showProgressBar = true;
            startSearch();
        }
    }


    @Override
    public void onRecyclerViewHolderItemClicked(View view, int position) {
        Business business = mBusinessList[position];
        String busiUrl = business.getUrl();
        if(!TextUtils.isEmpty(busiUrl)){
            Uri uriUrl = Uri.parse(business.getUrl());
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    private void startSearch(){
        Log.d(TAG, "startSearch()");

        AppUtils.hideKeyboard(this);
        getDeviceLocation();
        mRequestParams = new HashMap<>();
        mRequestParams.put(AppConstants.REQUEST_PARAM_TERM, mSearchWord);
        mRequestParams.put(AppConstants.REQUEST_PARAM_LIMIT, String.valueOf(mTotalToSearch));

        if(mLocation != null){
            mRequestParams.put(AppConstants.BUSINESS_LONGITUDE_KEY, String.valueOf(mLocation.getLongitude()));
            mRequestParams.put(AppConstants.BUSINESS_LATITUDE_KEY, String.valueOf(mLocation.getLatitude()));

        }else{
            mRequestParams.put(AppConstants.REQUEST_PARAM_LOCATION, "montreal");
        }

        BusinessSearchAsyncTask mBsAsyncTask = new BusinessSearchAsyncTask();
        mBsAsyncTask.execute();

    }

    /**
     * Class is responsible to perform Search
     */
    private class BusinessSearchAsyncTask extends AsyncTask<String, Void, Object> {

        SearchResponse response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(showProgressBar){
                showProgressBar("Searching");
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            Log.d(TAG, "doInBackground()");
            try {
                final Http mHttp = new Http(mRequestParams, 5000);
                response = mHttp.doHttpRequest();
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute()");

            if(result != null && response != null){

                mBusinessList = response.getBusinessSearchResult();
                if(mBusinessList != null){
                    mBusiListRecyclerViewAdapter.updateList(mBusinessList);

                    if(mBusinessList.length == 0){
                        AppUtils.getAlertDialog(MainActivity.this,
                                "Oops",
                                "No result found! Please try another word").show();
                    }
                }
            }
            hideProgressBar();
        }
    }


    private void setupRecyclerViewAdapter(){
        // prepare recycler view
        mBusiListRecyclerViewAdapter = new BusinessListRecyclerViewAdapter(this);

        // set phone 1D grid layout for business list
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

        mBusiListRecyclerView.setLayoutManager(gridLayoutManager);
        mBusiListRecyclerView.setAdapter(mBusiListRecyclerViewAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case AppConstants.GPS_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                mLocation = mLManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.GPS_PERMISSION_REQUEST_CODE);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void handleLocationPermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mLocation = mLManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.GPS_PERMISSION_REQUEST_CODE);
        }
    }

    private void showProgressBar(String progressMessage){
        View view = findViewById(R.id.search_layout_progress_bar);
        view.setVisibility(View.VISIBLE);

        TextView loadingScreenText = findViewById(R.id.textview_loadingscreen);
        loadingScreenText.setText(progressMessage);
    }

    private void hideProgressBar(){
        View view = findViewById(R.id.search_layout_progress_bar);
        if (view != null)
            view.setVisibility(View.GONE);
    }

}
