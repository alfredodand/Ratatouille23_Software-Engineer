package com.example.ratatouille_android.controllers;
import android.content.Intent;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.models.Prodotto;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.CreaProdottoActivity;
import com.example.ratatouille_android.views.DispensaActivity;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;
import com.example.ratatouille_android.views.ModificaProdottoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DispensaController {
    private DispensaActivity dispensaActivity;
    private User loggedUser;
    private String url = MainActivity.address + "/dispensa";
    private ArrayList<Prodotto> dispensa = new ArrayList<Prodotto>();

    public DispensaController(){ }

    public DispensaController(DispensaActivity dispensaActivity, User loggedUser) {
        this.dispensaActivity = dispensaActivity;
        this.loggedUser = loggedUser;
    }

    public void getProductFromServer(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "?id_ristorante=" + loggedUser.getIdRistorante())
                .header("Authorization", loggedUser.getToken())
                .build();
        serverRequestWithSavingOfStatus(client, request);
    }

    public void getProductByNameFromServer(String productName){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/prodotto?id_ristorante=" + loggedUser.getIdRistorante() + "&&nomeProdotto="+productName)
                .header("Authorization", loggedUser.getToken())
                .build();

        serverRequest(client, request);
    }

    public void  getProductByCategoriaFromServer(String category){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "/categoria?id_ristorante=" + loggedUser.getIdRistorante() + "&&categoria="+category)
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
                dispensaActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonA = null;
                        try {
                            jsonA = new JSONArray(myResponse);
                            for (int i = 0; i < jsonA.length(); i++){
                                JSONObject json = jsonA.getJSONObject(i);
                                Prodotto p = new Prodotto(dispensaActivity, json.getInt("idProdotto"), json.getInt("idRistorante"), json.getString("nome"), json.getInt("stato"), json.getString("descrizione"), json.getDouble("prezzo"), json.getDouble("quantita"), json.getString("unitaMisura"),  json.getString("categoria"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void serverRequestWithSavingOfStatus(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                dispensaActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonA = null;
                        try {
                            jsonA = new JSONArray(myResponse);
                            for (int i = 0; i < jsonA.length(); i++){
                                JSONObject json = jsonA.getJSONObject(i);
                                Prodotto p = new Prodotto(dispensaActivity, json.getInt("idProdotto"), json.getInt("idRistorante"), json.getString("nome"), json.getInt("stato"), json.getString("descrizione"), json.getDouble("prezzo"), json.getDouble("quantita"), json.getString("unitaMisura"),  json.getString("categoria"));
                                dispensa.add(p);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void goToMenuActivity(){
        Intent switchActivityIntent = new Intent(dispensaActivity, HomeActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        switchActivityIntent.putExtra("frgToLoad", R.id.functions);
        dispensaActivity.startActivity(switchActivityIntent);
        dispensaActivity.finish();
    }

    public void goToModificaProdottoActivity(String nomeProdotto){
        Intent switchActivityIntent = new Intent(dispensaActivity, ModificaProdottoActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        switchActivityIntent.putExtra("nomeProdotto", nomeProdotto);
        switchActivityIntent.putExtra("dispensa", dispensa);
        dispensaActivity.startActivity(switchActivityIntent);
        dispensaActivity.finish();
    }

    public void goToCreaProdottoActivity() {
        Intent switchActivityIntent = new Intent(dispensaActivity, CreaProdottoActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        dispensaActivity.startActivity(switchActivityIntent);
        dispensaActivity.finish();
    }

}
