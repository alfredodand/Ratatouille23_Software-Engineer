package application.model;

import java.util.Observable;

import application.controller.AvvisiController;

@SuppressWarnings("deprecation")

public class Avviso extends Observable{
	private int id_avviso;
	private int id_utente;
	private int id_ristorante;
	private String testo;
	private String data_ora, autore;
	private AvvisiController avvisiController;
	
	public Avviso(AvvisiController avvisiController, int id_avviso, int id_utente, int id_ristorante, String testo, String data_ora, String autore) {
		this.id_avviso = id_avviso;
		this.id_utente = id_utente;
		this.id_ristorante = id_ristorante;
		this.testo = testo;
		this.data_ora = data_ora;
		this.autore = autore;
		addObserver(avvisiController);
		setChanged();
		notifyObservers();
	}

	public int getIdAvviso() {
		return id_avviso;
	}

	public void setIdAvviso(int id_avviso) {
		this.id_avviso = id_avviso;
	}

	public int getIdUtente() {
		return id_utente;
	}

	public void setIdUtente(int id_utente) {
		this.id_utente = id_utente;
	}

	public int getIdRistorante() {
		return id_ristorante;
	}

	public void setIdRistorante(int id_ristorante) {
		this.id_ristorante = id_ristorante;
	}

	public String getTesto() {
		return testo;
	}
	
	public String getAutore() {
		return autore;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getDataOra() {
		return data_ora;
	}

	public void setDataOra(String data_ora) {
		this.data_ora = data_ora;
	}
	
	
}
