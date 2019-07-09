package com.ibnsaad.thedcc.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.activities.OthersProfileActivity;
import com.ibnsaad.thedcc.adapter.UsersAdapterGridScrollProgress;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.server.BaseClient;
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
public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";

    public static UsersFragment usersFragment;
    private Button last_active, newest_members, top_rating, low_rating;
    private int item_per_display = 15;
    private int pageCount = 1;
    private UsersAdapterGridScrollProgress mAdapter;
    private RecyclerView recyclerView;
    private List<User> users;
    private String token;
    private AppCompatSpinner spinnerSpecialization;
    private String specialization = "";
    private String orderBy = "";
    private int selectedItem = 0;
    private boolean doneLoadAllPages = false;

    public UsersFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (usersFragment == null) {
            return new UsersFragment();
        } else return usersFragment;
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
        recyclerView = view.findViewById(R.id.recyclerView);
        last_active = view.findViewById(R.id.last_active);
        newest_members = view.findViewById(R.id.newest_members);
        top_rating = view.findViewById(R.id.top_rating);
        low_rating = view.findViewById(R.id.low_rating);
        spinnerSpecialization = view.findViewById(R.id.spinner_specialization);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new UsersAdapterGridScrollProgress(getActivity(), item_per_display, users);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new UsersAdapterGridScrollProgress.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (!doneLoadAllPages)
                    loadNextData();
            }
        });

        mAdapter.setOnItemClickListener(new UsersAdapterGridScrollProgress.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                Intent intent = new Intent(getActivity(), OthersProfileActivity.class);
                intent.putExtra(Enums.ID.name(), obj);
                startActivity(intent);
            }
        });

        token = SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item_selected, specializationsList()) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position
                if (position == selectedItem) {
                    v.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                } else {
                    // for other views
                    v.setBackgroundColor(Color.WHITE);

                }
                return v;
            }
        };

        last_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBy = "lastActive";
                getPaginatesUsers();
            }
        });
        low_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBy = "lowrating";
                getPaginatesUsers();
            }
        });
        top_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBy = "rating";
                getPaginatesUsers();
            }
        });
        newest_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBy = "created";
                getPaginatesUsers();
            }
        });

        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(dataAdapter);
        spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                if (position == 0) {
                    specialization = "";
                } else
                    specialization = specializationsList().get(position);
                Log.d(TAG, "onItemSelected: " + specialization);
                getPaginatesUsers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (SharedHelper.getKey(getActivity(), Enums.UserType.name()).equals(getString(R.string.doctors))) {
            spinnerSpecialization.setVisibility(View.GONE);
        } else {
            spinnerSpecialization.setVisibility(View.VISIBLE);
        }
        getPaginatesUsers();
    }

    private void getPaginatesUsers() {
        doneLoadAllPages = false;
        mAdapter.clear();
        pageCount = 1;
        BaseClient.getApi().getUsersPaging(
                token,
                pageCount,
                item_per_display,
                orderBy,
                specialization
        ).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

//                Log.d(TAG, "onResponse: " + response.body().size());
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        pageCount += 1;
                        if (response.body() != null) {
                            mAdapter.setItems(response.body());
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

    }

    private List<String> specializationsList() {
        List<String> specializationsList = new ArrayList<>();
        specializationsList.add(getString(R.string.all));
        specializationsList.add(getString(R.string.bones_doctor));
        specializationsList.add(getString(R.string.heart_doctor));
        specializationsList.add(getString(R.string.internal_doctor));
        specializationsList.add(getString(R.string.dermatologist));
        specializationsList.add(getString(R.string.feminine_doctor));
        specializationsList.add(getString(R.string.pediatrician));
        return specializationsList;
    }

    private void loadNextData() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setLoading();
            }
        });

        BaseClient.getApi().getUsersPaging(
                token,
                pageCount,
                item_per_display,
                orderBy,
                specialization
        ).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body().size() <= 0)
                    doneLoadAllPages = true;
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
