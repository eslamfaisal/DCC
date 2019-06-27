package com.ibnsaad.thedcc.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.BodyAreasResponse;
import com.ibnsaad.thedcc.model.BulletinResponse;
import com.ibnsaad.thedcc.model.DrugsResponse;
import com.ibnsaad.thedcc.server.BaseClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosisFragment extends Fragment {

    public static DiagnosisFragment diagnosisFragment;
    private final String TAG = "DiagnosisFragment";
    private List<BodyAreasResponse> bodyAreasResponseList = new ArrayList<>();
    private List<DrugsResponse> drugsResponseArrayList = new ArrayList<>();
    private List<BulletinResponse> bulletinResponseList = new ArrayList<>();

    public DiagnosisFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (diagnosisFragment != null) {
            return diagnosisFragment;
        } else return new DiagnosisFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnosis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        showBodyDetectionDialog();
//        getAllDrugs();
        getAllBulletin();
        view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }


        });
    }

    private void getAllBulletin() {
        BaseClient.getApi().getAllBulletin(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
        ).enqueue(new Callback<List<BulletinResponse>>() {
            @Override
            public void onResponse(Call<List<BulletinResponse>> call, Response<List<BulletinResponse>> response) {

                if (response.body() != null) {
                    bulletinResponseList = response.body();
                    Log.d(TAG, "onResponse: " + bulletinResponseList.toString());
                }
            }

            @Override
            public void onFailure(Call<List<BulletinResponse>> call, Throwable t) {

            }
        });
    }

    private void getAllDrugs() {
        BaseClient.getApi().getAllDrugs(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
        ).enqueue(new Callback<List<DrugsResponse>>() {
            @Override
            public void onResponse(Call<List<DrugsResponse>> call, Response<List<DrugsResponse>> response) {
                if (response.body() != null) {
                    drugsResponseArrayList = response.body();
                    Log.d(TAG, "onResponse: " + drugsResponseArrayList.toString());
                }
            }

            @Override
            public void onFailure(Call<List<DrugsResponse>> call, Throwable t) {

            }
        });
    }


    private void showBodyDetectionDialog() {

        BaseClient.getApi().getBodyAreas(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
        ).enqueue(new Callback<List<BodyAreasResponse>>() {
            @Override
            public void onResponse(Call<List<BodyAreasResponse>> call, Response<List<BodyAreasResponse>> response) {
                if (response.body() != null) {
                    bodyAreasResponseList = response.body();
                    Log.d(TAG, "onResponse: " + bodyAreasResponseList.toString());
                }
            }

            @Override
            public void onFailure(Call<List<BodyAreasResponse>> call, Throwable t) {

            }
        });
    }


}
