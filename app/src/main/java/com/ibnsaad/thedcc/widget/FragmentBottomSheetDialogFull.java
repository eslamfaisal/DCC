package com.ibnsaad.thedcc.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.adapter.AdapterListExpand;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.listeners.ChangeBodyAreaListener;
import com.ibnsaad.thedcc.model.BodyAreasResponse;
import com.ibnsaad.thedcc.model.DrugsResponse;
import com.ibnsaad.thedcc.model.Social;
import com.ibnsaad.thedcc.model.SymptomByIdResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class FragmentBottomSheetDialogFull extends BottomSheetDialogFragment {

    private List<BodyAreasResponse> bodyAreasResponseList = new ArrayList<>();
    private List<SymptomByIdResponse> symptomByIdResponseList = new ArrayList<>();
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    private ImageView bodyView;
    private ChangeBodyAreaListener changeBodyAreaListener;
    private Enums bodyAreaEnum;
    private AppCompatSpinner shap_body_area_btn;
    private AppCompatSpinner spinnerbodyAreas;
    private AppCompatSpinner symptomsSpinner;
    private AppCompatSpinner spinner_drugs;
    private ImageButton btn_close;
    private View space;

    private RecyclerView recyclerView;
    private AdapterListExpand mAdapter;
    private int mLastTouchEvent = -1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet_dialog_full, null);
        dialog.setContentView(view);
        initViews(view);
        setlistener();
        setBodyArea(bodyAreaEnum);
        if (getActivity() != null)
            showBodyDetectionDialog();
        return dialog;
    }

    private void initViews(View view) {
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(Tools.getScreenHeight() / 2);
        bodyView = view.findViewById(R.id.bodyView);
        space = view.findViewById(R.id.lyt_spacer);
        shap_body_area_btn = view.findViewById(R.id.spinner_body_area);
        spinnerbodyAreas = view.findViewById(R.id.bodyAreasResponseList);
        symptomsSpinner = view.findViewById(R.id.symptomsSpinner);
        spinner_drugs = view.findViewById(R.id.spinner_drugs);
        app_bar_layout = view.findViewById(R.id.app_bar_layout);
        btn_close = view.findViewById(R.id.bt_close);
        space.setMinimumHeight(Tools.getScreenHeight() / 3);
        hideView(app_bar_layout);
        setShap_body_area_spinner();

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, Tools.getScreenHeight() / 2);
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        recyclerView = view.findViewById(R.id.drugs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);
        //set data and list adapter
        mAdapter = new AdapterListExpand(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(true);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListExpand.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Social obj, int position) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setlistener() {

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        bodyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastTouchEvent = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        mLastTouchEvent = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (mLastTouchEvent == MotionEvent.ACTION_DOWN) {

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
                                changeBodyAreaListener.setBodyArea(Enums.Legs);
                                shap_body_area_btn.setSelection(2);
                            } else if (x >= 420 && x <= 580 && y >= 320 && y <= 520) {
                                bodyView.setImageDrawable(getResources().getDrawable(R.drawable.stomach));
                                changeBodyAreaListener.setBodyArea(Enums.Stomach);
                                shap_body_area_btn.setSelection(4);
                            } else if (x >= 420 && x <= 580 && y >= 215 && y <= 320) {
                                bodyView.setImageDrawable(getResources().getDrawable(R.drawable.chest));
                                changeBodyAreaListener.setBodyArea(Enums.Chest);
                                shap_body_area_btn.setSelection(3);
                            } else if (x >= 350 && x <= 420 && y >= 210 && y <= 580) {
                                bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
                                changeBodyAreaListener.setBodyArea(Enums.Arms);
                                shap_body_area_btn.setSelection(1);
                            } else if (x >= 580 && x <= 650 && y >= 210 && y <= 580) {
                                bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
                                changeBodyAreaListener.setBodyArea(Enums.Arms);
                                shap_body_area_btn.setSelection(1);
                            } else if (x >= 450 && x <= 550 && y >= 70 && y <= 200) {
                                bodyView.setImageDrawable(getResources().getDrawable(R.drawable.head_pain));
                                changeBodyAreaListener.setBodyArea(Enums.Head);
                                shap_body_area_btn.setSelection(0);
                            }

                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setShap_body_area_spinner() {
        List<String> bodyAreaList = new ArrayList<>();
        bodyAreaList.add(getString(R.string.head));
        bodyAreaList.add(getString(R.string.arms));
        bodyAreaList.add(getString(R.string.legs));
        bodyAreaList.add(getString(R.string.chest));
        bodyAreaList.add(getString(R.string.stomach));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bodyAreaList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shap_body_area_btn.setAdapter(dataAdapter);
        shap_body_area_btn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        changeBodyAreaListener.setBodyArea(Enums.Head);
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.head_pain));
                    }
                    break;
                    case 1: {
                        changeBodyAreaListener.setBodyArea(Enums.Arms);
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
                    }
                    break;
                    case 2:
                        changeBodyAreaListener.setBodyArea(Enums.Legs);
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.legs_pain));
                        break;
                    case 3: {
                        changeBodyAreaListener.setBodyArea(Enums.Chest);
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.chest));
                    }
                    break;
                    case 4: {
                        changeBodyAreaListener.setBodyArea(Enums.Stomach);
                        bodyView.setImageDrawable(getResources().getDrawable(R.drawable.stomach));
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setBodyArea(Enums enums) {
        if (enums.equals(Enums.Head)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.head_pain));
            shap_body_area_btn.setSelection(0);
        } else if (enums.equals(Enums.Arms)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.arms));
            shap_body_area_btn.setSelection(1);
        } else if (enums.equals(Enums.Stomach)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.stomach));
            shap_body_area_btn.setSelection(4);
        } else if (enums.equals(Enums.Chest)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.chest));
            shap_body_area_btn.setSelection(3);
        } else if (enums.equals(Enums.Legs)) {
            bodyView.setImageDrawable(getResources().getDrawable(R.drawable.legs_pain));
            shap_body_area_btn.setSelection(2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

    private void showBodyDetectionDialog() {

        BaseClient.getApi().getBodyAreas(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
        ).enqueue(new Callback<List<BodyAreasResponse>>() {
            @Override
            public void onResponse(Call<List<BodyAreasResponse>> call, Response<List<BodyAreasResponse>> response) {
                if (response.body() != null) {
                    bodyAreasResponseList = response.body();
                    List<String> list = new ArrayList<>();
                    for (BodyAreasResponse bodyAreasResponse : response.body()) {
                        list.add(bodyAreasResponse.getNameArea());
                    }
                    if (getActivity() != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerbodyAreas.setAdapter(dataAdapter);
                        Log.d(TAG, "onResponse: " + bodyAreasResponseList.toString());
                        spinnerbodyAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getSypmotByBodyAreasId(position + 1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<List<BodyAreasResponse>> call, Throwable t) {

            }
        });
    }


    private void getSypmotByBodyAreasId(int bodyArea) {
        mAdapter.clear();
        BaseClient.getApi().getSympotByBodyAreasId(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name()),
                bodyArea
        ).enqueue(new Callback<List<SymptomByIdResponse>>() {
            @Override
            public void onResponse(Call<List<SymptomByIdResponse>> call, Response<List<SymptomByIdResponse>> response) {
                if (response.body() != null) {
                    List<String> list = new ArrayList<>();
                    for (SymptomByIdResponse symptomByIdResponse : response.body()) {
                        list.add(symptomByIdResponse.getSymptomName());
                    }
                    if (getActivity() != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        symptomsSpinner.setAdapter(dataAdapter);

                        symptomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getDrugBySyptomId(position + 1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onFailure(Call<List<SymptomByIdResponse>> call, Throwable t) {

            }
        });
    }


    private void getDrugBySyptomId(int symptomId) {
        BaseClient.getApi().getDrugBySyptomId(
                SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name())
                , symptomId
        ).enqueue(new Callback<List<DrugsResponse>>() {
            @Override
            public void onResponse(Call<List<DrugsResponse>> call, Response<List<DrugsResponse>> response) {
                if (response.body() != null) {

                    List<String> list = new ArrayList<>();
                    for (DrugsResponse drugsResponse : response.body()) {
                        list.add(drugsResponse.getDrugName());
                    }
                    if (getActivity() != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_drugs.setAdapter(dataAdapter);
                        spinner_drugs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        mAdapter.setItems(response.body());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<DrugsResponse>> call, Throwable t) {

            }
        });
    }

    public void setChangeBodyAreaListener(ChangeBodyAreaListener changeBodyAreaListener) {
        this.changeBodyAreaListener = changeBodyAreaListener;
    }

    public void setBodyAreaEnum(Enums bodyAreaEnum) {
        this.bodyAreaEnum = bodyAreaEnum;
    }
}
