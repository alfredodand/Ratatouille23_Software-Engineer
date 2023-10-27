package application.controller;


import java.io.IOException;
import java.util.regex.Pattern;

import application.driver.UtenteDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignUpController {
	@FXML
	TextField nomeField;
	@FXML
	PasswordField passwordField;
	@FXML
	DatePicker dataNascitaField;
	@FXML
	TextField cognomeField;
	@FXML
	TextField emailField;
	@FXML
	TextField nomeAttivitaField;
	@FXML
	Label login;
	@FXML
	Label errorLabel;
	@FXML
	Button signUpButton;
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private UtenteDriver utenteDriver = new UtenteDriver();
	
    @FXML
    public void initialize() {
    	dataNascitaField.setEditable(false);
    	login.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
        	try {
				goToLoginScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
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
    
    public void signUp(ActionEvent actionEvent) throws IOException{
    	String nome = nomeField.getText().toString();
    	String email = emailField.getText().toString();
    	String password = passwordField.getText().toString();
    	String cognome = cognomeField.getText().toString();
    	String dataNascita = "";
    	if(dataNascitaField.getValue() != null)
    		dataNascita = dataNascitaField.getValue().toString();
    	String nomeAttivita = nomeAttivitaField.getText().toString();
    	
    	if(!(nome.isBlank() || cognome.isBlank() || email.isBlank() || password.isBlank() || dataNascita.isBlank() || nomeAttivita.isBlank())) {
    		if(isValid(email)) {
    			if(!utenteDriver.requestSignUpToServer(nome, email, password, cognome, dataNascita, nomeAttivita)) {
    		    	errorLabel.setText("Errore");
    		    	errorLabel.setTextFill(Color.RED);
    			}else {
    		    	errorLabel.setText("Registrazione avvenuta con successo");
    		    	errorLabel.setTextFill(Color.GREEN);
    				goToLoginScene();
    			}
    		}else {
    	    	errorLabel.setText("L'email deve avere il formato example@dominio.com");
    	    	errorLabel.setTextFill(Color.RED);
			}
		}else {
	    	errorLabel.setText("Compilare tutti i campi");
	    	errorLabel.setTextFill(Color.RED);
		}

    	
    }
    
	private void goToLoginScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/Login.fxml"));
		stage = (Stage) login.getScene().getWindow();
		Scene scene = new Scene(root);
		double prevWidth = stage.getWidth();
		double prevHeight = stage.getHeight();
		stage.setScene(scene);
		stage.setWidth(prevWidth);
		stage.setHeight(prevHeight);
		stage.show();
	}
}
