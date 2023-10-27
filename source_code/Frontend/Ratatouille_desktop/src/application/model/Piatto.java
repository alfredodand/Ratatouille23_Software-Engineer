package application.model;

import java.util.Observable;

import application.controller.MenuController;


@SuppressWarnings("deprecation")
public class Piatto extends Observable{

	private String nome, descrizione, allergeni, nomeSecondaLingua, descrizioneSecondaLingua;
	private int idElemento, idRistorante, idCategoria;
	Integer posizione;
	private float prezzo;
	MenuController menuController;
	
	public Piatto(MenuController menuController, int idElemento, int idRistorante, int idCategoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua, Integer posizione) {
		this.idElemento = idElemento;
		this.idRistorante = idRistorante;
		this.idCategoria = idCategoria;
		this.nome = nome;
		this.prezzo = prezzo;
		this.descrizione = descrizione;
		this.allergeni = allergeni;
		this.nomeSecondaLingua = nomeSecondaLingua;
		this.descrizioneSecondaLingua = descrizioneSecondaLingua;
		this.posizione = posizione;
		addObserver(menuController);
		setChanged();
		notifyObservers();
	}
	
	public Piatto(int idElemento, int idRistorante, int idCategoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua, Integer posizione) {
		this.idElemento = idElemento;
		this.idRistorante = idRistorante;
		this.idCategoria = idCategoria;
		this.nome = nome;
		this.prezzo = prezzo;
		this.descrizione = descrizione;
		this.allergeni = allergeni;
		this.nomeSecondaLingua = nomeSecondaLingua;
		this.descrizioneSecondaLingua = descrizioneSecondaLingua;
		this.posizione = posizione;
	}

	public String getNome() {
		return nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getAllergeni() {
		return allergeni;
	}

	public String getNomeSecondaLingua() {
		return nomeSecondaLingua;
	}

	public String getDescrizioneSecondaLingua() {
		return descrizioneSecondaLingua;
	}

	public int getIdElemento() {
		return idElemento;
	}

	public int getIdRistorante() {
		return idRistorante;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public float getPrezzo() {
		return prezzo;
	}
	
	public Integer getPosizione() {
		return posizione;
	}

}
