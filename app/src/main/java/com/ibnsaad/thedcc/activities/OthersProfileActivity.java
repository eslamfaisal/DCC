package com.ibnsaad.thedcc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.fragments.ProfileFragment;

public class OthersProfileActivity extends AppCompatActivity {

    private static final String TAG = "OthersProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Enums.ID.name());

        Log.d(TAG, "onCreate: "+id);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new ProfileFragment(id,false));
        ft.commit();
    }
}
