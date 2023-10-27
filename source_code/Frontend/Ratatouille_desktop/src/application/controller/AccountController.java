package application.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

import application.driver.UtenteDriver;
import application.model.Utente;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AccountController {
	boolean isInvisible = true;
	@FXML
	Button accountBtn, homeBtn, noticeBtn, menuBtn, personaleBtn, personalizzaBtn;
	@FXML
	ImageView sidebarBtn;
	@FXML
	TextField nomeInput, cognomeInput, mailInput;
	@FXML
	Button salvaBtn;
	@FXML
	Label errorLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private Utente loggedUser = LoginController.loggedUser;
	private UtenteDriver utenteDriver = new UtenteDriver();
	private SideBarController sideBar = new SideBarController();
	
	public AccountController() {
		
	}
	
	@FXML
	public void initialize() {
		mostraDatiAccount();
	}
	
	private void mostraDatiAccount() {
		nomeInput.setText(loggedUser.getNome());
		cognomeInput.setText(loggedUser.getCognome());
		mailInput.setText(loggedUser.getEmail());		
	}
	
	public boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";
                              
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
	
	public void modificaProfilo(ActionEvent actionEvent) {
		String nome = nomeInput.getText().toString();
    	String email = mailInput.getText().toString();
    	String cognome = cognomeInput.getText().toString();
    	if(!(nome.isBlank() || cognome.isBlank() || email.isBlank())) {
    		if(isValid(email)) {
				if(!utenteDriver.requestModifyAccountToServer(nome, email, cognome)) {
			    	errorLabel.setText("Errore");
			    	errorLabel.setTextFill(Color.RED);
				}else {
					loggedUser.setNome(nome);
					loggedUser.setCognome(cognome);
					loggedUser.setEmail(email);
			    	errorLabel.setText("Modifiche apportate con successo");
			    	errorLabel.setTextFill(Color.GREEN);
				}
    		} else {
    			errorLabel.setText("Errore nel formato della mail");
    			errorLabel.setTextFill(Color.RED);
    		}
		}else {
	    	errorLabel.setText("Compilare tutti i campi");
	    	errorLabel.setTextFill(Color.RED);
		}
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