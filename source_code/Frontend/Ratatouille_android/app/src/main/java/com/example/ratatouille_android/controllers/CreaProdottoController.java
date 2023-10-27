package com.example.ratatouille_android.controllers;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.CreaProdottoActivity;
import com.example.ratatouille_android.views.DispensaActivity;
import com.example.ratatouille_android.views.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreaProdottoController {
    private CreaProdottoActivity creaProdottoActivity;
    private User loggedUser;
    private Integer resultIndex = 0;
    private String[] categorie = {"Frutta", "Verdura", "Carne", "Pesce", "Uova", "LatteDerivati", "CerealiDerivati", "Legumi", "Altro"};
    private String urlOpenFood = "https://it.openfoodfacts.org/cgi/search.pl?search_terms=";
    private String url = MainActivity.address + "/dispensa/newProduct";

    public CreaProdottoController(CreaProdottoActivity creaProdottoActivity, User loggedUser) {
        this.creaProdottoActivity = creaProdottoActivity;
        this.loggedUser = loggedUser;
    }

    public void goToDispensaActivity() {
        Intent switchActivityIntent = new Intent(creaProdottoActivity, DispensaActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        creaProdottoActivity.startActivity(switchActivityIntent);
    }

    public void getInfoProdotto(String nomeProdotto) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlOpenFood + nomeProdotto + "&&json=true")
                .build();
        openDataRequest(client, request, nomeProdotto);
    }

    private void openDataRequest(OkHttpClient client, Request request, String nomeProdotto) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                creaProdottoActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String nome = nomeProdotto, categoria = "", descrizione = "", kg_or_lt = "kg";

                            JSONObject jsonObject1 = new JSONObject(myResponse);
                            JSONArray nestedJsonA = jsonObject1.getJSONArray("products");
                            JSONObject jsonObject = nestedJsonA.getJSONObject(resultIndex);
                            categoria = jsonObject.getString("categories");

                            nome = getNomeFromJson(nome, jsonObject, nomeProdotto);
                            descrizione = getDescrizioneFromJson(jsonObject);
                            kg_or_lt = getQuantitaFromJson(kg_or_lt, jsonObject);
                            categoria = getCategoriaFromJson(categoria);

                            creaProdottoActivity.setNomeInput(nome);
                            creaProdottoActivity.setCategoriaInput(categoria);
                            creaProdottoActivity.setDescrizioneInput(descrizione);
                            creaProdottoActivity.setKg_or_ltInput(kg_or_lt);
                            creaProdottoActivity.setErrorLableAutoCompilationOnSuccess();
                            creaProdottoActivity.setCaricamentoOnEnd();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @NonNull
    private String getCategoriaFromJson(String categoria) {
        if(categoria.contains("Beverages,") || categoria.contains("Bevande,") || categoria.contains("Acque"))
            categoria = "bibite";
        else if(categoria.contains("Fruits,"))
            categoria = "frutta";
        else if(categoria.contains("végétaux") || categoria.contains("vegetables") )
            categoria = "verdura";
        else if(categoria.contains("Meats,"))
            categoria = "carne";
        else if(categoria.contains("Pesci,") || categoria.contains("Seafood,"))
            categoria = "pesce";
        else if(categoria.contains("Egg"))
            categoria = "uova";
        else if(categoria.contains("Fermented milk products,") || categoria.contains("Cheeses,") || categoria.contains("Mozzarella,") || categoria.contains("Milk") || categoria.contains("latte"))
            categoria = "latte_e_derivati";
        else if(categoria.contains("Pasta") || categoria.contains("Cereals"))
            categoria = "cereali_e_derivati";
        else
            categoria = "altro";
        return categoria;
    }

    private String getQuantitaFromJson(String kg_or_lt, JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("quantity")) {
            kg_or_lt = jsonObject.getString("quantity");
            if (kg_or_lt.contains("L") || kg_or_lt.contains("l"))
                kg_or_lt = "lt";
        }
        return kg_or_lt;
    }

    private String changeCategoriaSignature(String categoria){
        if(categorie[0].equals(categoria))
            return "frutta";
        if(categorie[1].equals(categoria))
            return "verdura";
        if(categorie[2].equals(categoria))
            return "carne";
        if(categorie[3].equals(categoria))
            return "pesce";
        if(categorie[4].equals(categoria))
            return "uova";
        if(categorie[5].equals(categoria))
            return "latte_e_derivati";
        if(categorie[6].equals(categoria))
            return "cereali_e_derivati";
        if(categorie[7].equals(categoria))
            return "legumi";

        return "altro";
    }

    @NonNull
    private String getDescrizioneFromJson(JSONObject jsonObject) throws JSONException {
        String descrizione;
        if(jsonObject.has("quantity") && jsonObject.has("brands") && jsonObject.has("countries") && jsonObject.has("packaging")) {
            descrizione = "Quantità: " + jsonObject.getString("quantity") + "\nBrands: " + jsonObject.getString("brands") + "\nPaesi di produzione: " + jsonObject.getString("countries") + "\nConfezionamento: " + jsonObject.getString("packaging");
            descrizione = descrizione.replace("'", " ");
        }else{
            descrizione = "Non trovata";
        }
        return descrizione;
    }

    private String getNomeFromJson(String nome, JSONObject jsonObject, String nomeProdotto) throws JSONException {
        if (jsonObject.has("generic_name_it")) {
            nome = jsonObject.getString("generic_name_it");
            if (nome.equals(""))
                nome = nomeProdotto;
            nome = nome.replace("'", "");
        }
        return nome;
    }

    public void setResultIndex(Integer index){
        if(index == 24)
            index = 0;
        this.resultIndex = index;
    }

    public Integer getResultIndex() {
        return resultIndex;
    }


    public void salvaProdotto(String nomeProdotto, String descrizione, String costo, String quantita, String categoria, String kgOrLt) {
        Float stato = Float.parseFloat(String.valueOf(quantita));
        Integer stato2 = stato.intValue();
        OkHttpClient client = new OkHttpClient();
        Log.v("Prova", categoria);
        RequestBody formBody = new FormBody.Builder()
                .add("idRistorante", String.valueOf(loggedUser.getIdRistorante()))
                .add("nome", nomeProdotto)
                .add("stato", stato2.toString())
                .add("descrizione", descrizione)
                .add("prezzo", costo)
                .add("quantita", quantita)
                .add("unitaMisura", kgOrLt.toLowerCase(Locale.ROOT))
                .add("categoria", changeCategoriaSignature(categoria))
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
                creaProdottoActivity.setErrorLableOnError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                if(!myResponse.contains("status")) {
                    creaProdottoActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creaProdottoActivity.setErrorLableOnSuccess();
                            creaProdottoActivity.resetField();
                        }
                    });
                }

            }
        });
    }
}

