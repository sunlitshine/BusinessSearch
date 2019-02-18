package com.shansong.bussinesssearch.utils;

public class AppConstants {

    //Yelp server
    public static final String URL = "https://api.yelp.com/v3/businesses/search?";
    public static final String API_KEY = "Bearer oT3pjjW0JpIuEJ-_003lLHTwoDCSLzbVZKug7BRTxbW9slmIep3yH_HncyQV_zPGt3zits6vk7_eQVqN0JEkZ1KW5IF5TbOCwn-1tf5B2-enx60jDC6qs1D7igL2W3Yx";

    //Request params
    public static final String REQUEST_PARAM_TERM = "term";
    public static final String REQUEST_PARAM_LOCATION = "location";
    public static final String REQUEST_PARAM_AUTHENTICATION = "Authorization";
    public static final String REQUEST_PARAM_LIMIT = "limit";

    public static final int SEARCH_LIMIT = 10;

    // Response
    public static final String BUSINESS_KEY = "businesses";
    public static final String BUSINESS_NAME_KEY = "name";
    public static final String BUSINESS_URL = "url";
    public static final String BUSINESS_IMAGE_URL_KEY = "image_url";
    public static final String BUSINESS_IS_CLOSED_KEY = "is_closed";
    public static final String BUSINESS_RATING_KEY = "rating";
    public static final String BUSINESS_LATITUDE_KEY = "latitude";
    public static final String BUSINESS_LONGITUDE_KEY = "longitude";
    public static final String BUSINESS_DISTANCE_KEY = "distance";

    //permission
    public static final int GPS_PERMISSION_REQUEST_CODE = 101;




}
