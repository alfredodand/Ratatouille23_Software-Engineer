package com.example.ratatouille_android.controllers;

import android.content.Intent;
import android.util.Log;

import com.example.ratatouille_android.models.Attivita;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.DispensaActivity;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;
import com.example.ratatouille_android.views.jfragment.AccountFragment;
import com.google.android.material.badge.BadgeDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeController {
    private User loggedUser;
    private HomeActivity homeActivity;
    private String url = MainActivity.address + "/";

    public HomeController(User loggedUser, HomeActivity homeActivity){
        this.loggedUser = loggedUser;
        this.homeActivity = homeActivity;
    }



    public void getNomeRistorante() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "business/nomeAttivita?id_ristorante=" + loggedUser.getIdRistorante())
                .header("Authorization", loggedUser.getToken())
                .build();

        serverRequestAttivita(client, request);
    }

    public void getNumberOfNoticesToRead() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "avviso/numero-di-avvisi-da-leggere?id_utente=" + loggedUser.getIdUtente() + "&&id_ristorante=" + loggedUser.getIdRistorante())
                .header("Authorization", loggedUser.getToken())
                .build();

        serverRequestNumberOfNoticesToRead(client, request);
    }

    private void serverRequestNumberOfNoticesToRead(OkHttpClient client, Request request) {
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
                        BadgeDrawable badgeDrawable = homeActivity.getBadgeDrawable();
                        badgeDrawable.setNumber(Integer.parseInt(myResponse));
                    }
                });
            }
        });
    }

    public void setUserInfo(AccountFragment accountFragment, String nome, String cognome, String dataNascita) throws IOException {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nome", nome);
            jsonBody.put("cognome", cognome);
            jsonBody.put("dataNascita", dataNascita);
            jsonBody.put("idUtente", String.valueOf(loggedUser.getIdUtente()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody formBody = RequestBody.create(JSON, jsonBody.toString());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "user/account")
                .put(formBody)
                .header("Authorization", loggedUser.getToken())
                .build();

        serverRequest(accountFragment, client, request);
    }

    private String serverRequest(AccountFragment accountFragment, OkHttpClient client, Request request) {
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
                        if(myResponse.length() < 10){
                            accountFragment.setErrorLableOnErrorGeneric();
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                loggedUser.setDataNascita(jsonObject.getString("dataNascita"));
                                loggedUser.setNome(jsonObject.getString("nome"));
                                loggedUser.setCognome(jsonObject.getString("cognome"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            accountFragment.setErrorLableOnSuccessGeneric();
                        }
                    }
                });
            }
        });
        return null;
    }

    private void serverRequestAttivita(OkHttpClient client, Request request) {
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
                        JSONObject json = null;
                        try {
                            json = new JSONObject(myResponse);
                            Attivita attivita = new Attivita(homeActivity, json.getInt("idRistorante"), json.getString("nome"), json.getString("numeroTelefono"), json.getString("indirizzo"), json.getString("nomeImmagine"), json.getInt("idProprietario"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

}
