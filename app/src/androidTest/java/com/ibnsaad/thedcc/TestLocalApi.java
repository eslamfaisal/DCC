package com.ibnsaad.thedcc;


import androidx.test.InstrumentationRegistry;
import android.util.Log;

import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.server.BaseClient;

import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestLocalApi {
    private static final String TAG = "TestLocalApi";

    @Test
    public void testApis() {
        String token = "Bearer "+ SharedHelper.getKey(InstrumentationRegistry.getTargetContext(), Enums.TOKEN.name());
        BaseClient.getApi().getUsers(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "onResponse: "+response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }


}
