package com.ingsw.DAOinterface;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ingsw.ratatouille.Business;
import com.ingsw.ratatouille.User;

public interface BusinessDAOint {
	
	public  Business getBusinessFromBusinessId(Integer idRistorante);
	public Business modifyBusinessInfo(Integer id_ristorante, String nome, String indirizzo, String telefono);
	public void saveBusinessImage(Integer idUtente, Integer idRistorante, MultipartFile image, String fileName) throws IOException;
	public String getBusinessImage(Integer idUtente, Integer idRistorante);
}
