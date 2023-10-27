package com.ingsw.DAOimplements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ingsw.DAOinterface.ProdottoDAOint;
import com.ingsw.ratatouille.DatabaseConnection;
import com.ingsw.ratatouille.Menu;
import com.ingsw.ratatouille.Prodotto;
import com.ingsw.ratatouille.Prodotto.categoriaProdotto;

public class ProdottoDAOimp implements ProdottoDAOint {
	DatabaseConnection db;
	Prodotto prodotto;
	
	public ProdottoDAOimp(DatabaseConnection db){
		this.db = db;
	}

	
	public ArrayList<Prodotto> getDispensa() {
		ArrayList<Prodotto> prodotto = new ArrayList<Prodotto>();
		String query = "SELECT * FROM prodotto";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Prodotto prodottoTmp = new Prodotto(rs.getInt("id_prodotto"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("stato"), rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getDouble("quantita"), rs.getString("unita_misura"), rs.getString("categoria_prodotto"));
				prodotto.add(prodottoTmp);
			}
			return prodotto;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}

	public ArrayList<Prodotto> getDispensaFromRestaurant(Integer idRistorante) {
		ArrayList<Prodotto> prodotto = new ArrayList<Prodotto>();
		String query = "SELECT * FROM prodotto WHERE id_ristorante = " + idRistorante;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Prodotto prodottoTmp = new Prodotto(rs.getInt("id_prodotto"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("stato"), rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getDouble("quantita"), rs.getString("unita_misura"), rs.getString("categoria_prodotto"));
				prodotto.add(prodottoTmp);
			}
			return prodotto;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}

	
	public ArrayList<Prodotto> getDispensaProduct(Integer idRistorante, String categoria) {
		ArrayList<Prodotto> prodotto = new ArrayList<Prodotto>();
		String query = "SELECT * FROM prodotto WHERE id_ristorante = " + idRistorante + " AND categoria_prodotto::text ILIKE '%" + categoria + "%'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Prodotto prodottoTmp = new Prodotto(rs.getInt("id_prodotto"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("stato"), rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getDouble("quantita"), rs.getString("unita_misura"), rs.getString("categoria_prodotto"));
				prodotto.add(prodottoTmp);
			}
			return prodotto;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}


	
	public ArrayList<Prodotto> getDispensaProductByName(Integer id_ristorante, String nomeProdotto) {
		ArrayList<Prodotto> prodotto = new ArrayList<Prodotto>();
		String query = "SELECT * FROM prodotto WHERE id_ristorante = " + id_ristorante + " AND nome LIKE '%" + nomeProdotto + "%'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Prodotto prodottoTmp = new Prodotto(rs.getInt("id_prodotto"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("stato"), rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getDouble("quantita"), rs.getString("unita_misura"), rs.getString("categoria_prodotto"));
				prodotto.add(prodottoTmp);
			}
			return prodotto;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}



	public ArrayList<Prodotto> getAvaiableDispensaProduct(Integer idRistorante) {
		ArrayList<Prodotto> prodotto = new ArrayList<Prodotto>();
		String query = "SELECT * FROM prodotto WHERE stato > 0 AND id_ristorante = " + idRistorante;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				Prodotto prodottoTmp = new Prodotto(rs.getInt("id_prodotto"), rs.getInt("id_ristorante"), rs.getString("nome"), rs.getInt("stato"), rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getDouble("quantita"), rs.getString("unita_misura"), rs.getString("categoria_prodotto"));
				prodotto.add(prodottoTmp);
			}
			return prodotto;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		
		return null;
	}


	public Prodotto createProduct(Integer idRistorante, String nome, Integer stato, String descrizione, Double prezzo, Double quantita, String unitaMisura, String categoria) {
		String query = "INSERT INTO prodotto (id_prodotto, id_ristorante, nome, stato, descrizione, prezzo, quantita, unita_misura, categoria_prodotto) VALUES (default, " + idRistorante + ", '" + nome + "', " + stato + ", '" + descrizione + "', " + prezzo + ", " + quantita + ", '" + unitaMisura + "', '" + categoria + "')";
		try {
			db.getStatement().executeUpdate(query);
			query = "SELECT * FROM prodotto WHERE id_prodotto = (SELECT max(id_prodotto) FROM prodotto)";
			ResultSet rs;
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				return new Prodotto(rs.getInt("id_prodotto"), idRistorante, nome, stato, descrizione, prezzo, quantita, unitaMisura, categoria);
			}
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
				
		return null;
	}


	@Override
	public Prodotto modifyProduct(Integer idProdotto, String nome, Integer stato, String descrizione, Double prezzo, Double quantita, String unitaMisura, String categoria) {
		String query = "UPDATE prodotto SET nome = '" + nome + "', stato = " + stato + ", descrizione = '" + descrizione + "', prezzo = " + prezzo + ", quantita = " + quantita + ", unita_misura = '" + unitaMisura + "', categoria_prodotto = '" + categoria + "' WHERE id_prodotto = "+ idProdotto;
		try {
			db.getStatement().executeUpdate(query);
			query = "SELECT * FROM prodotto WHERE id_prodotto = (SELECT max(id_prodotto) FROM prodotto)";
			ResultSet rs;
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				return new Prodotto(rs.getInt("id_prodotto"), idProdotto, nome, stato, descrizione, prezzo, quantita, unitaMisura, categoria);
			}
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
				
		return null;
	}


	@Override
	public Prodotto deleteProduct(Integer idProdotto) {
		String query = "DELETE FROM prodotto WHERE id_prodotto = " + idProdotto;
		try {
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}

}
