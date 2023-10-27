package com.example.ratatouille_android.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.ModificaProdottoController;
import com.example.ratatouille_android.models.Prodotto;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.dialogs.DeleteDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class ModificaProdottoActivity extends AppCompatActivity {
    private User loggedUser;
    private ArrayList<Prodotto> dispensa;
    private String nomeProdotto;
    private ModificaProdottoController modificaProdottoController;
    private String categorie [] = {"Frutta", "Verdura", "Carne", "Pesce", "Uova", "LatteDerivati", "CerealiDerivati", "Legumi", "Altro"};
    private TextView errorLable, nomeProdottoSelezionato;
    private EditText descrizioneField, costoField, quantitaField;
    private Spinner categoria;
    private ToggleButton kgOrlt;
    private Button applicaButton, piubutton, menobutton, eliminabutton;
    private ImageView backButton;
    private FirebaseAnalytics analytics = MainActivity.analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_prodotto);

        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");
        nomeProdotto = (String) getIntent().getSerializableExtra("nomeProdotto");
        setDispensa((ArrayList<Prodotto>) getIntent().getSerializableExtra("dispensa"));

        modificaProdottoController = new ModificaProdottoController(this, loggedUser);

        nomeProdottoSelezionato = findViewById(R.id.nome_prodotto_selezionato);
        descrizioneField = findViewById(R.id.descrizione_input);
        costoField = findViewById(R.id.costo_input);
        quantitaField = findViewById(R.id.quantita_input);
        categoria = findViewById(R.id.spinner_categoria);
        errorLable = findViewById(R.id.error_lable);
        kgOrlt = findViewById(R.id.kg_or_lt);
        applicaButton = findViewById(R.id.ok_button);
        piubutton = findViewById(R.id.piu_button);
        menobutton = findViewById(R.id.meno_button);
        eliminabutton = findViewById(R.id.elimina_button);
        backButton = findViewById(R.id.back_button2);
        autocompilazionePagina(nomeProdottoSelezionato, descrizioneField, costoField, quantitaField, categoria, kgOrlt);

        View.OnClickListener piuOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float value = Float.parseFloat(String.valueOf(quantitaField.getText())) + 1;
                quantitaField.setText(value.toString());
            }
        };

        View.OnClickListener menoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float value = Float.parseFloat(String.valueOf(quantitaField.getText())) - 1;
                quantitaField.setText(value.toString());
            }
        };


        View.OnClickListener dispensaOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificaProdottoController.goToDispensaActivity();
            }
        };

        View.OnClickListener applicaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLog();
                String descrizione = descrizioneField.getText().toString();
                String costo = costoField.getText().toString();
                String quantita = quantitaField.getText().toString();
                String kg_or_lt = kgOrlt.getText().toString();
                String selectedCategoria = categoria.getSelectedItem().toString();
                int i = findIndexProductInDispensa();
                if (descrizione.equals("") || costo.equals("") || quantita.equals("")) {
                    errorLable.setText("Compilare tutti i campi");
                    errorLable.setTextColor(Color.RED);
                } else {
                    modificaProdottoController.modificaServerPiattoInfo(getDispensa().get(i).getIdProdotto(), nomeProdotto, descrizione, costo, quantita, kg_or_lt, selectedCategoria);
                }
            }
        };

        View.OnClickListener eliminaOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog dialog = new DeleteDialog(ModificaProdottoActivity.this.modificaProdottoController, ModificaProdottoActivity.this);
                dialog.show(getSupportFragmentManager(), "");
            }
        };

        menobutton.setOnClickListener(menoOnClickListener);
        eliminabutton.setOnClickListener(eliminaOnClickListener);
        piubutton.setOnClickListener(piuOnClickListener);
        applicaButton.setOnClickListener(applicaListener);
        backButton.setOnClickListener(dispensaOnClickListener);
    }

    private void firebaseLog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "prodotto_modificato");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "modified")  ;
        analytics.logEvent("ProdottoModificato", bundle);
    }

    private void autocompilazionePagina(TextView nomeProdottoSelezionato, EditText descrizioneField, EditText costoField, EditText quantitaField, Spinner categoria, ToggleButton kgOrlt) {
        nomeProdottoSelezionato.setText(nomeProdotto);
        Integer i = findIndexProductInDispensa();

        setUpSpinner(categoria);

        descrizioneField.setText(getDispensa().get(i).getDescrizione());
        costoField.setText(getDispensa().get(i).getPrezzo().toString());
        quantitaField.setText(getDispensa().get(i).getQuantita().toString());
        kgOrlt.setText(getDispensa().get(i).getUnitaMisura().toString());
        switch (getDispensa().get(i).getCategoria()){
            case "frutta":
                categoria.setSelection(0);
                break;
            case "verdura":
                categoria.setSelection(1);
                break;
            case "carne":
                categoria.setSelection(2);
                break;
            case "pesce":
                categoria.setSelection(3);
                break;
            case "uova":
                categoria.setSelection(4);
                break;
            case "latte_e_derivati":
                categoria.setSelection(5);
                break;
            case "cereali_e_derivati":
                categoria.setSelection(6);
                break;
            case "legumi":
                categoria.setSelection(7);
                break;
            default:
                categoria.setSelection(8);
                break;
        }


    }

    private void setUpSpinner(Spinner categoria) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter);
    }

    public int findIndexProductInDispensa() {
        int i = 0;
        for(Prodotto p : getDispensa()){
            if(p.getNome().equals(nomeProdotto)){
                return i;
            }
            i++;
        }
        return 0;
    }

    public void setErrorLableOnError(){
        errorLable.setText("Errore");
        errorLable.setTextColor(Color.RED);
    }

    public void setErrorLableOnCancel(){
        errorLable.setText("Operazione di eliminazione annullata");
        errorLable.setTextColor(Color.parseColor("#FF4500"));
    }

    public void setErrorLableOnSuccess(){
        errorLable.setText("Aggiornamento avvenuto con successo");
        errorLable.setTextColor(Color.GREEN);
    }

    public void setErrorLableOnMissed(){
        errorLable.setText("Compilare tutti i campi");
        errorLable.setTextColor(Color.RED);
    }

    public ArrayList<Prodotto> getDispensa() {
        return dispensa;
    }

    public void setDispensa(ArrayList<Prodotto> dispensa) {
        this.dispensa = dispensa;
    }
}