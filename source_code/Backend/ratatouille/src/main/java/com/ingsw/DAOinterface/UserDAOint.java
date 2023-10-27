package com.ingsw.DAOinterface;

import java.util.ArrayList;

import com.ingsw.ratatouille.LoggedUser;
import com.ingsw.ratatouille.User;

public interface UserDAOint {
	public ArrayList<User> getUser();
	public User createAdmin(String nome, String cognome, String email, String password, String dataNascita, String nomeAttivita);
	public User createEmployee(String nome, String cognome, String passwordTemporanea, String email, String dataNascita, String ruolo, Integer idUtente, Integer idRistorante);
	public ArrayList<User> getUserOfResturant(int id_ristorante);
	public User getUserById(Integer id);
	public LoggedUser login(String email, String password, String token, String time);
	public User verifyEmployee(String email, String nomeRistorante);
	public User modifyUserNameSurnameDate(Integer idUtente, String nome, String cognome, String dataNascita);
	public User firstAccess(Integer id_utente, String newPassword);
	public User modifyUserNameSurnameEmail(Integer idUtente, String nome, String cognome, String email);
	public User upgradeUserRole(int idUtente, String ruolo);
	public User downgradeUserRole(String ruoloLogged, int idUtente, String ruoloInput);
	public void fireUser(String ruoloLogged, int idUtente, String ruolo);
}
