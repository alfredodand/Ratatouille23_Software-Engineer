package com.example.ratatouille_android.controllers;

import android.content.Intent;

import android.util.Log;


import com.example.ratatouille_android.models.Avviso;
import com.example.ratatouille_android.models.Prodotto;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.AvvisiNascostiActivity;
import com.example.ratatouille_android.views.DispensaActivity;
import com.example.ratatouille_android.views.FirstAccessActivity;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;
import com.example.ratatouille_android.views.jfragment.NoticesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoticesController {

    HomeActivity homeActivity;
    User loggedUser;
    String url = MainActivity.address;
    ArrayList<Avviso> avvisiLetti = new ArrayList<Avviso>();
    ArrayList<Avviso> avvisiNascosti = new ArrayList<Avviso>();
    ArrayList<Avviso> avvisi = new ArrayList<Avviso>();

    public NoticesController(HomeActivity homeActivity, User loggedUser) {
        this.loggedUser = loggedUser;
        this.homeActivity = homeActivity;
        getReadNoticeFromServer();
        getHiddenNoticeFromServer();
        PrimeThread p = new PrimeThread(this);
        p.start();
        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getNoticeFromServer(){
        try {
            runNotices(loggedUser.getIdRistorante(), loggedUser.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHiddenNoticeFromServer(){
        try {
            runHiddenNotices(loggedUser.getIdRistorante(), loggedUser.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getReadNoticeFromServer(){
        try {
            runReadNotices(loggedUser.getIdRistorante(), loggedUser.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void markAsReadNotice(Integer id_avviso){
        try {
            runMarkNoticeAsRead(id_avviso, loggedUser.getIdRistorante());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsHideNotice(Integer id_avviso) {
        try {
            runMarkNoticeAsHide(id_avviso);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsNotReadNotice(Integer id_avviso){
        try {
            runMarkNoticeAsNotRead(id_avviso);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runMarkNoticeAsHide(Integer id_avviso) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id_avviso", String.valueOf(id_avviso))
                .build();
        Request request = new Request.Builder()
                .url(url + "/avviso/segna-come-nascosto/" + id_avviso + "?idUtente=" + loggedUser.getIdUtente())
                .post(formBody)
                .header("Authorization", loggedUser.getToken())
                .build();
        serverRequestNoticeHide(client, request);
    }

    private void runMarkNoticeAsNotRead(Integer id_avviso) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id_avviso", String.valueOf(id_avviso))
                .build();
        Request request = new Request.Builder()
                .url(url + "/avviso/segna-come-non-letto/" + id_avviso + "?idUtente=" + loggedUser.getIdUtente())
                .post(formBody)
                .header("Authorization", loggedUser.getToken())
                .build();
        serverRequestNotice(client, request);
    }

    void runMarkNoticeAsRead(Integer id_avviso, Integer id_ristorante){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id_avviso", String.valueOf(id_avviso))
                .add("id_ristorante", String.valueOf(id_ristorante))
                .build();
        Request request = new Request.Builder()
                .url(url + "/avviso/segna-come-letto/" + id_avviso + "?idUtente=" + loggedUser.getIdUtente())
                .post(formBody)
                .header("Authorization",  loggedUser.getToken())
                .build();
        serverRequestNotice(client, request);
    }

    private void serverRequestNotice(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
            }
        });
    }

    private void serverRequestNoticeHide(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                try {
                    Log.v("Prova", myResponse);
                    JSONObject json = new JSONObject(myResponse);
                    Avviso a = new Avviso(homeActivity, json.getInt("idUtente"), json.getInt("idAvviso"));
                    avvisiNascosti.add(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void runNotices(Integer id_ristorante, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/avvisi?id_ristorante=" + id_ristorante.toString())
                .header("Authorization", token)
                .build();

        serverRequest(client, request);
    }

    void runReadNotices(Integer id_ristorante, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/avvisi-viewed/" + loggedUser.getIdUtente())
                .header("Authorization", token)
                .build();

        serverRequestReadNotices(client, request);
    }

    void runHiddenNotices(Integer id_ristorante, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/avvisi-hidden/" + loggedUser.getIdUtente())
                .header("Authorization", token)
                .build();

        serverRequestHiddenNotices(client, request);
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
                final JSONArray[] jsonA = {null};
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonA[0] = new JSONArray(myResponse);
                            for (int i = 0; i < jsonA[0].length(); i++) {
                                JSONObject json = jsonA[0].getJSONObject(i);
                                Log.v("Prova1",  json.getString("testo") + " " + containsIdAvvisoNascosti(json) + " " + containsIdAvvisoLetti(json));
                                Avviso a = new Avviso(homeActivity, json.getInt("idAvviso"), json.getInt("idUtente"), json.getInt("idRistorante"), json.getString("testo"), json.getString("dataOra"), json.getString("autore"), containsIdAvvisoLetti(json), containsIdAvvisoNascosti(json));
                                avvisi.add(a);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void serverRequestReadNotices(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                final JSONArray[] jsonA = {null};
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonA[0] = new JSONArray(myResponse);
                            for (int i = 0; i < jsonA[0].length(); i++) {
                                JSONObject json = jsonA[0].getJSONObject(i);
                                Avviso a = new Avviso(homeActivity, json.getInt("idUtente"), json.getInt("idAvviso"));
                                avvisiLetti.add(a);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void serverRequestHiddenNotices(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                final JSONArray[] jsonA = {null};
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonA[0] = new JSONArray(myResponse);
                            for (int i = 0; i < jsonA[0].length(); i++) {
                                JSONObject json = jsonA[0].getJSONObject(i);
                                Avviso a = new Avviso(homeActivity, json.getInt("idUtente"), json.getInt("idAvviso"));
                                Log.v("Prova avvisi nascosti: ", String.valueOf(a.getIdAvviso()));
                                avvisiNascosti.add(a);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private boolean containsIdAvvisoLetti(JSONObject json) throws JSONException {
        for(Avviso a : avvisiLetti){
            if(a.getIdAvviso() == json.getInt("idAvviso")){
                return true;
            }
        }
        return false;
    }

    private boolean containsIdAvvisoNascosti(JSONObject json) throws JSONException {
        for(Avviso a : avvisiNascosti){
            if(a.getIdAvviso() == json.getInt("idAvviso")){
                return true;
            }
        }
        return false;
    }

    public void goToHideMessageActivity(){
        Intent switchActivityIntent = new Intent(homeActivity, AvvisiNascostiActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        switchActivityIntent.putExtra("avvisiNascosti", avvisiNascosti);
        switchActivityIntent.putExtra("avvisi", avvisi);
        homeActivity.startActivity(switchActivityIntent);
        homeActivity.finish();
    }



}

class PrimeThread extends Thread {

    NoticesController controller;
    PrimeThread(NoticesController controller) {
        this.controller = controller;
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.getNoticeFromServer();
    }
}
