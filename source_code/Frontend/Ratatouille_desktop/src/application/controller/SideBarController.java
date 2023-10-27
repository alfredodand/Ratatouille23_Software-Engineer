package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SideBarController {
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	
	private void changeScene(ActionEvent actionEvent, Parent root) {
		stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		double prevWidth = stage.getWidth();
		double prevHeight = stage.getHeight();
		stage.setScene(scene);
		stage.setWidth(prevWidth);
		stage.setHeight(prevHeight);
		stage.show();
	}
	
	public void goToNotice(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/AvvisiScene.fxml"));
		changeScene(actionEvent, root);
	}
	
	public void goToMenu(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/MenuScene.fxml"));
		changeScene(actionEvent, root);
	}
	
	public void goToGestisciPersonale(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/RisorseUmaneScene.fxml"));
		changeScene(actionEvent, root);
	}
	
	public void goToPersonalizzaAttivita(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/PersonalizzazioneAttivitaScene.fxml"));
		changeScene(actionEvent, root);
	}
	
	public void goToAccount(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/AccountScene.fxml"));
		changeScene(actionEvent, root);
	}
	
	public void goToHome(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/Home.fxml"));
		changeScene(actionEvent, root);
	}

}
