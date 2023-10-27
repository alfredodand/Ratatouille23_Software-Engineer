package com.example.ratatouille_android.controllers;

import com.example.ratatouille_android.views.MainActivity;
import com.example.ratatouille_android.views.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpController {
    private SignUpActivity signUpActivity;
    private String url = MainActivity.address + "/verify";

    public SignUpController(SignUpActivity signUpActivity){
        this.signUpActivity = signUpActivity;
    }

    public void verifyAccount(String email, String nomeRistorante) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("nomeRistorante", nomeRistorante)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        verifyAccountToServer(client, request);
    }

    private void verifyAccountToServer(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                signUpActivity.setErrorVerificaOnError();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                signUpActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject Jobject = null;
                        try {
                            Jobject = new JSONObject(myResponse);
                            if(Jobject.has("nome"))
                                signUpActivity.setErrorVerificaOnSuccess();
                        } catch (JSONException e) {
                            signUpActivity.setErrorVerificaOnError();
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
