package com.ingsw.DAOinterface;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ingsw.ratatouille.Avviso;
import com.ingsw.ratatouille.AvvisoNascostoVisto;
import com.ingsw.ratatouille.LoggedUser;
import com.ingsw.ratatouille.User;

public interface AvvisoDAOint {

	public ArrayList<Avviso> getAvvisi();
	public ArrayList<Avviso> getAvvisiOfResturant(Integer id_ristorante);
	public ArrayList<AvvisoNascostoVisto> getAvvisiHiddenOf(Integer id_user);
	public ArrayList<AvvisoNascostoVisto> getAvvisiViewedOf(Integer id_user);
	public AvvisoNascostoVisto setAvvisoViewed(Integer id_avviso, Integer idUtente);
	public AvvisoNascostoVisto setAvvisoNotViewed(Integer id_avviso, Integer idUtente);
	public AvvisoNascostoVisto setAvvisoHidden(Integer id_avviso, Integer idUtente);
	public Avviso createNewAvviso(Integer id_ristorante, String testo, Integer idUtente);
	public AvvisoNascostoVisto setAvvisoNotHidden(Integer id_avviso, Integer idUtente);
	public boolean deleteAvviso(Integer idAvviso);
	public Integer getNumberOfAvvisiToRead(Integer idUtente, Integer idRistorante);
}
