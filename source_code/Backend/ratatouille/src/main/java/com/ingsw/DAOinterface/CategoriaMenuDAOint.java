package com.ingsw.DAOinterface;

import java.util.ArrayList;

import com.ingsw.ratatouille.CategoriaMenu;

public interface CategoriaMenuDAOint {


	public ArrayList<CategoriaMenu> getMenuCategories();
	public ArrayList<CategoriaMenu> getMenuCategoriesFromRestaurant(int idRistorante);

    public boolean updatePosizioneCategoria(Integer idCategoria, Integer posizione);

    public boolean deleteSortingMenu(Integer idRistorante);

    public boolean createCategoria(Integer idRistorante, String nomeNuovaCategoria);
}
