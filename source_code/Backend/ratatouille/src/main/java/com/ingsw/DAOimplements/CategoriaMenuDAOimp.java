package com.ingsw.DAOimplements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ingsw.DAOinterface.CategoriaMenuDAOint;
import com.ingsw.ratatouille.CategoriaMenu;
import com.ingsw.ratatouille.DatabaseConnection;

public class CategoriaMenuDAOimp implements CategoriaMenuDAOint {
	DatabaseConnection db;
	
	public CategoriaMenuDAOimp(DatabaseConnection db){
		this.db = db;
	}

	
	public ArrayList<CategoriaMenu> getMenuCategories() {
		ArrayList<CategoriaMenu> categoria = new ArrayList<CategoriaMenu>();
		String query = "SELECT * FROM categorie_menu";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				CategoriaMenu menuTmp = new CategoriaMenu(rs.getInt("id_categoria"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("posizione_categoria"));
				categoria.add(menuTmp);
			}
			return categoria;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}
	
	
	public ArrayList<CategoriaMenu> getMenuCategoriesFromRestaurant(int idRistorante){
		ArrayList<CategoriaMenu> categoria = new ArrayList<CategoriaMenu>();
		String query = "SELECT * FROM categorie_menu WHERE id_ristorante = " + idRistorante;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				CategoriaMenu menuTmp = new CategoriaMenu(rs.getInt("id_categoria"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("posizione_categoria"));
				categoria.add(menuTmp);
			}
			return categoria;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}

	private CategoriaMenu getCategoriaFromId(Integer idCategoria) {
		String query = "SELECT * FROM categorie_menu WHERE id_categoria = " + idCategoria;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				return new CategoriaMenu(rs.getInt("id_categoria"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("posizione_categoria"));
			}
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}

	@Override
	public boolean updatePosizioneCategoria(Integer idCategoria, Integer posizione) {
		String query = "UPDATE categorie_menu SET posizione_categoria = " + posizione + " WHERE id_categoria = " + idCategoria;
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

	@Override
	public boolean deleteSortingMenu(Integer idRistorante) {
		String query = "UPDATE categorie_menu SET posizione_categoria = NULL WHERE id_ristorante = " + idRistorante;
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

	@Override
	public boolean createCategoria(Integer idRistorante, String nomeNuovaCategoria) {
		String query = "INSERT INTO categorie_menu(id_categoria, id_ristorante, nome, posizione_categoria) VALUES (default, " + idRistorante + ", '" + nomeNuovaCategoria + "', NULL)";
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}




}
