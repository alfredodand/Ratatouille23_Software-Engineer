package com.example.ratatouille_android.models;

import java.io.Serializable;
import java.util.Observable;

public class User extends Observable implements Serializable {
    private int id_utente;
    private String nome, cognome, data_nascita, email, password, ruolo;
    private boolean isFirstAccess;
    private int aggiunto_da;
    private String data_aggiunta;
    private int id_ristorante;
    private String token;
    private String tk_expiration_timestamp;

    public User(){}

    public User(int id, String nome, String cognome, String dataNascita, String email, String password, String ruolo, boolean isFirstAccess, int aggiuntoDa, String dataAggiunta, int idRistorante, String token, String tk_expiration_timestamp){
        this.id_utente = id;
        this.cognome = cognome;
        this.nome = nome;
        this.data_nascita = dataNascita;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.isFirstAccess = isFirstAccess;
        this.aggiunto_da = aggiuntoDa;
        this.data_aggiunta = dataAggiunta;
        this.id_ristorante = idRistorante;
        this.token = token;
        this.tk_expiration_timestamp = tk_expiration_timestamp;
        setChanged();
        notifyObservers();
    }

    public int getIdUtente() { return id_utente; }

    public int getIdRistorante() {
        return id_ristorante;
    }

    public String getToken() {
        return token;
    }

    public String getNome() { return nome; }

    public String getCognome() { return cognome; }

    public String getDataNascita() { return data_nascita; }

    public String getRuolo() { return ruolo; }

    public String getEmail() { return email; }

    public String getDataAggiunta() { return data_aggiunta; }

    public int getAggiunto_da() { return aggiunto_da; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascita(String data_nascita) {
        this.data_nascita = data_nascita;
    }
}


