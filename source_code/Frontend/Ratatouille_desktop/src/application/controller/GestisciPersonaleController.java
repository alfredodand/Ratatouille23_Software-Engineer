package application.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.driver.UtenteDriver;
import application.model.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GestisciPersonaleController {
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private Utente loggedUser = LoginController.loggedUser;
	private UtenteDriver utenteDriver = new UtenteDriver();
	private ArrayList<Utente> utenti = utenteDriver.showEmplyees();
	
	@FXML
	Label nomeCognomeLabel, emailLabel, ruoloLabel, errorLabel;
	@FXML
	ComboBox<String> utentiComboBox;

	private String ruoloUI = "";
	public GestisciPersonaleController() {}
	
	@FXML
	public void initialize() {
		fillComboBoxOfUsers(utenti);
		utentiComboBox.getSelectionModel().select(0);
		mostraDatiUtente();
	}

	private void fillComboBoxOfUsers(List<Utente> utenti) {
		for (Utente utente : utenti) {
			utentiComboBox.getItems().add("ID: " + utente.getIdUtente() + " ~ " + utente.getNome() + " " + utente.getCognome() + " - " + utente.getRuolo());
		}
	}
	
	public void mostraDatiUtente(){
		String datiUtente = utentiComboBox.getSelectionModel().getSelectedItem().toString();
		String id = datiUtente.substring(4, 6);
		if(id.contains(" ")) {
			id = datiUtente.substring(4, 5);
		}
		if(datiUtente.contains("addetto_sala")) {
			ruoloUI = "Addetto alla Sala";
		}
		if(datiUtente.contains("addetto_cucina")) {
			ruoloUI = "Addetto alla Cucina";
		}
		if(datiUtente.contains("supervisore")) {
			ruoloUI = "Supervisore";
		}
		if(datiUtente.contains("admin")) {
			ruoloUI = "Amministratore";
		}
		System.out.println(id);
		
		for(Utente u : utenti) {
			if(id.equals(String.valueOf(u.getIdUtente()))) {
				String nomeCognome = u.getNome() + " " + u.getCognome();
				String email = u.getEmail();
				String ruolo = ruoloUI;
				mostraInformazioniUtente(nomeCognome, email, ruolo);
			}
		}
	}	
	
	public void goToRisorseUmaneScene(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/RisorseUmaneScene.fxml"));
		stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void mostraInformazioniUtente(String nomeCognome, String email, String ruolo) {
		nomeCognomeLabel.setText(nomeCognome);
		emailLabel.setText(email);
		ruoloLabel.setText(ruolo);
	}
	
	public void declassa(ActionEvent actionEvent) {
		if((loggedUser.getRuolo().equals("supervisore")) && ruoloLabel.getText().equals("Supervisore")) {
			errorLabel.setText("Non hai i permessi per declassare un supervisore");
	    	errorLabel.setTextFill(Color.RED);
		} else {
			if(ruoloLabel.getText().equals("Addetto alla Sala")) {
				errorLabel.setText("Non puoi declassare un addetto alla sala, forse volevi sollevarlo dal suo incarico");
		    	errorLabel.setTextFill(Color.RED);
			} else {
				if(ruoloLabel.getText().equals("Amministratore")) {
					errorLabel.setText("Non puoi declassare un amministratore");
			    	errorLabel.setTextFill(Color.RED);
				} else {
					String datiUtente = utentiComboBox.getSelectionModel().getSelectedItem().toString();
					String id = datiUtente.substring(4, 6);
					String ruolo = "";
					if(id.contains(" ")) {
						id = datiUtente.substring(4, 5);
					}
					if(ruoloLabel.getText().toString().contains("Cucina")) {
						ruolo = "addetto_cucina";
					}
					if(ruoloLabel.getText().toString().contains("Sala")) {
						ruolo = "addetto_sala";
					}
					if(ruoloLabel.getText().toString().contains("visore")) {
						ruolo = "supervisore";
					}
					utentiComboBox.getItems().remove(utentiComboBox.getSelectionModel().getSelectedIndex());
					Utente updatedUser = utenteDriver.requestDowngradeRoleToServer(id, ruolo);
					utentiComboBox.getItems().add("ID: " + updatedUser.getIdUtente() + " ~ " + updatedUser.getNome() + " " + updatedUser.getCognome() + " - " + updatedUser.getRuolo());
					errorLabel.setText("Modifiche apportate con successo");
			    	errorLabel.setTextFill(Color.GREEN);
				}
			}
		}
	}
	
	public void promuovi(ActionEvent actionEvent) {
		if(ruoloLabel.getText().equals("Amministratore")) {
			errorLabel.setText("Non puoi promuovere un amministratore, è già al vertice");
	    	errorLabel.setTextFill(Color.RED);
		} else {
			if(ruoloLabel.getText().equals("Supervisore")) {
				errorLabel.setText("Non puoi promuovere un supervisore, ci può essere un solo amministratore per attività");
		    	errorLabel.setTextFill(Color.RED);
			} else {
				String datiUtente = utentiComboBox.getSelectionModel().getSelectedItem().toString();
				String id = datiUtente.substring(4, 6);
				String ruolo = "";
				if(id.contains(" ")) {
					id = datiUtente.substring(4, 5);
				}
				if(ruoloLabel.getText().toString().contains("Cucina")) {
					ruolo = "addetto_cucina";
				}
				if(ruoloLabel.getText().toString().contains("Sala")) {
					ruolo = "addetto_sala";
				}
				if(ruoloLabel.getText().toString().contains("visore")) {
					ruolo = "supervisore";
				}
				utentiComboBox.getItems().remove(utentiComboBox.getSelectionModel().getSelectedIndex());
				Utente updatedUser = utenteDriver.requestUpgradeRoleToServer(id, ruolo);
				utentiComboBox.getItems().add("ID: " + updatedUser.getIdUtente() + " ~ " + updatedUser.getNome() + " " + updatedUser.getCognome() + " - " + updatedUser.getRuolo());
				errorLabel.setText("Modifiche apportate con successo");
		    	errorLabel.setTextFill(Color.GREEN);
			}
		}
	}
	
	public void licenzia(ActionEvent actionEvent) {
		if(ruoloLabel.getText().equals("Amministratore")) {
			errorLabel.setText("Non puoi licenziare un amministratore");
	    	errorLabel.setTextFill(Color.RED);
		} else {
			if(ruoloLabel.getText().equals("Supervisore") && loggedUser.getRuolo().equals("supervisore")) {
				errorLabel.setText("Non puoi licenziare un supervisore perché ha il tuo stesso grado");
		    	errorLabel.setTextFill(Color.RED);
			} else {
				String datiUtente = utentiComboBox.getSelectionModel().getSelectedItem().toString();
				String id = datiUtente.substring(4, 6);
				String ruolo = "";
				if(id.contains(" ")) {
					id = datiUtente.substring(4, 5);
				}
				if(ruoloLabel.getText().toString().contains("Cucina")) {
					ruolo = "addetto_cucina";
				}
				if(ruoloLabel.getText().toString().contains("Sala")) {
					ruolo = "addetto_sala";
				}
				if(ruoloLabel.getText().toString().contains("visore")) {
					ruolo = "supervisore";
				}
				utenteDriver.requestFireUserToServer(id, ruolo);
				errorLabel.setText("Modifiche apportate con successo");
		    	errorLabel.setTextFill(Color.GREEN);
			}
		}
	}
	
}
