package com.ibnsaad.thedcc.fragments;


import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.listeners.ChangeBodyAreaListener;
import com.ibnsaad.thedcc.model.BodyAreasResponse;
import com.ibnsaad.thedcc.model.BulletinResponse;
import com.ibnsaad.thedcc.model.DrugsResponse;
import com.ibnsaad.thedcc.model.SymptomByIdResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.widget.FragmentBottomSheetDialogFull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosisFragment extends Fragment implements ChangeBodyAreaListener {

    public static DiagnosisFragment diagnosisFragment;
    private final String TAG = "DiagnosisFragment";
    private List<BodyAreasResponse> bodyAreasResponseList = new ArrayList<>();
    private List<DrugsResponse> drugsResponseArrayList = new ArrayList<>();
    private List<BulletinResponse> bulletinResponseList = new ArrayList<>();
    private List<SymptomByIdResponse> symptomByIdResponseList = new ArrayList<>();

    private ImageView bodyView;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        showBodyDetectionDialog();
//        getAllDrugs();
//        getAllBulletin();
//        getSypmotByBodyAreasId();
//        getDrugBySyptomId();
//        getDrugById();

        bodyView = view.findViewById(R.id.bodyView);

        bodyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    float[] point = new float[]{event.getX(), event.getY()};

                    Matrix inverse = new Matrix();
                    bodyView.getImageMatrix().invert(inverse);
                    inverse.mapPoints(point);

                    float density = getResources().getDisplayMetrics().density;

                    float x = point[0] /= density;
                    float y = point[1] /= density;

                    Log.d(TAG, "touch inverse=" + x + " - " + y);

                    // check legs
                    if (x >= 420 && x <= 580 && y >= 520 && y <= 930) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.legs_pain));
                        showBottomSheetDialog(Enums.Legs);
                    } else if (x >= 420 && x <= 580 && y >= 320 && y <= 520) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.stomach));
                        showBottomSheetDialog(Enums.Stomach);
                    } else if (x >= 420 && x <= 580 && y >= 215 && y <= 320) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.chest));
                        showBottomSheetDialog(Enums.Chest);
                    } else if (x >= 350 && x <= 420 && y >= 210 && y <= 580) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
                        showBottomSheetDialog(Enums.Arms);
                    } else if (x >= 580 && x <= 650 && y >= 210 && y <= 580) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
                        showBottomSheetDialog(Enums.Arms);
                    } else if (x >= 450 && x <= 550 && y >= 70 && y <= 200) {
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.head_pain));
                        showBottomSheetDialog(Enums.Head);
                    }

                    return true;
                } else {
                    return false;
                }

            }
        });

    }

    private void getSypmotByBodyAreasId() {
        BaseClient.getApi().getSympotByBodyAreasId(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name()),
                1
        ).enqueue(new Callback<List<SymptomByIdResponse>>() {
            @Override
            public void onResponse(Call<List<SymptomByIdResponse>> call, Response<List<SymptomByIdResponse>> response) {

                if (response.body() != null) {
                    symptomByIdResponseList = response.body();
                    Log.d(TAG, "onResponse: " + symptomByIdResponseList.toString());
                }

            }

            @Override
            public void onFailure(Call<List<SymptomByIdResponse>> call, Throwable t) {

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

    private void getDrugById() {
        BaseClient.getApi().getDrugById(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name()),
                1
        ).enqueue(new Callback<DrugsResponse>() {
            @Override
            public void onResponse(Call<DrugsResponse> call, Response<DrugsResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<DrugsResponse> call, Throwable t) {

            }
        });
    }

    private void getDrugBySyptomId() {
        BaseClient.getApi().getDrugBySyptomId(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
                , 1
        ).enqueue(new Callback<List<DrugsResponse>>() {
            @Override
            public void onResponse(Call<List<DrugsResponse>> call, Response<List<DrugsResponse>> response) {
                if (response.body() != null) {

                    Log.d(TAG, "onResponse: " + response.body());
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

    private void showBottomSheetDialog(Enums enums) {
        // display first sheet
        FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull();
        fragment.setChangeBodyAreaListener(this::setBodyArea);
        fragment.setBodyAreaEnum(enums);
        fragment.show(getChildFragmentManager(), fragment.getTag());
    }

    public void setBodyArea(Enums enums) {
        if (enums.equals(Enums.Head)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.head_pain));
        } else if (enums.equals(Enums.Arms)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
        } else if (enums.equals(Enums.Stomach)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.stomach));
        } else if (enums.equals(Enums.Chest)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.chest));
        } else if (enums.equals(Enums.Legs)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.legs_pain));
        }
    }
}
