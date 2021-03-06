package com.ibnsaad.thedcc.activities;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;

public class SplashActivity extends AppCompatActivity {
    private int ACTIVITY_AUTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!SharedHelper.getboolean(this, Enums.IS_LOG_IN.name())) {
            startActivityForResult(new Intent(this, LoginActivity.class), ACTIVITY_AUTH);
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        } else {
            finish();
        }
    }

}
