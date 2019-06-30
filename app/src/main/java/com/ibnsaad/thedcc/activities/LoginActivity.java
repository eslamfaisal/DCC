package com.ibnsaad.thedcc.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.LoginRespons;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.Dialogs;
import com.ibnsaad.thedcc.utils.Tools;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //views
    private EditText email, password;
    private View progress;
    private RelativeLayout rootView;
    private Dialog noInternetDialog;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initToolbar();
        initViews();
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
//        try {
//            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + loginRespons.getUser().getUsername() + "'}, 'include_player_ids': ['" + onsignalid + "']}"), null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rootView = findViewById(R.id.root_view);
        progress = findViewById(R.id.progress);
        setListeners();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        toolbar.setTitle("");
        Tools.setSystemBarColorInt(this, Color.parseColor("#006ACF"));

    }

    public void goRegister(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 123);
        finish();
    }

    private void setListeners() {
        (findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void showProgress() {
        rootView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        rootView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    private void login() {
        OneSignal.sendTag("user_name", email.getText().toString());
        String email_ = email.getText().toString();
        String password_ = password.getText().toString();

        if (password_.equals("")) {
            password.setError(getString(R.string.password_is_required));
            Dialogs.getInstance().showSnack(LoginActivity.this, getString(R.string.password_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.password_is_required));
            return;
        }

        showProgress();

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("username", email_);
        jsonObject1.addProperty("password", password_);

        BaseClient.getApi().logIn(jsonObject1).enqueue(new Callback<LoginRespons>() {
            @Override
            public void onResponse(Call<LoginRespons> call, Response<LoginRespons> response) {
                if (response.body() != null) {
                    LoginRespons loginRespons = response.body();
                    Log.d(TAG, "onResponse: " + response.body().getToken());
                    SharedHelper.putKey(LoginActivity.this, Enums.TOKEN.name(), loginRespons.getToken());
                    SharedHelper.putKey(LoginActivity.this, Enums.AUTH_TOKEN.name(), "Bearer " + loginRespons.getToken());
                    SharedHelper.putKey(LoginActivity.this, Enums.ID.name(), String.valueOf(loginRespons.getUser().getId()));
                    SharedHelper.putBoolean(LoginActivity.this, Enums.IS_LOG_IN.name(), true);
                    SharedHelper.putKey(LoginActivity.this, Enums.NAME.name(), loginRespons.getUser().getUsername());
                    SharedHelper.putKey(LoginActivity.this, Enums.DateOfBirth.name(), loginRespons.getUser().getDateOfBirth());
                    SharedHelper.putKey(LoginActivity.this, Enums.City.name(), loginRespons.getUser().getCity());
                    SharedHelper.putKey(LoginActivity.this, Enums.Gender.name(), loginRespons.getUser().getGender());
                    SharedHelper.putKey(LoginActivity.this, Enums.Country.name(), loginRespons.getUser().getCountry());
                    SharedHelper.putKey(LoginActivity.this, Enums.KnownAs.name(), loginRespons.getUser().getKnownAs());
                    SharedHelper.putKey(LoginActivity.this, Enums.PhotoUrl.name(), loginRespons.getUser().getPhotoUrl());
                    SharedHelper.putKey(LoginActivity.this, Enums.Age.name(), String.valueOf(loginRespons.getUser().getAge()));
                    SharedHelper.putKey(LoginActivity.this, Enums.UserType.name(), String.valueOf(loginRespons.getUser().getUserType()));
                    SharedHelper.putKey(LoginActivity.this, Enums.Spetialization.name(), String.valueOf(loginRespons.getUser().getSpecialization()));
                    String onsignalid = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
                    while (onsignalid == null) {
                        onsignalid = null;
                    }
                    Log.d(TAG, "onResponse: " + onsignalid);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    hideProgress();
                    if (response.toString().contains("Unauthorized")) {
                        noInternetDialog = Dialogs.getInstance().showWorningDialog(LoginActivity.this, getString(R.string.login_fail_type));
                    } else
                        noInternetDialog = Dialogs.getInstance().showWorningDialog(LoginActivity.this, response.toString());
                    Log.d(TAG, "onResponse: body is null");
                }

            }

            @Override
            public void onFailure(Call<LoginRespons> call, Throwable t) {
                hideProgress();
                if (t.getMessage().contains("Unauthorized")) {
                    noInternetDialog = Dialogs.getInstance().showWorningDialog(LoginActivity.this, getString(R.string.login_fail_type));
                } else
                    noInternetDialog = Dialogs.getInstance().showWorningDialog(LoginActivity.this, t.getMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (noInternetDialog != null) {
            if (noInternetDialog.isShowing())
                noInternetDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}