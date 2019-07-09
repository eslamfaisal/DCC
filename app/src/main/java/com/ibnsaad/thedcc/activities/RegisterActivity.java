package com.ibnsaad.thedcc.activities;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.google.gson.JsonObject;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.LoginResponse;
import com.ibnsaad.thedcc.model.RegisterResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.Dialogs;
import com.ibnsaad.thedcc.utils.Tools;
import com.ibnsaad.thedcc.widget.EslamDatePickerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    List<String> specializations;
    private EditText email, password, userName, userAge, userGender, city, country, userType, specialization;
    private View progress;
    private RelativeLayout rootView;
    private NestedScrollView mNestedScrollView;
    private Dialog noInternetDialog;
    //dialogs
    private AlertDialog alertDialog;
    private ActionBar actionBar;
    private int REQUEST_PHONE_VERIFICATION = 1258;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();
        initViews();
        initComponent();
    }

    private boolean checkIfContainLowerChar(String string){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        for (int i=0;i <string.length();i++){
            if (chars.contains(""+string.charAt(i))){
                return true;
            }
        }
        return false;
    }

    private boolean validations() {

        String name = userName.getText().toString().trim();
        String age = userAge.getText().toString().trim();
        String passwordTesxt = password.getText().toString().trim();
        String user_gender = userGender.getText().toString().trim();
        String email_ = email.getText().toString().trim();

        if (email_.equals("")) {
            scrollToView(email);
            email.setError(getString(R.string.user_name_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.not_valid_email));
            Log.d(TAG, "creatNewUser: " + getString(R.string.not_valid_email));
            return false;
        }
        else if (passwordTesxt.equals("")|| passwordTesxt.length() < 6|| !checkIfContainLowerChar(passwordTesxt)) {
            scrollToView(password);
            password.setError(getString(R.string.password_is_required));
            noInternetDialog = Dialogs.getInstance().showWorningDialog(RegisterActivity.this, getString(R.string.password_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.password_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.password_is_required));
            return false;
        } else if (name.equals("")) {
            scrollToView(userName);
            userName.setError(getString(R.string.name_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.name_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.name_is_required));
            return false;
        } else if (city.getText().toString().trim().equals("")) {
            scrollToView(city);
            city.setError(getString(R.string.city_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.name_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.city_is_required));
            return false;
        } else if (country.getText().toString().trim().equals("")) {
            scrollToView(city);
            country.setError(getString(R.string.country_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.name_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.city_is_required));
            return false;
        } else if (age.equals("")) {
            scrollToView(userAge);
            userAge.setError(getString(R.string.age_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.age_is_required));
            Log.d(TAG, "creatNewUser: " + getString(R.string.age_is_required));
            return false;
        } else if (user_gender.equals("")) {
            scrollToView(userGender);
            userGender.setError(getString(R.string.user_gender));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.user_gender));
            Log.d(TAG, "creatNewUser: " + getString(R.string.user_gender));
            return false;
        } else if (userType.getText().toString().trim().equals("")) {
            scrollToView(userGender);
            userType.setError(getString(R.string.userType_is_required));
            Dialogs.getInstance().showSnack(RegisterActivity.this, getString(R.string.user_gender));
            Log.d(TAG, "creatNewUser: " + getString(R.string.userType_is_required));
            return false;
        } else
            return true;
    }

    private void registerNewUser() {

        if (!validations())
            return;

        showProgress();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("username", email.getText().toString().trim());
        jsonObject1.addProperty("password", password.getText().toString().trim());
        jsonObject1.addProperty("gender", userGender.getText().toString().trim());
        jsonObject1.addProperty("knownAs", userName.getText().toString().trim());
        jsonObject1.addProperty("dateOfBirth", userAge.getText().toString().trim());
        jsonObject1.addProperty("city", city.getText().toString().trim());
        jsonObject1.addProperty("country", country.getText().toString().trim());
        jsonObject1.addProperty("created", "2019-06-12T20:55:50.063Z");
        jsonObject1.addProperty("lastActive", "2019-06-12T20:55:50.063Z");

        if (userType.getText().toString().trim().equals(getString(R.string.doctors)))
        {
            jsonObject1.addProperty("typeOfUser", userType.getText().toString().trim());
            jsonObject1.addProperty("specialization", specialization.getText().toString().trim());
        }
        else
            jsonObject1.addProperty("specialization", "Patient");
        BaseClient.getApi().registerNewUser(jsonObject1).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                hideProgress();
                if (response.body() != null) {
                    if (response.body().getId()!=null) {

                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.addProperty("username", response.body().getUsername());
                        jsonObject1.addProperty("password", password.getText().toString().trim());

                        BaseClient.getApi().logIn(jsonObject1).enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if (response.body() != null) {
                                    LoginResponse loginRespons = response.body();
                                    Log.d(TAG, "onResponse: " + response.body().getToken());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.TOKEN.name(), loginRespons.getToken().getResult());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.AUTH_TOKEN.name(), "Bearer " + loginRespons.getToken().getResult());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.ID.name(), String.valueOf(loginRespons.getUser().getId()));
                                    SharedHelper.putBoolean(RegisterActivity.this, Enums.IS_LOG_IN.name(), true);
                                    SharedHelper.putKey(RegisterActivity.this, Enums.NAME.name(), loginRespons.getUser().getUsername());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.DateOfBirth.name(), loginRespons.getUser().getDateOfBirth());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.City.name(), loginRespons.getUser().getCity());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.Gender.name(), loginRespons.getUser().getGender());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.Country.name(), loginRespons.getUser().getCountry());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.KnownAs.name(), loginRespons.getUser().getKnownAs());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.PhotoUrl.name(), loginRespons.getUser().getPhotoUrl());
                                    SharedHelper.putKey(RegisterActivity.this, Enums.Age.name(), String.valueOf(loginRespons.getUser().getAge()));
                                    SharedHelper.putKey(RegisterActivity.this, Enums.UserType.name(), String.valueOf(loginRespons.getUser().getUserType()));
                                    SharedHelper.putKey(RegisterActivity.this, Enums.Spetialization.name(), String.valueOf(loginRespons.getUser().getSpecialization()));

                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    hideProgress();
                                    if(response.body().toString().contains("PasswordRequiresLower")) {
                                        noInternetDialog = Dialogs.getInstance().showWorningDialog(RegisterActivity.this, getString(R.string.password_is_required));
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                if(t.getMessage().contains("PasswordRequiresLower")) {
                                    noInternetDialog = Dialogs.getInstance().showWorningDialog(RegisterActivity.this, getString(R.string.password_is_required));
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                hideProgress();
                if(t.getMessage().contains("PasswordRequiresLower")) {
                    noInternetDialog = Dialogs.getInstance().showWorningDialog(RegisterActivity.this, getString(R.string.password_is_required));
                }

            }
        });


    }

    private void scrollToTop() {
        int targetScroll = mNestedScrollView.getScrollY() + 1000;
        mNestedScrollView.scrollTo(0, targetScroll);
        mNestedScrollView.setSmoothScrollingEnabled(true);
        ViewCompat.setNestedScrollingEnabled(mNestedScrollView, false);
        final int currentScrollY = mNestedScrollView.getScrollY();
        ViewCompat.postOnAnimationDelayed(mNestedScrollView, new Runnable() {
            int currentY = currentScrollY;

            @Override
            public void run() {
                if (currentScrollY == mNestedScrollView.getScrollY()) {
                    ViewCompat.setNestedScrollingEnabled(mNestedScrollView, true);
                    return;
                }
                currentY = mNestedScrollView.getScrollY();
                ViewCompat.postOnAnimation(mNestedScrollView, this);
            }
        }, 5);
    }

    private void initViews() {


        email = findViewById(R.id.email);
        userType = findViewById(R.id.userType);
        specialization = findViewById(R.id.specialization);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        userGender = findViewById(R.id.user_gender);
        rootView = findViewById(R.id.root_view);
        progress = findViewById(R.id.progress);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        mNestedScrollView = findViewById(R.id.scroller);

    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        toolbar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Tools.setSystemBarColorInt(this, Color.parseColor("#006ACF"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.create_new_account);
    }

    private void initComponent() {

        (findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
        (findViewById(R.id.user_age)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgeDialog();
            }
        });
        userGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog(v);
            }
        });
        userType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserTypeDialog(v);
            }
        });

        (findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogIn();
            }
        });
        specializations = specializationsList();
        specialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpecialDialog(v);
            }
        });

    }

    private void getSpecialization() {
        BaseClient.getApi().getSpecializations(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name())
        ).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                specializations = response.body();

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
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

    private void creatNewUser() {
        final String name = userName.getText().toString();
        final String age = userAge.getText().toString();
        String passwordTesxt = password.getText().toString();
        final String user_gender = userGender.getText().toString();
        final String email_ = email.getText().toString();

    }

    private void scrollToView(final View view) {
        mNestedScrollView.scrollBy(0, 1);
        ObjectAnimator.ofInt(mNestedScrollView, "scrollY", view.getTop()).setDuration(700).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PHONE_VERIFICATION && resultCode == RESULT_OK) {

        }
    }


    private void showAgeDialog() {

        EslamDatePickerDialog dialogfragment = new EslamDatePickerDialog();
        dialogfragment.setTextView(userAge);
        dialogfragment.show(getFragmentManager(), "Eslam");

    }

    private void showGenderDialog(final View v) {
        final String[] array = new String[]{
                getString(R.string.male), getString(R.string.female)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(getString(R.string.gender));
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private List<String> specializationsList() {
        List<String> specializationsList = new ArrayList<>();
        specializationsList.add(getString(R.string.bones_doctor));
        specializationsList.add(getString(R.string.heart_doctor));
        specializationsList.add(getString(R.string.internal_doctor));
        specializationsList.add(getString(R.string.dermatologist));
        specializationsList.add(getString(R.string.feminine_doctor));
        specializationsList.add(getString(R.string.pediatrician));
        return specializationsList;
    }
    private void showSpecialDialog(final View v) {

        final String[] array = new String[specializations.size()];
        for (int i = 0; i < specializations.size(); i++) {
            array[i] = specializations.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(getString(R.string.gender));
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserTypeDialog(final View v) {
        final String[] array = new String[]{
                getString(R.string.doctors), getString(R.string.patient)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(getString(R.string.accunt_type));
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                if (array[i].equals(getString(R.string.patient))) {
                    specialization.setVisibility(View.GONE);
                } else {
                    specialization.setVisibility(View.VISIBLE);
                }
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        goLogIn();
    }

    private void goLogIn() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
