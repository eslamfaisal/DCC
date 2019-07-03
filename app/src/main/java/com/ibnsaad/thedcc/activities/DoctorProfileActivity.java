package com.ibnsaad.thedcc.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;

import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.utils.Tools;

public class DoctorProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_1), R.drawable.image_8);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_2), R.drawable.image_8);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_3), R.drawable.image_8);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_4), R.drawable.image_8);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_5), R.drawable.image_8);
    }


}
