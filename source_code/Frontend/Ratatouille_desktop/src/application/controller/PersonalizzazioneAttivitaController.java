package application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import application.driver.BusinessDriver;
import application.model.Business;
import application.model.Utente;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PersonalizzazioneAttivitaController {
	boolean isInvisible = true;
	@FXML
	Button accountBtn, homeBtn, noticeBtn, menuBtn, personaleBtn, personalizzaBtn;
	@FXML
	ImageView sidebarBtn;
	@FXML
	TextField nomeInput, indirizzoInput, telefonoInput;
	@FXML
	Button salvaBtn;
	@FXML
	ImageView logoInputView; 
	@FXML
	Label errorLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private BusinessDriver businessDriver = new BusinessDriver();
	private SideBarController sideBar = new SideBarController();
	private Utente loggedUser = LoginController.loggedUser;
	private Business business;
	private String fileName = "";
	
	public PersonalizzazioneAttivitaController() {
	
	}
	
	@FXML
	public void initialize() {
		mostraDatiRistorante();
		telefonoInput.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) {
	        		telefonoInput.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        if(newValue.length() > 12) {
		        	telefonoInput.setText(newValue.substring(0, 12));
		        }
		    }
		});
		
		try {
			Image image = businessDriver.requestGetLogoToServer();
			if (image != null) {
                logoInputView.setImage(image);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mostraDatiRistorante() {
		try {
			business = businessDriver.requestBusinessToServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		nomeInput.setText(business.getNome());
		indirizzoInput.setText(business.getIndirizzo());
		telefonoInput.setText(business.getNumeroTelefono());
	}
	
	public void caricaLogo() {
		FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Immagini PNG", "*.png")
		    );
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		if (selectedFile != null) {
			errorLabel.setText("");
			fileName = selectedFile.getName();
			System.out.println("cosedavedere" + selectedFile.toURI().toString());
	        File file = new File(selectedFile.toURI().toString().replace("file:/", ""));	        
			try {
				if(file.length() < 1048576 ) {
					businessDriver.setLogoInDatabase(file, fileName);
					Image image = new Image(selectedFile.toURI().toString());
			        logoInputView.setImage(image);
				} else {
					errorLabel.setText("File di dimensioni troppo grandi, impossibile salvarlo\nScegliere un altro file");
					errorLabel.setTextFill(Color.RED);
				}
			} catch (FileNotFoundException e) {
				errorLabel.setText("Impossibile accedere al percorso specificato");
				errorLabel.setTextFill(Color.RED);
			} catch (Exception e) {
				errorLabel.setText("Errore nel caricamento dell'immagine");
				errorLabel.setTextFill(Color.RED);
			}			
	    }
	}
	
	public void modificaDati() {
		business = businessDriver.requestModifyBusinessToServer(nomeInput.getText().toString(), indirizzoInput.getText().toString(), telefonoInput.getText().toString());
		errorLabel.setText("Modifiche apportate con successo");
    	errorLabel.setTextFill(Color.GREEN);
	}
	
	
	public void goToHome(ActionEvent actionEvent) throws IOException {
		sideBar.goToHome(actionEvent);
	}
	
	public void goToNotice(ActionEvent actionEvent) throws IOException {
		sideBar.goToNotice(actionEvent);
	}
	
	public void goToMenu(ActionEvent actionEvent) throws IOException {
		sideBar.goToMenu(actionEvent);
	}
	
	public void goToGestisciPersonale(ActionEvent actionEvent) throws IOException {
		sideBar.goToGestisciPersonale(actionEvent);
	}
	
	public void goToPersonalizzaAttivita(ActionEvent actionEvent) throws IOException {
		sideBar.goToPersonalizzaAttivita(actionEvent);
	}
	
	public void goToAccount(ActionEvent actionEvent) throws IOException {
		sideBar.goToAccount(actionEvent);
	}
	
	
	public void showSideBar() {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), e -> {
			homeBtn.setVisible(!isInvisible);
		}));

		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), e -> {
			noticeBtn.setVisible(!isInvisible);
		}));

		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(150), e -> {
			menuBtn.setVisible(!isInvisible);
		}));
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), e -> {
			personaleBtn.setVisible(!isInvisible);
		}));
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(250), e -> {
			personalizzaBtn.setVisible(!isInvisible);
		}));
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {
			personalizzaBtn.setVisible(!isInvisible);
		}));
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(350), e -> {
			accountBtn.setVisible(!isInvisible);
		}));
		timeline.play();
		isInvisible = !isInvisible;
	}
	
}