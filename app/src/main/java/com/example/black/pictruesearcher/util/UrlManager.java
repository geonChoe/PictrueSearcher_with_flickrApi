package com.example.black.pictruesearcher.util;

import android.net.Uri;

public class UrlManager {

    public static final String API_KEY = "c9c1fdc5baba6a35643405def867e9b4";
    public static final String PREF_SEARCH_QUERY ="searchQuery";
    private static final String ENDPOINT = "https://secure.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static volatile UrlManager instance = null;

    private UrlManager() {
    }

    public static UrlManager getInstance() {
        if (instance == null) {
            synchronized (UrlManager.class) {
                if (instance == null) {
                    instance = new UrlManager();
                }
            }
        }
        return instance;
    }

    public static String getItemUrl(String keyword, int page) {
        String url;

        if (keyword != null) {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", keyword)
                    .appendQueryParameter("page", String.valueOf(page))
                    .appendQueryParameter("sort", "interestingness-desc")
//                    .appendQueryParameter("sort", "relevance")
                    .appendQueryParameter("content_type", "1")
                    .build().toString();
        } else {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GETRECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .appendQueryParameter("sort", "interestingness-desc")
//                    .appendQueryParameter("sort", "relevance")
                    .appendQueryParameter("content_type", "1")
                    .build().toString();
        }
        return url;
    }

}