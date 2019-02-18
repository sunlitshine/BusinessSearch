package com.shansong.bussinesssearch.model;

import android.util.Log;

import com.shansong.bussinesssearch.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResponse
{

    private final String TAG = SearchResponse.class.getName();
    private JSONObject mJSONObject;
    private STATUS status;

    public static enum STATUS {
        SUCCESS,  FAILED;

        private STATUS() {}
    }


    public SearchResponse(STATUS status)
    {
        this.status = status;
    }


    public SearchResponse(STATUS status, JSONObject jsonObject) {
        this.status = status;
        this.mJSONObject = jsonObject;
    }

    public STATUS getStatus(){
        return status;
    }

    public Business[] getBusinessSearchResult(){
        Log.d(TAG, "getBusinessSearchResult()");
        Business[] businessList = null;
        try{
            if(STATUS.SUCCESS == getStatus() && mJSONObject != null){

                if(mJSONObject.has(AppConstants.BUSINESS_KEY)){
                    JSONArray resultArray = mJSONObject.getJSONArray(AppConstants.BUSINESS_KEY);
                    businessList = new Business[resultArray.length()];
                    Log.d(TAG, "result size: "+ resultArray.length());

                    for(int i =0; i< resultArray.length(); i++){
                        Business business = new Business();
                        JSONObject result = resultArray.getJSONObject(i);

                        if(result.has(AppConstants.BUSINESS_IMAGE_URL_KEY)){
                            business.setImage_url(result.getString(AppConstants.BUSINESS_IMAGE_URL_KEY));
                        }

                        if(result.has(AppConstants.BUSINESS_NAME_KEY)){
                            business.setName(result.getString(AppConstants.BUSINESS_NAME_KEY));
                        }

                        if(result.has(AppConstants.BUSINESS_URL)){
                            business.setUrl(result.getString(AppConstants.BUSINESS_URL));
                        }


                        if(result.has(AppConstants.BUSINESS_IS_CLOSED_KEY)){
                            business.setClosed(result.getBoolean(AppConstants.BUSINESS_IS_CLOSED_KEY));
                        }

                        if(result.has(AppConstants.BUSINESS_RATING_KEY)){
                            business.setRating(result.getDouble(AppConstants.BUSINESS_RATING_KEY));
                        }

                        if(result.has(AppConstants.BUSINESS_DISTANCE_KEY)){
                            business.setDistance(result.getDouble(AppConstants.BUSINESS_DISTANCE_KEY));
                        }

                        businessList[i] = business;

                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG, "Exception thrown with message: "+ e.getMessage());
        }

        return businessList;

    }

}
