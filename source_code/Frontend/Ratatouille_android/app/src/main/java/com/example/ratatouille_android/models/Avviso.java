package com.example.ratatouille_android.models;

import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.jfragment.NoticesFragment;

import java.io.Serializable;
import java.util.Observable;

public class Avviso extends Observable implements Serializable {

    private int idAvviso, idUtente, idRistorante;
    private String testo, dataOra, autore;
    private boolean isRead, isHidden;

    public Avviso(HomeActivity homeActivity, int idAvviso, int idUtente, int idRistorante, String testo, String dataOra, String autore, boolean isRead, boolean isHidden){
        this.idAvviso = idAvviso;
        this. idUtente = idUtente;
        this.idRistorante = idRistorante;
        this.testo = testo;
        this.dataOra = dataOra;
        this.autore = autore;
        this.isRead = isRead;
        this.isHidden = isHidden;
        addObserver(homeActivity);
        setChanged();
        notifyObservers();
    }

    public Avviso(HomeActivity homeActivity, int idUtente, int idAvviso){
        this.idAvviso = idAvviso;
        this. idUtente = idUtente;
    }

    public int getIdAvviso(){
        return idAvviso;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public int getIdRistorante() {
        return idRistorante;
    }

    public String getTesto() {
        return testo;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public String getAutore() {
        return autore;
    }

    public String getDataOra() {
        return dataOra;
    }
}
