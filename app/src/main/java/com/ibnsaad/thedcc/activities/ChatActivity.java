package com.ibnsaad.thedcc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.adapter.ChatAdapter;
import com.ibnsaad.thedcc.enums.Enums;
import com.ibnsaad.thedcc.heper.SharedHelper;
import com.ibnsaad.thedcc.model.Message;
import com.ibnsaad.thedcc.model.User;
import com.ibnsaad.thedcc.network.RetrofitNetwork.BaseClient;
import com.ibnsaad.thedcc.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {


    private final String TAG = "ChatActivity";
    private FloatingActionButton btn_send;
    private EditText et_content;
    private ChatAdapter adapter;
    private RecyclerView recycler_view;
    private View back;
    private ActionBar actionBar;
    private User user;
    private TextView userName;
    private SimpleDraweeView userImage;
    private FloatingActionButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolbar();
        iniComponent();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(Enums.USER.name());
        userName.setText(user.getKnownAs());
        userImage.setImageURI(user.getPhotoUrl());
        getOldMessages();


    }

    private void getOldMessages() {

        BaseClient.getApi().getAllMessages(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                Integer.parseInt(SharedHelper.getKey(this, Enums.ID.name())),
                user.getId()

        ).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                if (response.body() != null) {

                    Log.d(TAG, "onResponse: " + response.body().toString());
                    adapter.setItems(response.body());

                } else {
                    Log.d(TAG, "onResponse: " + response.toString());
                }

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle(null);
        Tools.setSystemBarColor(this);
    }

    public void iniComponent() {
        back = findViewById(R.id.back);
        userImage = findViewById(R.id.user_image);
        sendBtn = findViewById(R.id.btn_send);
        userName = findViewById(R.id.user_name);
        recycler_view = findViewById(R.id.chatRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        adapter = new ChatAdapter(new ArrayList<>(), this);
        recycler_view.setAdapter(adapter);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_content.getText().toString().trim().equals(""))
                    makeMessage(et_content.getText().toString());
                et_content.setText("");
            }
        });
    }

    public static String dateFormat = "dd-MM-yyyy hh:mm";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public static String ConvertMilliSecondsToFormattedDate(String milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }

    //for make message with content
    private void makeMessage(String messageContent) {

        int recipientId = user.getId();
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        String time = output.format(new Date());

        Message message = new Message(Integer.parseInt(SharedHelper.getKey(this, Enums.ID.name())),
                recipientId, time, messageContent);

        BaseClient.getApi().senMessage(
                SharedHelper.getKey(this, Enums.AUTH_TOKEN.name()),
                Integer.parseInt(SharedHelper.getKey(this, Enums.ID.name())),
                message)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {

                        if (response.isSuccessful()) {
                            adapter.insertItem(message);
                            recycler_view.scrollToPosition(adapter.getItemCount() - 1);

                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                        Toast.makeText(ChatActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
