package com.ingsw.ratatouille;

public class AvvisoNascostoVisto extends Avviso {
	String data_nascosto_visto;

	public AvvisoNascostoVisto(int id_avviso, int id_utente, int id_ristorante, String testo, String data_ora, String data_nascosto_visto) {
		super(id_avviso, id_utente, id_ristorante, testo, data_ora);
		this.data_nascosto_visto = data_nascosto_visto;
	}

	
	public String getDataNascosto() {
		return data_nascosto_visto;
	}
	
}
