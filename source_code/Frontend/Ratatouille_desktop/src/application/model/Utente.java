package application.model;

import java.util.Observable;


@SuppressWarnings("deprecation")
public class Utente extends Observable{
	private String token;
	private String tk_expiration_timestamp;
	private int id_utente;
	private String nome, cognome, data_nascita, email, ruolo; 
	private boolean isFirstAccess;
	private int aggiunto_da;
	private String data_aggiunta;
	private int id_ristorante;

	public Utente(int id, String nome, String cognome, String dataNascita, String email, String ruolo, boolean isFirstAccess, int aggiuntoDa, String dataAggiunta, int idRistorante, String token, String expirationTime) {
		this.id_utente = id;
		this.cognome = cognome;
		this.nome = nome;
		this.data_nascita = dataNascita;
		this.email = email;
		this.ruolo = ruolo;
		this.isFirstAccess = isFirstAccess;
		this.aggiunto_da = aggiuntoDa;
		this.data_aggiunta = dataAggiunta;
		this.id_ristorante = idRistorante;
		this.token = token;
		this.tk_expiration_timestamp = expirationTime;
	}
	
	public Utente(int id, String nome, String cognome, String email, String ruolo) {
		this.id_utente = id;
		this.cognome = cognome;
		this.nome = nome;
		this.email = email;
		this.ruolo = ruolo;
	}

	public Utente() {}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdUtente() {
		return id_utente;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getDataNascita() {
		return data_nascita;
	}

	public String getEmail() {
		return email;
	}

	public String getRuolo() {
		return ruolo;
	}

	public boolean isFirstAccess() {
		return isFirstAccess;
	}

	public int getAggiuntoDa() {
		return aggiunto_da;
	}

	public String getDataAggiunta() {
		return data_aggiunta;
	}

	public int getIdRistorante() {
		return id_ristorante;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTk_expiration_timestamp() {
		return tk_expiration_timestamp;
	}

	public void setTk_expiration_timestamp(String tk_expiration_timestamp) {
		this.tk_expiration_timestamp = tk_expiration_timestamp;
	}
	
}
