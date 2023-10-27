package application.model;

public class Business {
	
	private int idRistorante, idProprietario;
	private String nome, numeroTelefono, indirizzo, nomeImmagine;
	
	public Business(int idRistorante, String nome, String numeroTelefono, String indirizzo, String nomeImmagine, int idProprietario) {
		this.idRistorante = idRistorante;
		this.nome = nome;
		this.numeroTelefono = numeroTelefono;
		this.indirizzo = indirizzo;
		this.nomeImmagine = nomeImmagine;
		this.idProprietario = idProprietario;
	}

	public Business() {
		
	}

	public Business(int idRistorante, String nome, int idProprietario) {
		this.idRistorante = idRistorante;
		this.nome = nome;
		this.idProprietario = idProprietario;
	}

	public int getIdRistorante() {
		return idRistorante;
	}

	public int getIdProprietario() {
		return idProprietario;
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getNomeImmagine() {
		return nomeImmagine;
	}
	
	

}
