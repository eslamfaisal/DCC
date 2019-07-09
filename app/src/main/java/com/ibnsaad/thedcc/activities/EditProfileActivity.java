package com.ibnsaad.thedcc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.ibnsaad.thedcc.IndicatorView.draw.controller.DrawController;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.Photo;
import com.ibnsaad.thedcc.model.ProfileResponse;
import com.ibnsaad.thedcc.server.BaseClient;
import com.ibnsaad.thedcc.slider.IndicatorAnimations;
import com.ibnsaad.thedcc.slider.SliderAdapterExample;
import com.ibnsaad.thedcc.slider.SliderAnimations;
import com.ibnsaad.thedcc.slider.SliderView;
import com.ibnsaad.thedcc.utils.ViewAnimation;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public final String TAG = EditProfileActivity.class.getSimpleName();
    ProfileResponse profileResponse;
    @BindView(R.id.gender)
    AppCompatEditText gender;
    @BindView(R.id.name)
    AppCompatEditText name;
    @BindView(R.id.date_of_birth)
    AppCompatEditText date_of_birth;
    @BindView(R.id.country)
    AppCompatEditText country;
    @BindView(R.id.city)
    AppCompatEditText city;
    @BindView(R.id.introduction)
    AppCompatEditText introduction;
    @BindView(R.id.looking_for)
    AppCompatEditText looking_for;
    @BindView(R.id.interests)
    AppCompatEditText interests;
    @BindView(R.id.specialization)
    AppCompatEditText specialization;
    @BindView(R.id.userType)
    AppCompatEditText userType;

    @BindView(R.id.back_drop)
    View back_drop;

    @BindView(R.id.new_image)
    CardView new_image;

    @BindView(R.id.main)
    CardView mainImage;

    @BindView(R.id.delete)
    CardView deleteImage;

    @BindView(R.id.pick_image)
    FloatingActionButton imageChooces;

    SliderAdapterExample adapter;
    private SliderView sliderView;
    private UCrop.Options options;
    private Bitmap thumbBitmap = null;
    private byte[] imageBytes;
    private boolean rotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        BaseClient.getApi().getProfile(SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                SharedHelper.getKey(this, Enums.ID.name())).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.d(TAG, "onResponse: " + response.body().toString());
                profileResponse = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fillFields();
                        initSlider();
                    }
                });
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.back_drop)
    void backDrop() {
        toggleFabMode(imageChooces);
    }

    @OnClick(R.id.pick_image)
    void imageChooces() {
        toggleFabMode(imageChooces);
    }

    @OnClick(R.id.new_image)
    void pick_image() {
        ImagePicker.create(this)
                .limit(1)
                .folderMode(false)
                .showCamera(true)
                .start();
    }


    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(mainImage);
            ViewAnimation.showIn(deleteImage);
            ViewAnimation.showIn(new_image);
            back_drop.setVisibility(View.VISIBLE);
            sliderView.setAutoCycle(false);
        } else {
            ViewAnimation.showOut(mainImage);
            ViewAnimation.showOut(deleteImage);
            ViewAnimation.showOut(new_image);
            back_drop.setVisibility(View.GONE);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
        }
    }

    private void fillFields() {

        if (profileResponse == null) {
            profileResponse = new ProfileResponse();
        } else {
            gender.setText(profileResponse.getGender());
            name.setText(profileResponse.getKnownAs());
            date_of_birth.setText(profileResponse.getDateOfBirth());
            interests.setText(profileResponse.getInterests());
            introduction.setText(profileResponse.getIntroducation());
            city.setText(profileResponse.getCity());
            country.setText(profileResponse.getCountry());
            looking_for.setText(profileResponse.getLookingFor());
            specialization.setText(profileResponse.getSpecialization());
            userType.setText(profileResponse.getUserType());
        }
    }

    @OnClick(R.id.userType)
    void setUserType(){
        showUserTypeDialog(userType);
    }
    @OnClick(R.id.save)
    void save() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("gender", gender.getText().toString());
        jsonObject.addProperty("dateOfBirth", date_of_birth.getText().toString());
        jsonObject.addProperty("knownAs", name.getText().toString());
        jsonObject.addProperty("introducation", introduction.getText().toString());
        jsonObject.addProperty("lookingFor", looking_for.getText().toString());
        jsonObject.addProperty("interests", interests.getText().toString());
        jsonObject.addProperty("city", city.getText().toString());
        jsonObject.addProperty("typeOfUser", userType.getText().toString());
        jsonObject.addProperty("country", country.getText().toString());
        jsonObject.addProperty("specialization", specialization.getText().toString());

        BaseClient.getApi().updateProfile(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                profileResponse.getId(),
                jsonObject
        ).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {


                if (response.code() == 204) {
                    finish();
                } else {
                    Log.d(TAG, "onResponse: ." + response.toString());
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                finish();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.delete)
    void deleteImage() {
        BaseClient.getApi().deleteImage(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                SharedHelper.getKey(this, Enums.ID.name()),
                String.valueOf(profileResponse.getPhotos().get(sliderView.getCurrentPagePosition()).getId())
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200 || response.code() == 204) {
                    recreate();
                } else {
                    Toast.makeText(EditProfileActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                if (t.getMessage() != null) {
                    if (t.getMessage().equals("End of input at line 1 column 1 path $")) {
                        recreate();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recreate();
                }
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.main)
    void setMainImage() {
        BaseClient.getApi().setMainImage(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                SharedHelper.getKey(this, Enums.ID.name()),
                String.valueOf(profileResponse.getPhotos().get(sliderView.getCurrentPagePosition()).getId())
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200 || response.code() == 204) {
                    recreate();
                } else {
                    Toast.makeText(EditProfileActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t.getMessage() != null) {
                    if (t.getMessage().equals("End of input at line 1 column 1 path $")) {
                        recreate();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recreate();
                }
            }
        });

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
                ((AppCompatEditText) v).setText(array[i]);
                if (array[i].equals(getString(R.string.patient))) {
                    specialization.setVisibility(View.GONE);
                } else {
                    specialization.setVisibility(View.VISIBLE);
                }
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return;
        }

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);
            Uri res_url = Uri.fromFile(new File((image.getPath())));
            cropImage(image, res_url);

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //  if (resultUri!=null)
            assert resultUri != null;
            bitmapCompress(resultUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();
            //imageView.setImageURI(resultUri);
            upLoadImae(resultUri);
            Log.d("TAG", "onActivityResult: " + Arrays.toString(imageBytes));
        }

    }

    private void cropImage(Image image, Uri res_url) {
        UCrop.of(res_url, Uri.fromFile(new File(getCacheDir(), image.getName())))
                .withOptions(options)
                .start(this);
    }

    private void bitmapCompress(Uri resultUri) {
        final File thumbFilepathUri = new File(resultUri.getPath());

        try {
            thumbBitmap = new Compressor(this)
                    .setQuality(100)
                    .compressToBitmap(thumbFilepathUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initSlider() {

        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderAdapterExample(this, profileResponse.getPhotos());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderIndicatorSelectedColor(Color.BLUE);
        sliderView.setSliderIndicatorUnselectedColor(Color.GRAY);
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

            }
        });

        sliderView.startAutoCycle();


    }

    private void upLoadImae(Uri imageUri) {
        File file2 = new File(imageUri.getPath());
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file2);
        MultipartBody.Part image = MultipartBody.Part.createFormData("File", System.currentTimeMillis() + ".jpg", surveyBody);

        BaseClient.getApi().uploadImage(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                SharedHelper.getKey(this, Enums.ID.name()),
                "2019-06-12T19:50:23.479",
                "2019-06-12T19:50:23.479",
                "2019-06-12T19:50:23.479",
                "2019-06-12T19:50:23.479",
                image
        ).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                recreate();
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

                recreate();
            }
        });

    }

}
