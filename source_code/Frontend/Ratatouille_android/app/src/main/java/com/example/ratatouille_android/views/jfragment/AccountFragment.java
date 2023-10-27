package com.example.ratatouille_android.views.jfragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private User loggedUser;
    private EditText nomeField, cognomeField, dateField;
    private HomeActivity homeActivity;
    private TextView ruolo, email, aggiuntoDa, aggiutoInData, aggiuntoDaLabel, dataAggiuntaLabel, errore;
    private final int LUNGHEZZA_NOME_DB = 30, LUNGHEZZA_COGNOME_DB = 30;
    private FirebaseAnalytics analytics = MainActivity.analytics;

    public AccountFragment(HomeActivity homeActivity,User loggedUser){
        this.loggedUser = loggedUser;
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button btn = view.findViewById(R.id.confermaButton);
        btn.setOnClickListener(this);

        firebaseLog();

        nomeField = view.findViewById(R.id.nomeField);
        cognomeField = view.findViewById(R.id.cognomeField);
        dateField = view.findViewById(R.id.dataField);

        ruolo = view.findViewById(R.id.textViewRuolo);
        email = view.findViewById(R.id.textViewEmail);
        aggiuntoDa = view.findViewById(R.id.textViewAggiuntoDa);
        aggiutoInData =  view.findViewById(R.id.textViewAggiutoInData);
        aggiuntoDaLabel = view.findViewById(R.id.aggiuntoDaLabel);
        dataAggiuntaLabel = view.findViewById(R.id.dataAggiuntaDaLabel);
        errore = view.findViewById(R.id.textViewErrore);

        nomeField.setHint(loggedUser.getNome());
        cognomeField.setHint(loggedUser.getCognome());
        dateField.setHint(loggedUser.getDataNascita());
        ruolo.setText(loggedUser.getRuolo());
        email.setText(loggedUser.getEmail());
        aggiutoInData.setText(loggedUser.getDataAggiunta());

        if(aggiuntoDa.getText().toString().equals("")){
            aggiuntoDa.setText("");
            aggiuntoDaLabel.setText("");
            dataAggiuntaLabel.setText("Data iscrizione:");
        }

        return view;
    }

    private void firebaseLog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "account");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "account_clicked");
        analytics.logEvent("InAccountFragment", bundle);
    }

    private void firebaseLogAccountModified() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "accountModified");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "modified");
        analytics.logEvent("accountModified", bundle);
    }

    public boolean validate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        try {
            if(nomeField.getText().toString().equals("")){
                nomeField.setText(nomeField.getHint().toString());
            }
            if(cognomeField.getText().toString().equals("")){
                cognomeField.setText(cognomeField.getHint().toString());
            }
            if(dateField.getText().toString().equals("")) {
                dateField.setText(dateField.getHint().toString());
            }
            if(validate(dateField.getText().toString())){
                if(nomeField.getText().length() < LUNGHEZZA_NOME_DB && cognomeField.getText().length() < LUNGHEZZA_COGNOME_DB){
                    homeActivity.getHomeController().setUserInfo(this, nomeField.getText().toString(), cognomeField.getText().toString(), dateField.getText().toString());
                    homeActivity.setTextNomeCognome(nomeField.getText().toString() + " " + cognomeField.getText().toString());
                    nomeField.setHint(nomeField.getText().toString());
                    cognomeField.setHint(cognomeField.getText().toString());
                    dateField.setHint(dateField.getText().toString());
                    nomeField.setText("");
                    cognomeField.setText("");
                    dateField.setText("");
                    firebaseLogAccountModified();
                }else{
                    setErrorLableOnErrorNameOrSurname();
                }
            }else{
                setErrorLableOnErrorDate();
            }


        } catch (IOException e) {
            this.setErrorLableOnErrorGeneric();
            e.printStackTrace();
        }
    }

    public void setErrorLableOnErrorGeneric() {
        errore.setText("Errore");
        errore.setTextColor(Color.RED);
    }

    public void setErrorLableOnErrorNameOrSurname() {
        errore.setText("Nome o cognome troppo lungo");
        errore.setTextColor(Color.RED);
    }

    public void setErrorLableOnErrorDate(){
        errore.setText("Errore il formato della data è yyyy-mm-dd");
        errore.setTextColor(Color.RED);
    }

    public void setErrorLableOnSuccessGeneric(){
        errore.setText("Modifiche apportate con successo");
        errore.setTextColor(Color.parseColor("#008000"));
    }

}