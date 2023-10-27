package com.ingsw.DAOinterface;

import java.util.ArrayList;

import com.ingsw.ratatouille.CategoriaMenu;
import com.ingsw.ratatouille.Menu;

public interface MenuDAOint {
	
	public ArrayList<Menu> getMenu();
	public ArrayList<Menu> getMenuFromRestaurant(int id_ristorante);
	public ArrayList<Menu> getMenuCategories(Integer idRistorante, String categoria);
	public ArrayList<Menu> getMenuPlateInRestaurant(Integer id_ristorante, String nomePiatto);
	public ArrayList<Menu> getMenuPlate(String nomePiatto);
	public boolean createPlate(Integer idRistorante, String categoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua);
	public boolean createCategoria(Integer idRistorante, String nomeNuovaCategoria);
	public int getCategoryIdOfResturantFromCategoryName(Integer idRistorante, String categoria);
	public Menu addSecondLanguage(Integer idRistorante, int idProdotto, String nomeSecondaLingua, String descrizoineSecondaLingua);

	public boolean deletePlate(Integer idPiatto);

	public boolean updatePosizionePiatto(Integer idPiatto, Integer posizione);

	public boolean deleteSortingMenu(Integer idRistorante);

	public Menu getPiattoById(Integer idPiatto);

	boolean updatePlate(Integer idPiatto, Integer idRistorante, String categoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua);
}
