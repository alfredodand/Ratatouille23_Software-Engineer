package application.controller;

import java.io.IOException;

import application.driver.BusinessDriver;
import application.model.Business;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController {
	boolean isInvisible = true;
	@FXML
	Button accountBtn, homeBtn, noticeBtn, menuBtn, personaleBtn, personalizzaBtn;
	@FXML
	ImageView sidebarBtn;
	@FXML
	Text nomeAttivitaLabel;
	
	private Utente LoggedUser = LoginController.loggedUser;
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private BusinessDriver businessDriver = new BusinessDriver();
	private SideBarController sideBar = new SideBarController();
	
	public HomeController() {
		
	}
	
	@FXML
	public void initialize() {
		Business business = businessDriver.requestBusinessToServer();
		nomeAttivitaLabel.setText(business.getNome().toUpperCase());
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