package com.ingsw.DAOimplements;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import com.ingsw.DAOinterface.UserDAOint;
import com.ingsw.ratatouille.DatabaseConnection;
import com.ingsw.ratatouille.LoggedUser;
import com.ingsw.ratatouille.User;
@Component
public class UserDAOimp implements UserDAOint {
	DatabaseConnection db;
	
	public UserDAOimp(DatabaseConnection db){
		this.db = db;
	}
	
 
	public ArrayList<User> getUser() {
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM utente";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				User u = new User(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"), rs.getString("data_nascita"), rs.getString("email"), rs.getString("password"), rs.getString("ruolo"), rs.getBoolean("isFirstAccess"), rs.getInt("aggiunto_da"), rs.getString("data_aggiunta"), rs.getInt("id_ristorante"));
				users.add(u);
			}
			return users;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	
	public User createAdmin(String nome, String cognome, String email, String password, String dataNascita, String nomeRistorante) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		password = convertInSha256(password);
		System.out.println(password);
		String query = "INSERT INTO ristorante(id_ristorante, nome, telefono, indirizzo, logo, nome_immagine, id_proprietario) VALUES (default, '" + nomeRistorante + "', NULL, NULL, NULL, '', NULL)";
		Integer idUtente = 0, idRistorante = 0;
		try {
			
			db.getStatement().executeUpdate(query);
			Statement stmt = db.getConnection().createStatement(
				    ResultSet.TYPE_SCROLL_INSENSITIVE,
				    ResultSet.CONCUR_READ_ONLY
				);
			query = "SELECT max(id_ristorante) AS last_value FROM ristorante";
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					idRistorante = rs.getInt("last_value");
				}
			}
			
			query = "INSERT INTO utente(id_utente, nome, cognome, data_nascita, email, password, ruolo, isFirstAccess, aggiunto_da, data_aggiunta, id_ristorante) VALUES (default, '" + nome + "', '" + cognome.replace("'", "''") + "', '" + dataNascita + "', '" + email + "', '" + password + "' ,'admin' ," + "false, -1, '" + now + "', " + idRistorante  + ");";
			db.getStatement().executeUpdate(query);
			query = "SELECT id_utente FROM utente WHERE email = '" + email + "'";

			ResultSet rs2 = db.getStatement().executeQuery(query);
			if(rs2.isBeforeFirst()) {
				while(rs2.next())  {
					idUtente = rs2.getInt("id_utente");
				}
			}
			return getUserById(idUtente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
			return null;
		}
	}
	
	public User createEmployee(String nome, String cognome, String passwordTemporanea, String email, String dataNascita, String ruolo, Integer idUtente, Integer idRistorante) {
				
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		int idUtenteCreato = 0;
		String query = "";
		passwordTemporanea = convertInSha256(passwordTemporanea);
		try {
			query = "INSERT INTO utente(id_utente, nome, cognome, password, data_nascita, email, ruolo, isFirstAccess, aggiunto_da, data_aggiunta, id_ristorante) VALUES (default, '" + nome + "', '" + cognome.replace("'", "''") + "', '" + passwordTemporanea + "', '" + dataNascita + "', '" + email.toLowerCase() + "' ,'" + ruolo + "' ," + "true, " + idUtente + ", '" + now + "', " + idRistorante + ");";
			db.getStatement().executeUpdate(query);
			
			query = "SELECT id_utente FROM utente WHERE email = '" + email + "';";
			db.getStatement().executeQuery(query);
			ResultSet rs = db.getStatement().executeQuery(query);
			
			while(rs.next()) {
				idUtenteCreato = rs.getInt("id_utente");
			}
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
			return null;
		}		
		
		User newUser = new User(idUtenteCreato, nome, cognome, dataNascita, email, passwordTemporanea, ruolo, false, idUtente, now.toString(), idRistorante);
		return newUser;
	}

	
	public ArrayList<User> getUserOfResturant(int id_ristorante) {
		ArrayList<User> users = new ArrayList<User>();
		String query = "SELECT * FROM utente WHERE id_ristorante = " + id_ristorante + " ORDER BY ruolo";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				User u = new User(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"), rs.getString("data_nascita"), rs.getString("email"), rs.getString("password"), rs.getString("ruolo"), rs.getBoolean("isFirstAccess"), rs.getInt("aggiunto_da"), rs.getString("data_aggiunta"), rs.getInt("id_ristorante"));
				users.add(u);
			}
			return users;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}

	public User getUserById(Integer id_utente) {
		User u = null;
		String query = "SELECT * FROM utente WHERE id_utente = " + id_utente;
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				u = new User(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"), rs.getString("data_nascita"), rs.getString("email"), rs.getString("password"), rs.getString("ruolo"), rs.getBoolean("isFirstAccess"), rs.getInt("aggiunto_da"), rs.getString("data_aggiunta"), rs.getInt("id_ristorante"));
			}
			return u;
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	
	public LoggedUser login(String email, String password, String token, String expirationTime) {
		LoggedUser u = null;
		password = convertInSha256(password);
		System.out.println(password);
		String query = "SELECT * FROM utente WHERE email = '" + email + "' AND password = '" + password + "'";
		ResultSet rs;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				u = new LoggedUser(rs.getInt("id_utente"), rs.getString("nome"), rs.getString("cognome"), rs.getString("data_nascita"), rs.getString("email"), rs.getString("password"), rs.getString("ruolo"), rs.getBoolean("isFirstAccess"), rs.getInt("aggiunto_da"), rs.getString("data_aggiunta"), rs.getInt("id_ristorante"), token, expirationTime);
			}
			return u;
		} catch (SQLException e) {
			System.out.println("Login fallito possibi motivi : [email o password errate / token scaduto \n");
		}
		return null;
	}


	private String convertInSha256(String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		password = Base64.getEncoder().encodeToString(hash);
		return password;
	}


	public User verifyEmployee(String email, String nomeRistorante) {
		String query = null;
		ResultSet rs;
		Integer idUser = null;
		try {
			query = "SELECT id_utente FROM utente JOIN ristorante ON utente.id_ristorante = ristorante.id_ristorante WHERE email = '" + email + "' AND ristorante.nome = '" + nomeRistorante + "';";
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				idUser = rs.getInt("id_utente");
			}
			return getUserById(idUser);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}


	@Override
	public User modifyUserNameSurnameDate(Integer idUtente, String nome, String cognome, String dataNascita) {
		String query = "UPDATE utente SET nome = '" + nome + "', cognome = '" + cognome.replace("'", "''") + "', data_nascita = '" + dataNascita + "' WHERE id_utente = " + idUtente;
		try {
			db.getStatement().executeUpdate(query);
			return getUserById(idUtente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	public User modifyUserNameSurnameEmail(Integer idUtente, String nome, String cognome, String email) {
		String query = "UPDATE utente SET nome = '" + nome + "', cognome = '" + cognome.replace("'", "''") + "', email = '" + email + "' WHERE id_utente = " + idUtente;
		try {
			db.getStatement().executeUpdate(query);
			return getUserById(idUtente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}


	@Override
	public User firstAccess(Integer id_utente, String newPassword) {
		newPassword = convertInSha256(newPassword);
		String query = "UPDATE utente SET password = '" + newPassword + "', isfirstaccess = false WHERE id_utente = " + id_utente;
		try {
			db.getStatement().executeUpdate(query);
			return getUserById(id_utente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	public User downgradeUserRole(String ruoloLogged, int idUtente, String ruoloInput) {
		String query = "";
		if(ruoloLogged.equals("admin") && ruoloInput.equals("supervisore")) {
			query = "UPDATE utente SET ruolo = 'addetto_cucina' WHERE id_utente = " + idUtente;
		}
		if(ruoloInput.contains("cucina")) {
			query = "UPDATE utente SET ruolo = 'addetto_sala' WHERE id_utente = " + idUtente;
		}
		
		try {
			db.getStatement().executeUpdate(query);
			return getUserById(idUtente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	public User upgradeUserRole(int idUtente, String ruoloInput) {
		String query = "";
		if(ruoloInput.contains("cucina")) {
			query = "UPDATE utente SET ruolo = 'supervisore' WHERE id_utente = " + idUtente;
		}
		if(ruoloInput.contains("sala")) {
			query = "UPDATE utente SET ruolo = 'addetto_cucina' WHERE id_utente = " + idUtente;
		}
		
		try {
			db.getStatement().executeUpdate(query);
			return getUserById(idUtente);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}
		return null;
	}
	
	public void fireUser(String ruoloLogged, int idUtente, String ruoloInput) {
		String query = "";
		String query2 = "";
		String query3 = "";
		
		if(ruoloLogged.equals("admin") && ruoloInput.equals("supervisore")) {
			query = "DELETE FROM cronologia_nascosti_avviso WHERE id_utente = " + idUtente;
			query2 = "DELETE FROM cronologia_lettura_avviso WHERE id_utente = " + idUtente;
			query3 = "DELETE FROM utente WHERE id_utente = " + idUtente;
		}

		if(ruoloInput.equals("addetto_cucina") || ruoloInput.equals("addetto_sala")) {
			query = "DELETE FROM cronologia_nascosti_avviso WHERE id_utente = " + idUtente;
			query2 = "DELETE FROM cronologia_lettura_avviso WHERE id_utente = " + idUtente;
			query3 = "DELETE FROM utente WHERE id_utente = " + idUtente;
		}

		try {
			db.getStatement().executeUpdate(query);
			db.getStatement().executeUpdate(query2);
			db.getStatement().executeUpdate(query3);
		} catch (SQLException e) {
			System.out.println("Query " + query + " o " + query2 + " o " + query3 + " fallita \n");
		}
	}
	
}

