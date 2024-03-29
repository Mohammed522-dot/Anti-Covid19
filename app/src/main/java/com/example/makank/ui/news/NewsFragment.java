package com.example.makank.ui.news;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.makank.Alert;
import com.example.makank.LoadingDialog;
import com.example.makank.R;
import com.example.makank.data.network.ApiClient;
import com.example.makank.data.network.ApiInterface;
import com.example.makank.adapter.NewsAdapter;
import com.example.makank.data.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {
    SearchView searchView;
    private TextView notfound;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    LoadingDialog loadingDialog;
    Alert alert;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new Alert(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView =  view.findViewById(R.id.recycler_news);
        notfound = view.findViewById(R.id.not_fond);
        notfound.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter();
        recyclerView.setAdapter(newsAdapter);
        final ProgressDialog progressDoalog;

       loadingDialog.startLoadingDialog();
        ApiInterface apiService = ApiClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiService.getNews();

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
               // progressDoalog.dismiss();
                loadingDialog.dismissDialog();
                newsList = response.body();
                if (!newsList.isEmpty()) {

                    Log.d("TAG", "Response = " + newsList);
                    newsAdapter.setMovieList(getContext(), newsList);
                }else
                    notfound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
            //    progressDoalog.dismiss();
                loadingDialog.dismissDialog();
                Log.d("TAG","Response = "+t.toString());
            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_item, menu);

        SearchManager searchManager2 = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager2
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                newsAdapter.getFilter().filter(query);
                return false;
            }
        });
    }
}
