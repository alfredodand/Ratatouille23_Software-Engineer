package com.ingsw.ratatouille;

public class LoggedUser extends User{

	private String token;
	private String tk_expiration_timestamp;

	public LoggedUser(int id, String nome, String cognome, String dataNascita, String email, String password, String ruolo, boolean isFirstAccess, int aggiuntoDa, String dataAggiunta, int idRistorante, String token, String expirationTime) {
		super(id, nome, cognome, dataNascita, email, password, ruolo, isFirstAccess, aggiuntoDa, dataAggiunta, idRistorante);
		this.token = token;
		this.tk_expiration_timestamp = expirationTime;
	}

	public LoggedUser() {}

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
