package com.example.ratatouille_android.models;

import com.example.ratatouille_android.views.DispensaActivity;

import java.io.Serializable;
import java.util.Observable;

public class Prodotto extends Observable implements Serializable {
    private int idProdotto,	idRistorante, stato;
    private String nome, descrizione, unitaMisura, categoria;
    private Double prezzo, quantita;

    public Prodotto(DispensaActivity dispensaActivity, int idProdotto, int idRistorante, String nome, int stato, String descrizione, Double prezzo, Double quantita, String unitaMisura, String categoria) {
        this.idProdotto = idProdotto;
        this.idRistorante = idRistorante;
        this.nome = nome;
        this.stato = stato;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.unitaMisura = unitaMisura;
        this.categoria = categoria;
        addObserver(dispensaActivity);
        setChanged();
        notifyObservers();
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public int getIdRistorante() {
        return idRistorante;
    }

    public int getStato() {
        return stato;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public String getCategoria() {
        return categoria;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public Double getQuantita() {
        return quantita;
    }
}
