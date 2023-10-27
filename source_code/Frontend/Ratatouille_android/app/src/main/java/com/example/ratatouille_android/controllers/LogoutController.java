package com.example.ratatouille_android.controllers;

import android.content.Intent;
import android.util.Log;

import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogoutController {

    private HomeActivity homeActivity;
    private User loggedUser;
    private String url = MainActivity.address + "/logout";

    public LogoutController(HomeActivity homeActivity, User loggedUser) {
        this.loggedUser = loggedUser;
        this.homeActivity = homeActivity;
    }

    public void logout(){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idUtente", String.valueOf(loggedUser.getIdUtente()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Authorization", loggedUser.getToken())
                .build();
        serverRequest(client, request);
    }

    private void serverRequest(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("prova", myResponse);
                    }
                });
            }
        });
    }

    public void goToMainActivity(){
        Intent switchActivityIntent = new Intent(homeActivity, MainActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        homeActivity.startActivity(switchActivityIntent);
        homeActivity.finish();
    }
}