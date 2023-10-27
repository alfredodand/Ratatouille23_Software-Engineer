package application.controller;
import java.io.IOException;

import application.driver.BusinessDriver;
import application.driver.UtenteDriver;
import application.model.Business;
import application.model.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginController {
	@FXML
	TextField email;
	@FXML
	PasswordField password;
	@FXML
	Label errorLabel;
	@FXML
	Label signUp;
	@FXML
	VBox layoutFields;
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private UtenteDriver utenteDriver = new UtenteDriver();
	public static Utente loggedUser;
	
	public LoginController() {	}

	public void login(ActionEvent actionEvent) throws IOException {
		loggedUser = utenteDriver.requestLoginToServer(email.getText(), password.getText());
//		loggedUser = utenteDriver.requestLoginToServer("joy.bates@example.com", "password");
		if(loggedUser == null) {
			errorLabel.setText("Email o password errata");
			errorLabel.setTextFill(Color.RED);
		} else {
			errorLabel.setText("Login avvenuto con successo");
			errorLabel.setTextFill(Color.GREEN);
			goToHomeScene(actionEvent);
		}
	}
	
	public void setLoggedUser(String nome, String cognome, String email) {
		loggedUser.setNome(nome);
		loggedUser.setCognome(cognome);
		loggedUser.setEmail(email);
	}
	
	@FXML
	public void initialize() {
		signUp.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			try {
            	goToSignUpScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
	}
	
	private void goToSignUpScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/SignUp.fxml"));
		stage = (Stage) (signUp.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void goToHomeScene(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/Home.fxml"));
		stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		stage.setWidth(954);
		stage.setHeight(680);
		Scene scene = new Scene(root);
		FXMLLoader fxmlLoader = new FXMLLoader();
		stage.setScene(scene);
		stage.show();
	}
	
	
}
