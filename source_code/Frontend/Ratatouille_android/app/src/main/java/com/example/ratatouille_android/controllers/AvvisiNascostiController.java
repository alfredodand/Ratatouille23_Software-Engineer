package com.example.ratatouille_android.controllers;

import android.content.Intent;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.AvvisiNascostiActivity;
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

public class AvvisiNascostiController {
    private AvvisiNascostiActivity avvisiNascostiActivity;
    private User loggedUser;
    private String url = MainActivity.address;

    public AvvisiNascostiController(AvvisiNascostiActivity avvisiNascostiActivity, User loggedUser){
        this.avvisiNascostiActivity = avvisiNascostiActivity;
        this.loggedUser = loggedUser;
    }

    public void goToNoticesFragment() {
        Intent switchActivityIntent = new Intent(avvisiNascostiActivity, HomeActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        switchActivityIntent.putExtra("frgToLoad", R.id.notices);
        avvisiNascostiActivity.startActivity(switchActivityIntent);
    }

    public void markAsNotHideNotice(Integer id_avviso){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id_avviso", String.valueOf(id_avviso))
                .add("idUtente", String.valueOf(loggedUser.getIdUtente()))
                .build();
        Request request = new Request.Builder()
                .url(url + "/avviso/segna-come-non-nascosto/" + id_avviso)
                .post(formBody)
                .header("Authorization", loggedUser.getToken())
                .build();
        serverRequestNotice(client, request, id_avviso);
    }

    private void serverRequestNotice(OkHttpClient client, Request request, Integer id_avviso) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                avvisiNascostiActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        avvisiNascostiActivity.removeFromAvvisiNascosti(id_avviso);
                    }
                });
            }
        });
    }
}
