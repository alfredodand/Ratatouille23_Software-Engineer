package application.model;

import java.util.Observable;

import application.controller.MenuController;

@SuppressWarnings("deprecation")
public class CategoriaMenu extends Observable{
	
	private int idCategoria, idRistorante;
	private Integer posizione;
	private String nome;
	
	public CategoriaMenu() {}
	
	public CategoriaMenu(MenuController menuController, int idCategoria, int idRistorante, String nome,  Integer posizione){
		this.nome = nome;
		this.idCategoria = idCategoria;
		this.idRistorante = idRistorante;
		this.posizione = posizione;
		addObserver(menuController);
		setChanged();
		notifyObservers();
	}
	
	public CategoriaMenu(int idCategoria, int idRistorante, String nome, Integer posizione){
		this.nome = nome;
		this.idCategoria = idCategoria;
		this.idRistorante = idRistorante;
		this.posizione = posizione;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
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
	
	public Integer getPosizione() {
		return posizione;
	}
	
}
