package com.ingsw.DAOimplements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.ingsw.DAOinterface.BusinessDAOint;
import com.ingsw.ratatouille.Business;
import com.ingsw.ratatouille.DatabaseConnection;
import com.ingsw.ratatouille.User;

import javax.imageio.ImageIO;


public class BusinessDAOimp implements BusinessDAOint {
	DatabaseConnection db;

	public BusinessDAOimp(DatabaseConnection db) {
		this.db = db;
	}

	public Business getBusinessFromBusinessId(Integer idRistorante) {
		String query = "SELECT * FROM ristorante WHERE id_ristorante = " + idRistorante;
		ResultSet rs = null;
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				return new Business(rs.getInt("id_ristorante"), rs.getString("nome"), rs.getString("telefono"), rs.getString("indirizzo"), rs.getString("nome_immagine"), rs.getInt("id_proprietario"));
			}
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}

		return null;
	}

	@Override
	public Business modifyBusinessInfo(Integer idRistorante, String nome, String indirizzo, String telefono) {
		String query = "UPDATE ristorante SET nome = '" + nome + "', telefono = '" + telefono + "', indirizzo = '" + indirizzo + "' WHERE id_ristorante = " + idRistorante;
		try {
			db.getStatement().executeUpdate(query);
			return getBusinessFromBusinessId(idRistorante);
		} catch (SQLException e) {
			System.out.println("Query " + query + " fallita \n");
		}

		return null;
	}

	public void saveBusinessImage(Integer idUtente, Integer idRistorante, MultipartFile image, String fileName) throws IOException {
		String repository = "C:\\logos\\" + idUtente;
		Path uploadPath = Paths.get(repository);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try (InputStream inputStream = image.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}

		File file = new File("C:\\logos\\" + idUtente + "\\" + fileName);
		FileInputStream fis = new FileInputStream(file);
		try {
			PreparedStatement ps = db.getConnection().prepareStatement("UPDATE ristorante SET logo = ?, nome_immagine = ? WHERE id_ristorante = ?");
			ps.setBinaryStream(1, fis, (int)file.length());
			ps.setString(2, fileName);
			ps.setInt(3, idRistorante);
			ps.executeUpdate();
			ps.close();
			fis.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		FileUtils.deleteDirectory(new File("C:\\logos\\" + idUtente));
	}

	public String getBusinessImage(Integer idUtente, Integer idRistorante) {
		String query = "SELECT nome_immagine FROM ristorante WHERE id_ristorante = " + idRistorante;
		ResultSet rs = null;
		String fileName = "";
		try {
			rs = db.getStatement().executeQuery(query);
			while(rs.next()) {
				fileName = rs.getString("nome_immagine");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!fileName.equals("")){
			try {
				PreparedStatement ps = db.getConnection().prepareStatement("SELECT logo FROM ristorante WHERE id_ristorante = " + idRistorante);
				ResultSet resultSet = ps.executeQuery();
				ByteArrayOutputStream output = new ByteArrayOutputStream();

				String repository = "C:\\logos\\" + idUtente;
				Path uploadPath = Paths.get(repository);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				FileOutputStream fos = new FileOutputStream("C:\\logos\\" + idUtente + "\\" + fileName);

				while (resultSet.next()) {
					byte[] buffer = new byte[1];
					InputStream is = resultSet.getBinaryStream(1);
					while (is.read(buffer) > 0) {
						fos.write(buffer);
					}
					fos.close();
				}
				ps.close();



				File file = new File("C:\\logos\\" + idUtente + "\\" + fileName);
				byte[] bytes = Files.readAllBytes(file.toPath());

				String encodedImage = Base64.getEncoder().encodeToString(bytes);
				JSONObject json = new JSONObject();
				json.put("image", encodedImage);
				return json.toString();

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("dsad");
		return null;
	}

}
