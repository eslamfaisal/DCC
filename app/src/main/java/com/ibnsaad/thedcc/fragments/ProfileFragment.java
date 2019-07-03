package com.ibnsaad.thedcc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.activities.EditProfileActivity;
import com.ibnsaad.thedcc.adapter.ProfileImagesAdapter;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.ProfileResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.ViewAnimation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    public static ProfileFragment instance;
    @BindView(R.id.like)
    CardView like;
    @BindView(R.id.message)
    CardView message;
    @BindView(R.id.userAction)
    FloatingActionButton userAction;
    private SimpleDraweeView image;
    private TextView name, looking_for, introduction, interests, location;
    private RecyclerView imagedRecyclerView;
    private LinearLayoutManager imagedLinearLayoutManager;
    private ProfileImagesAdapter imagesAdapter;
    //    private FloatingActionButton editProfile;
    private ProfileResponse profileResponse;
    private String userId;
    private boolean myProfile;
    private boolean rotate;

    public ProfileFragment() {
    }

    public ProfileFragment(String id, boolean myProfile) {
        // Required empty public constructor
        this.myProfile = myProfile;
        userId = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);


    }

    @Override
    public void onResume() {
        super.onResume();
        BaseClient.getApi().getProfile(SharedHelper.getKey(getActivity(), Enums.AUTH_TOKEN.name()),
                userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.d(TAG, "onResponse: " + response.body().toString());
                profileResponse = response.body();
                image.setImageURI(profileResponse.getPhotoUrl());
                name.setText(profileResponse.getKnownAs());
                String locations = profileResponse.getCity() + " , " + profileResponse.getCountry();
                location.setText(locations);
                interests.setText(profileResponse.getInterests());
                introduction.setText(profileResponse.getIntroducation());
                looking_for.setText(profileResponse.getLookingFor());
                imagedRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        imagesAdapter.setPhotoList(profileResponse.getPhotos());
                        imagesAdapter.notifyDataSetChanged();

                        Log.d(TAG, "run: " + profileResponse.getPhotos().size());
                    }
                });

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });
    }

    private void initViews(View view) {
        profileResponse = new ProfileResponse();
        image = view.findViewById(R.id.image);
        looking_for = view.findViewById(R.id.looking_for);
        interests = view.findViewById(R.id.interests);
        name = view.findViewById(R.id.name);
        introduction = view.findViewById(R.id.introduction);
        location = view.findViewById(R.id.location);
        imagedRecyclerView = view.findViewById(R.id.images_recycler_View);
        imagedLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        imagedRecyclerView.setLayoutManager(imagedLinearLayoutManager);
        imagesAdapter = new ProfileImagesAdapter(new ArrayList<>(), getActivity());
        imagedRecyclerView.setAdapter(imagesAdapter);

        if (myProfile) {
            userAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_create));
            userAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra(Enums.ID.name(), profileResponse);
                    startActivity(intent);
                }
            });
        } else {
            userAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_add));
            userAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleFabMode();
                }
            });

        }
        if (SharedHelper.getKey(getActivity(), Enums.UserType.name()).equals(getActivity().getString(R.string.doctors))) {
            image.getHierarchy().setPlaceholderImage(getActivity().getDrawable(R.drawable.doctor), ScalingUtils.ScaleType.CENTER_INSIDE);
        } else {
            image.getHierarchy().setPlaceholderImage(getActivity().getDrawable(R.drawable.patient), ScalingUtils.ScaleType.CENTER_INSIDE);
        }
    }


    @OnClick(R.id.message)
    void message() {
        toggleFabMode();
    }

    @OnClick(R.id.like)
    void like() {
        toggleFabMode();
    }

    private void toggleFabMode() {
        rotate = ViewAnimation.rotateFab(userAction, !rotate);
        if (rotate) {
            ViewAnimation.showIn(like);
            ViewAnimation.showIn(message);
        } else {
            ViewAnimation.showOut(like);
            ViewAnimation.showOut(message);
        }
    }
}
