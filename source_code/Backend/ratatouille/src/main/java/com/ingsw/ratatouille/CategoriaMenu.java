package com.ingsw.ratatouille;

public class CategoriaMenu {
	
	private int idCategoria, idRistorante, posizione;
	private String nome;
	
	public CategoriaMenu() { }
	
	public CategoriaMenu(int idCategoria, int idRistorante, String nome, int posizione){
		this.nome = nome;
		this.idCategoria = idCategoria;
		this.idRistorante = idRistorante;
		this.posizione = posizione;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public int getIdRistorante() {
		return idRistorante;
	}

	public String getNome() {
		return nome;
	}

	public int getPosizione() {
		return posizione;
	}
}
