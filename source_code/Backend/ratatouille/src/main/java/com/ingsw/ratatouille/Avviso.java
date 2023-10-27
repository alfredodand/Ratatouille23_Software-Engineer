package com.ingsw.ratatouille;

public class Avviso {
	private int id_avviso;
	private int id_utente;
	private int id_ristorante;
	private String testo;
	private String data_ora, autore;
	
	public Avviso(int id_avviso, int id_utente, int id_ristorante, String testo, String data_ora) {
		this.id_avviso = id_avviso;
		this.id_utente = id_utente;
		this.id_ristorante = id_ristorante;
		this.testo = testo;
		this.data_ora = data_ora;
	}
	
	public Avviso(int id_avviso, int id_utente, int id_ristorante, String testo, String data_ora, String autore) {
		this.id_avviso = id_avviso;
		this.id_utente = id_utente;
		this.id_ristorante = id_ristorante;
		this.testo = testo;
		this.data_ora = data_ora;
		this.autore = autore;
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
	
	 @Override
	 public boolean equals(Object obj) {
		 if(obj instanceof Avviso) {
			 Avviso avviso = (Avviso) obj;
		     if(avviso.getIdAvviso() == getIdAvviso() && avviso.getIdRistorante() == getIdRistorante() && avviso.getTesto().equals(getTesto()) && avviso.getAutore().equals(avviso.getAutore())) {
		    	 return true;
		     }
		 }
	     return false;

	 }
	
	
}
