package com.ibnsaad.thedcc.activities;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.ibnsaad.thedcc.listeners.ConnectivityListener;
import com.ibnsaad.thedcc.model.RegisterResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.utils.Connectivity;
import com.ibnsaad.thedcc.utils.Dialogs;
import com.ibnsaad.thedcc.utils.Tools;
import com.ibnsaad.thedcc.widget.EslamDatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements ConnectivityListener {

    private static final String TAG = "RegisterActivity";
    Location location;
    boolean gpsReq = false;
    List<String> specializations;
    private EditText email, password, userName, userAge, userGender, city, country, userType, specialization;
    private View progress;
    private RelativeLayout rootView;
    private NestedScrollView mNestedScrollView;
    // internet
    private Connectivity connectivity;
    private boolean internetConnected;
    //dialogs
    private DatePickerDialog datePicker;
    private Dialog noInternetDialog;
    private Dialog worningDialog;
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

    private void registerNewUser() {

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
        jsonObject1.addProperty("userType", userType.getText().toString().trim());
        if (userType.getText().toString().trim().equals(getString(R.string.doctors)))
            jsonObject1.addProperty("specialization", specialization.getText().toString().trim());
        else
            jsonObject1.addProperty("specialization", "null");
        BaseClient.getApi().registerNewUser(jsonObject1).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                hideProgress();
                if (response.body() != null) {
                    if (response.body().getId() > 0) {
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(RegisterActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
//        (findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (internetConnected)
//                    creatNewUser();
//                else
//                    noInternetDialog = Dialogs.getInstance().showWorningDialog(RegisterActivity.this, getString(R.string.no_internet_connection));
//            }
//        });
        connectivity = new Connectivity(this, this);
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
        getSpecialization();
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
    public void getConnectionType(String connectionType) {

    }

    @Override
    public void isConnected(boolean isConnected) {

        if (worningDialog != null) {
            worningDialog.dismiss();
        }
        if (datePicker != null) {
            datePicker.dismiss();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (!isConnected) {
            internetConnected = false;
            noInternetDialog = Dialogs.getInstance().showWorningDialog(this, getString(R.string.no_internet_connection));

        } else {
            internetConnected = true;
            if (noInternetDialog != null) {
                if (noInternetDialog.isShowing()) {
                    noInternetDialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivity = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (noInternetDialog != null) {
            if (noInternetDialog.isShowing())
                noInternetDialog.dismiss();
        }
        if (worningDialog != null) {
            if (worningDialog.isShowing())
                worningDialog.dismiss();
        }
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
