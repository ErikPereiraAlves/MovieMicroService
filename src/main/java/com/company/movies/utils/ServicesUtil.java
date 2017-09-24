package com.company.movies.utils;


public class ServicesUtil {

    public static final String LINEBREAK = "\r\n";

    public static final String BASE_URI = "/movie";

    public static final String VERSION_URI = "/v1";

    public static final String FETCH = "/fetch";

    public static final String PERSIST = "/persist";

    public static final String DETAILS_URI = "/details";

    public static final String ALL_URI = "/all";

    public static final String COMMENTS_URI = "/comments";


    public static enum ServicesAvailable {
        DETAILS_FETCH,
        ALL_DETAILS_FETCH,
        DETAILS_PERSIST,
        COMMENTS_PERSIST;
    }


}
