package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSceneController {
	private Stage stage;
	private Scene scene;
	private Parent parent;
	
	public MainSceneController() {}
	
	public void entra(ActionEvent actionEvent) throws IOException {
		goToLoginScene(actionEvent);
	}

	private void goToLoginScene(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/Login.fxml"));
		stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
