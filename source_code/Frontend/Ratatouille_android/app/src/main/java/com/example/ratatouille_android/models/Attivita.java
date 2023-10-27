package com.example.ratatouille_android.models;

import com.example.ratatouille_android.views.HomeActivity;

import java.io.Serializable;
import java.util.Observable;

public class Attivita extends Observable implements Serializable {

    private int idRistorante, idProprietario;
    private String nome, telefono, indirizzo, nomeImmagine;

    public Attivita(HomeActivity homeActivity, int idRistorante, String nome, String telefono, String indirizzo, String nomeImmagine, int idProprietario){
        this.idRistorante = idRistorante;
        this.nome = nome;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.nomeImmagine = nomeImmagine;
        this.idProprietario = idProprietario;
        addObserver(homeActivity);
        setChanged();
        notifyObservers();
    }

    public int getIdRistorante(){
        return idRistorante;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getNomeImmagine() {
        return nomeImmagine;
    }

    public int getIdProprietario() {
        return idProprietario;
    }
}
