package com.shansong.bussinesssearch.net;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.shansong.bussinesssearch.exception.CommunicationException;
import com.shansong.bussinesssearch.model.SearchResponse;
import com.shansong.bussinesssearch.utils.AppConstants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Http {
    private final String TAG = Http.class.getName();

    private static HashMap<String, String> mRequestHeaderFields;
    private int mTimeout;


    public Http(HashMap<String, String> requestHeaderFields, int timeout) {
        this.mTimeout = timeout;
        this.mRequestHeaderFields = requestHeaderFields;
    }


    public SearchResponse doHttpRequest()
            throws CommunicationException {

        // Do the GET
        HttpURLConnection urlConnection = null;
        SearchResponse response;

        try {
            final URL url = new URL(AppConstants.URL+getQueryParams());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(mTimeout);
            urlConnection.setReadTimeout(mTimeout);
            urlConnection.setRequestProperty(AppConstants.REQUEST_PARAM_AUTHENTICATION, AppConstants.API_KEY);

            response = handleServerResponse(urlConnection);

            return response;
        } catch (UnsupportedEncodingException uee) {
            throw new CommunicationException(uee.getMessage());
        } catch (IOException ioe) {
            throw new CommunicationException(ioe.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String getQueryParams(){
        Uri.Builder builder = new Uri.Builder();

        for(String key: mRequestHeaderFields.keySet()){
            Log.d(TAG, "kEY: "+ key +"   Value: "+ mRequestHeaderFields.get(key));
            builder.appendQueryParameter(key, mRequestHeaderFields.get(key));
        }

        return builder.build().getEncodedQuery();
    }


    private SearchResponse handleServerResponse(HttpURLConnection urlConnection) throws CommunicationException {
        InputStream inputStream = null;
        BufferedReader inputStreamReader = null;
        try {
            int httpStatusCode = urlConnection.getResponseCode();

            Log.i(TAG, "Http status code " + httpStatusCode);
            Log.i(TAG, "Response msg: " + urlConnection.getResponseMessage());

            switch (httpStatusCode) {
                case HttpURLConnection.HTTP_OK:
                    inputStream = urlConnection.getInputStream();
                    inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputStr = "";
                    StringBuffer responseBuffer = new StringBuffer();
                    while (inputStr != null) {
                        responseBuffer.append(inputStr);
                        inputStr = inputStreamReader.readLine();
                    }

                    if(TextUtils.isEmpty(responseBuffer.toString())){
                        return new SearchResponse(SearchResponse.STATUS.SUCCESS);
                    }else{
                        return new SearchResponse(SearchResponse.STATUS.SUCCESS, new JSONObject(responseBuffer.toString()));
                    }

                default:
                    return new SearchResponse(SearchResponse.STATUS.FAILED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "IOException " + e.getMessage());
            throw new CommunicationException(e.getMessage());
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            }
            catch (IOException e) {
                Log.e(TAG, "Unable to close the inputStream stream in the finally block. Simply skip it...");
            }
        }
    }

}
