package com.ingsw.DAOimplements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ingsw.DAOinterface.MenuDAOint;
import com.ingsw.ratatouille.CategoriaMenu;
import com.ingsw.ratatouille.DatabaseConnection;
import com.ingsw.ratatouille.Menu;
import com.ingsw.ratatouille.User;

public class MenuDAOimp implements MenuDAOint {
	
	DatabaseConnection db;
	
	public MenuDAOimp(DatabaseConnection db){
		this.db = db;
	}
	

	public ArrayList<Menu> getMenu() {
		ArrayList<Menu> menu = new ArrayList<Menu>();
		String query = "SELECT * FROM elementi_menu";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Menu menuTmp = new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
				menu.add(menuTmp);
			}
			return menu;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}
	
	public ArrayList<Menu> getMenuFromRestaurant(int idRistorante) {
		ArrayList<Menu> menu = new ArrayList<Menu>();
		String query = "SELECT * FROM elementi_menu WHERE id_ristorante = " + idRistorante;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Menu menuTmp = new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
				menu.add(menuTmp);
			}
			return menu;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}
	
	public ArrayList<Menu> getMenuCategories(Integer idRistorante, String categoria){
		ArrayList<Menu> menu = new ArrayList<Menu>();
		
		String query = "SELECT * FROM elementi_menu JOIN categorie_menu ON elementi_menu.id_categoria = categorie_menu.id_categoria WHERE elementi_menu.id_ristorante = " + idRistorante + " AND  categorie_menu.nome LIKE '%" + categoria + "%'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Menu menuTmp = new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
				menu.add(menuTmp);
			}
			return menu;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}


	public ArrayList<Menu> getMenuPlateInRestaurant(Integer idRistorante, String nomePiatto) {
		ArrayList<Menu> menu = new ArrayList<Menu>();
		String query = "SELECT * FROM elementi_menu WHERE id_ristorante = " + idRistorante + " AND nome ILIKE '" + nomePiatto + "%'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Menu menuTmp = new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
				menu.add(menuTmp);
			}
			return menu;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}


	public ArrayList<Menu> getMenuPlate(String nomePiatto) {
		ArrayList<Menu> menu = new ArrayList<Menu>();
		String query = "SELECT * FROM elementi_menu WHERE nome LIKE '%" + nomePiatto + "%'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Menu menuTmp = new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
				menu.add(menuTmp);
			}
			return menu;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}

	
	public int getCategoryIdOfResturantFromCategoryName(Integer idRistorante, String categoria) {
		String query = "SELECT id_categoria FROM categorie_menu WHERE nome = '" + categoria + "' AND id_ristorante = " + idRistorante;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next())
				return rs.getInt("id_categoria");
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return 0;
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
	
	public boolean createPlate(Integer idRistorante, String categoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua) {
		String query = null;
		int idCategoria = 0;
		
		idCategoria = getCategoryIdOfResturantFromCategoryName(idRistorante, categoria);
		if(idCategoria == 0){
			createCategoria(idRistorante, categoria);
			idCategoria = getCategoryIdOfResturantFromCategoryName(idRistorante, categoria);
		}
		query = "INSERT INTO elementi_menu (id_elemento, id_ristorante,	id_categoria, nome, prezzo, descrizione, allergeni, nome_seconda_lingua, descrizione_seconda_lingua, posizione_elemento ) VALUES (default, " + idRistorante + ", " + idCategoria + ",'" + nome + "', " + prezzo + ", '" + descrizione + "', '" + allergeni + "', '" + nomeSecondaLingua + "', '" + descrizioneSecondaLingua + "', NULL)";
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

	
	public Menu addSecondLanguage(Integer idRistorante, int idProdotto, String nomeSecondaLingua, String descrizoineSecondaLingua) {
		String query = "UPDATE elementi_menu SET nome_seconda_lingua = '" + nomeSecondaLingua + "', descrizione_seconda_lingua = '" + descrizoineSecondaLingua + "' WHERE id_prodotto = " + idProdotto;
		try {
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}

	@Override
	public boolean deletePlate(Integer idPiatto) {
		String query = "DELETE FROM elementi_menu WHERE id_elemento = " + idPiatto;
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

	@Override
	public boolean updatePosizionePiatto(Integer idPiatto, Integer posizione) {
		String query = "UPDATE elementi_menu SET posizione_elemento = " + posizione + " WHERE id_elemento = " + idPiatto;
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
			return false;
		}
	}

	@Override
	public boolean deleteSortingMenu(Integer idRistorante) {
		String query = "UPDATE elementi_menu SET posizione_elemento = NULL WHERE id_ristorante = " + idRistorante;
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

	@Override
	public Menu getPiattoById(Integer idPiatto) {
		String query = "SELECT * FROM elementi_menu WHERE id_elemento = " + idPiatto;
		ResultSet rs;

		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next())
				return  new Menu(rs.getInt("id_elemento"), rs.getInt("id_ristorante"), rs.getInt("id_categoria"), rs.getString("nome"), rs.getFloat("prezzo"), rs.getString("descrizione"), rs.getString("allergeni"), rs.getString("nome_seconda_lingua"), rs.getString("descrizione_seconda_lingua"), rs.getInt("posizione_elemento"));
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}

		return null;
	}

	@Override
	public boolean updatePlate(Integer idPiatto, Integer idRistorante, String categoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua) {
		Integer idCategoria = getCategoryIdOfResturantFromCategoryName(idRistorante, categoria);
		String query = "UPDATE elementi_menu SET id_categoria = " + idCategoria + ", nome = '" + nome + "', prezzo = " + prezzo + ", descrizione = '" + descrizione + "', allergeni = '" + allergeni + "', nome_seconda_lingua = '" + nomeSecondaLingua + "', descrizione_seconda_lingua = '" + descrizioneSecondaLingua + "'  WHERE id_elemento = " + idPiatto;
		System.out.print(query);
		try {
			db.getStatement().executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return false;
	}

}
