package com.ingsw.ratatouille;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ingsw.ratatouille.Prodotto.categoriaProdotto;
import com.ingsw.ratatouille.Prodotto.unitaMisura;

public class Prodotto {
	
	private int idProdotto,	idRistorante, stato;
	private String nome, descrizione, unitaMisura, categoria;
	private Double prezzo, quantita;
	
	public enum categoriaProdotto {Frutta, Verdura, Carne, Pesce, Uova, LatteDerivati, CerealiDerivati, Legumi, Altro};
	public enum unitaMisura {Kg, Lt};

	public Prodotto(){}
	public Prodotto(int idProdotto, int idRistorante, String nome, int stato, String descrizione, Double prezzo2, Double quantita2, String unitaMisura, String categoria) {
		this.idProdotto = idProdotto;
		this.idRistorante = idRistorante;
		this.nome = nome;
		this.stato = stato;
		this.descrizione = descrizione;
		this.prezzo = prezzo2;
		this.quantita = quantita2;
		this.unitaMisura = unitaMisura;
		this.categoria = categoria;
		
	}
	
	@JsonIgnore
	public ArrayList<String> getCategories() {
		ArrayList<String> categorie = new ArrayList<String>();
		
		for(categoriaProdotto categoria : categoriaProdotto.values()) {
			categorie.add(categoria.toString());
		}
		return categorie;
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
