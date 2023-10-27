package com.ingsw.ratatouille;

import org.springframework.stereotype.Component;

@Component
public class User {
	// same order of database columns
	private int idUtente;
	private String nome, cognome, dataNascita, email, password, ruolo;
	private boolean isFirstAccess;
	private int aggiunto_da;
	private String data_aggiunta;
	private int id_ristorante;
		
	public User(){}
	
	public User(int id, String nome, String cognome, String dataNascita, String email, String password, String ruolo, boolean isFirstAccess, int aggiuntoDa, String dataAggiunta, int idRistorante){
		this.idUtente = id;
		this.cognome = cognome;
		this.nome = nome;
		this.dataNascita = dataNascita;
		this.email = email;
		this.password = password;
		this.ruolo = ruolo;
		this.isFirstAccess = isFirstAccess;
		this.aggiunto_da = aggiuntoDa;
		this.data_aggiunta = dataAggiunta;
		this.id_ristorante = idRistorante;
	}


	public int getIdUtente() {
		return idUtente;
	}

	public String getPassword() {
		return password;
	}

	public String getNome() {
		return nome;
	}

	
	public String getCognome() {
		return cognome;
	}


	public String getDataNascita() {
		return dataNascita;
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

}

