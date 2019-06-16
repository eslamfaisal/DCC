package com.ibnsaad.thedcc.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.activities.OthersProfileActivity;
import com.ibnsaad.thedcc.adapter.UsersAdapterGridScrollProgress;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.network.RetrofitNetwork.BaseClient;
import com.ibnsaad.thedcc.utils.Tools;
import com.ibnsaad.thedcc.widget.SpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    public static DoctorsFragment doctorsFragment;
    private int item_per_display = 5;
    private int pageCount = 1;
    private UsersAdapterGridScrollProgress mAdapter;
    private RecyclerView recyclerView;
    private List<User> users;
    private String token;

    public DoctorsFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (doctorsFragment == null) {
            return new DoctorsFragment();
        } else return doctorsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageCount = 1;
        initComponent(view);
    }

    private void initComponent(View view) {
        users = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new UsersAdapterGridScrollProgress(getActivity(), item_per_display, users);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new UsersAdapterGridScrollProgress.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                loadNextData();
            }
        });

        mAdapter.setOnItemClickListener(new UsersAdapterGridScrollProgress.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String  obj, int position) {
                Intent intent = new Intent(getActivity(), OthersProfileActivity.class);
                intent.putExtra(Enums.ID.name(),obj);
                startActivity(intent);
            }
        });

        token = SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name());

        BaseClient.getApi().getUsers(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "onResponse: " + response.body().size());
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
//                        pageCount += 1;
                        mAdapter.setItems(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    private void loadNextData() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setLoading();
            }
        });

        BaseClient.getApi().getUsersPaging(token, pageCount, item_per_display).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        pageCount += 1;
                        mAdapter.insertData(response.body());
                    }
                });

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
