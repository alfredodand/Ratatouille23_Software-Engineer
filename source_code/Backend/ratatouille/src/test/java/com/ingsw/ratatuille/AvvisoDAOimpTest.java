package com.ingsw.ratatuille;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import com.ingsw.DAOimplements.AvvisoDAOimp;
import com.ingsw.ratatouille.Avviso;
import com.ingsw.ratatouille.DatabaseConnection;

public class AvvisoDAOimpTest {
	private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	private AvvisoDAOimp avvisoDAOimp;
	private final int ID_UTENTE_NON_PRESENTE_NEL_DB = 1500;
	
	@Before
	public void init(){
		avvisoDAOimp = new AvvisoDAOimp(databaseConnection);
	}
	
	private Integer getMaxIdAvvisoInDb() {
		ResultSet rs;
		String query = "SELECT last_value as id FROM avviso_id_avviso_seq;";
		try {
		rs = databaseConnection.getStatement().executeQuery(query);
			while(rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Avviso checkAddedToDb() {
		ResultSet rs;
		Integer idAvviso = getMaxIdAvvisoInDb();
		String query = "SELECT MAX(id_avviso) as id , avviso.id_utente, avviso.id_ristorante, testo, data_ora, nome as autore FROM avviso JOIN utente ON avviso.id_utente = utente.id_utente WHERE id_avviso = " + idAvviso + " GROUP BY avviso.id_utente, avviso.id_ristorante, testo, data_ora, nome";
		try {
		rs = databaseConnection.getStatement().executeQuery(query);
			while(rs.next()) {
				return new Avviso(rs.getInt("id"), rs.getInt("id_utente"), rs.getInt("id_ristorante"), rs.getString("testo"), rs.getString("data_ora"), rs.getString("autore"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Black box
	
	@Test
	public void idUtenteEidRistoranteValidi() {
		/* SOLUZIONE 1 */
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		String now = LocalDateTime.now().format(dtf);
		Avviso oracolo = new Avviso(getMaxIdAvvisoInDb() + 1, 2, 9, "testo", now, "Teka");		
		Avviso avviso = avvisoDAOimp.createNewAvviso(9, "testo", 2);
		/* SOLUZIONE II */
//		Avviso oracolo = checkAddedToDb();
		assertEquals(avviso, oracolo);
	}//
		
	@Test
	public void idRistoranteUtente2EidUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(9, "testo", ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteUtente2EidUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(9, "testo", -10));
	}
	
	@Test
	public void idRistoranteUtente2ETestoNullEidUtenteValido() {
		assertNull(avvisoDAOimp.createNewAvviso(9, null, 2));
	}
	
	@Test
	public void idRistoranteUtente2ETestoNullEidUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(9, null, ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteUtente2ETestoNullEIdUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(9, null, -10));
	}
	
	@Test
	public void idRistoranteValidoEIdUtenteNonAppartententeAlRistorante() {
		assertNull(avvisoDAOimp.createNewAvviso(10, "Testo", 2));
	}
	
	@Test
	public void idRistoranteValidoEIdUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(10, "Testo", ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteValidoEIdUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(10, "Testo", -10));
	}
	
	@Test
	public void idRistoranteValidoETestoNullEIdUtenteNonAppartenenteAlRistorante() {
		assertNull(avvisoDAOimp.createNewAvviso(10, null, 2));
	}
	
	@Test
	public void idRistoranteValidoETestoNullEIdUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(10, null, ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteValidoETestoNullEIdUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(10, null, -10));
	}
	
	@Test
	public void idRistoranteNonValidoEIdUtenteValido() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, "Testo", 2));
	}
	
	@Test
	public void idRistoranteNonValidoEIdUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, "Testo", ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteNonValidoEIdUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, "Testo", -10));
	}

	@Test
	public void idRistoranteNonValidoETestoNullEIdUtenteValido() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, null, 2));
	}
	@Test
	public void idRistoranteNonValidoETestoNullEIdUtenteNonPresenteNelDb() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, null, ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void idRistoranteNonValidoETestoNullEIdUtenteNonValido() {
		assertNull(avvisoDAOimp.createNewAvviso(-10, null, -10));
	}
	
	//WHITE BOX
	@Test
	public void testNuovoAvvisoPath_2to13_9_18_19_30() {
		assertNull(avvisoDAOimp.createNewAvviso(9, null, 2));
	}
	
	@Test
	public void testNuovoAvvisoPath_2to13_9_18to26() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		String now = LocalDateTime.now().format(dtf);
		Avviso oracolo = new Avviso(getMaxIdAvvisoInDb() + 1, 2, 9, "testo", now, "Teka");		
		Avviso avviso = avvisoDAOimp.createNewAvviso(9, "testo", 2);
		assertEquals(avviso, oracolo);
	}
	
	@Test
	public void testNuovoAvvisoPath_2to9_18to30() {
		assertNull(avvisoDAOimp.createNewAvviso(2, null, ID_UTENTE_NON_PRESENTE_NEL_DB));
	}
	
	@Test
	public void testNuovoAvvisoPath_2to11_9_18_30() {
		assertNull(avvisoDAOimp.createNewAvviso(7, "testo", 10));
	}
	
	//MODIFIED CONDITION
	@Test
	public void testCondizioneSupervisorePath_2to13_9_18_19_30() {
		assertNull(avvisoDAOimp.createNewAvviso(2, null, 7));
	}
	
	@Test
	public void testCondizioneAdminPath_2to13_9_18_19_30() {
		assertNull(avvisoDAOimp.createNewAvviso(9, null, 2));
	}
	
	@Test
	public void testCondizioneSupervisorePath_2to13_9_18to26() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		String now = LocalDateTime.now().format(dtf);
		Avviso oracolo = new Avviso(getMaxIdAvvisoInDb() + 1, 7, 2, "testo", now, "Ece");		
		Avviso avviso = avvisoDAOimp.createNewAvviso(2, "testo", 7);
		assertEquals(avviso, oracolo);
	}//
	
	@Test
	public void testCondizioneAdminPath_2to13_9_18to26() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		String now = LocalDateTime.now().format(dtf);
		Avviso oracolo = new Avviso(getMaxIdAvvisoInDb() + 1, 2, 9, "testo", now, "Teka");		
		Avviso avviso = avvisoDAOimp.createNewAvviso(9, "testo", 2);
		assertEquals(avviso, oracolo);
	}
	
	
}
