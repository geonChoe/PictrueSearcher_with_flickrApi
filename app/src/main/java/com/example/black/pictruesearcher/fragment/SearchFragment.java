package com.example.black.pictruesearcher.fragment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.black.pictruesearcher.R;
import com.example.black.pictruesearcher.model.SearchItem;
import com.example.black.pictruesearcher.util.SearchAdapter;
import com.example.black.pictruesearcher.util.UrlManager;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomSwipeRefreshLayout customSwipeRefreshLayout;
    private SearchAdapter searchAdapter;

    private boolean isLoad = false;
    private boolean isMore = true;

    private SearchView searchView;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        recyclerView =  view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        searchAdapter = new SearchAdapter(getActivity(), new ArrayList<SearchItem>());
        recyclerView.setAdapter(searchAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int size = gridLayoutManager.getItemCount();
                int lastIdx = gridLayoutManager.findLastVisibleItemPosition();
                if (isMore && !isLoad && size-1 != lastIdx) {
                    startLoading();
                }
            }
        });

        customSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        customSwipeRefreshLayout.setOnRefreshListener(
                new CustomSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );

        startLoading();
        return view;
    }

    public void refresh() {
        searchAdapter.clear();
        startLoading();
    }

    private void startLoading() {

        isLoad = true;
        final int page = gridLayoutManager.getItemCount() / 101;

        String keywrod = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(UrlManager.PREF_SEARCH_QUERY, null);
        String url = UrlManager.getInstance().getItemUrl(keywrod, page);

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<SearchItem> result = new ArrayList<>();
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            if (photos.getInt("pages") == page) {
                                isMore = false;
                            }
                            JSONArray photoArr = photos.getJSONArray("photo");
                            for (int i = 0; i < photoArr.length(); i++) {
                                JSONObject itemObj = photoArr.getJSONObject(i);
                                SearchItem item = new SearchItem(
                                        itemObj.getString("id"),
                                        itemObj.getString("secret"),
                                        itemObj.getString("server"),
                                        itemObj.getString("farm")
                                );
                                result.add(item);
                            }
                        } catch (JSONException e) {
                        }
                        searchAdapter.addAll(result);
                        searchAdapter.notifyDataSetChanged();
                        isLoad = false;
                        customSwipeRefreshLayout.refreshComplete();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        request.setTag("tag");
        requestQueue.add(request);
    }

    private void stopLoading() {
        if (requestQueue != null) {
            requestQueue.cancelAll("tag");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        searchView.setSearchableInfo(searchInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled = false;
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                selectionHandled = true;
                break;
            default:
                selectionHandled = super.onOptionsItemSelected(item);
                break;
        }
        return selectionHandled;
    }
}