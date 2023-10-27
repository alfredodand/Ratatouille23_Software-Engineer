package application.controller;

import java.awt.ScrollPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout.Alignment;

import application.driver.AvvisiDriver;
import application.model.Avviso;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AvvisiController implements Observer {
	
	boolean isInvisible = true;
	@FXML
	Button accountBtn, homeBtn, noticeBtn, menuBtn, personaleBtn, personalizzaBtn;
	@FXML
	ImageView sidebarBtn;
	@FXML
	TextArea testoAvviso;
	@FXML
	Button inviaBtn;
	@FXML
	VBox vBoxLayout;
	@FXML
	Label errorLabel;

	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private AvvisiDriver avvisiDriver = new AvvisiDriver();
	private SideBarController sideBar = new SideBarController();
	
	public AvvisiController() {	}
	
	@FXML
	public void initialize() {
		avvisiDriver.requestAvvisiFromServer(this);
	}
	
	public void scriviAvviso() {
		if(!testoAvviso.getText().isBlank()) {
			String text = testoAvviso.getText().toString();
			avvisiDriver.requestScriviAvvisiFromServer(this, text);
			errorLabel.setText("Messaggio inviato con successo");
			PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));
			pauseTransition.setOnFinished(e -> errorLabel.setText(""));
			pauseTransition.play();
			errorLabel.setTextFill(Color.GREEN);
			testoAvviso.setText("");
		}else {
			errorLabel.setText("Il testo non può essere vuoto");
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

	@Override
	public void update(Observable o, Object arg) {
		Avviso avviso = (Avviso) o;
		BorderPane containerNotice = new BorderPane();
		containerNotice.setStyle("-fx-border-color: #003F91; -fx-border-radius: 20; -fx-border-width: 2;");
		generateHeaderNotice(avviso, containerNotice);
		generateBodyNotice(avviso, containerNotice);
		ImageView imageView = generateLeftGarbageButtonNotice(containerNotice, avviso.getIdAvviso());
		vBoxLayout.getChildren().add(0, containerNotice);
	}

	private ImageView generateLeftGarbageButtonNotice(BorderPane containerNotice, Integer idAvviso) {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none;");
		String path = "src/application/img/garbage.png";
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        Image image = new Image(iStream);
		ImageView imageView = new ImageView();
		imageView.setFitWidth(34.0);
		imageView.setFitHeight(65.0);
		imageView.setLayoutX(3.0);
		imageView.setLayoutY(23.0);
		imageView.setPickOnBounds(true);
		imageView.setPreserveRatio(true);
		imageView.setImage(image);
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			if(avvisiDriver.requestDeleteAvviso(idAvviso)) {
				containerNotice.getChildren().clear();
				containerNotice.setPrefHeight(0);
				containerNotice.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 20;");
				Label label = new Label("Messaggio eliminato");
				containerNotice.setCenter(label);
				
				
			} else {
				errorLabel.setText("Errore nella cancellazione del messaggio");
				errorLabel.setTextFill(Color.RED);
			}
        });
		pane.addEventFilter(MouseEvent.MOUSE_ENTERED, MouseEvent -> {
			pane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 60% , #287EED, #FFFBF3); -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none; ");
		});
		pane.addEventFilter(MouseEvent.MOUSE_EXITED, MouseEvent -> {
			pane.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none; ");
		});
		pane.getChildren().add(imageView);
		containerNotice.setLeft(pane);
		BorderPane.setAlignment(imageView, Pos.CENTER);
		return imageView;
	}

	private void generateBodyNotice(Avviso avviso, BorderPane containerNotice) {
		Label body = new Label(avviso.getTesto());
		body.setAlignment(Pos.TOP_LEFT);
		body.setStyle("-fx-border-color: #003F91; -fx-border-style: solid none none solid; -fx-border-width: 2; -fx-background-color: #FFF; -fx-background-radius: 20;");
		body.setPrefHeight(87.0);
		body.setPrefWidth(453.0);
		body.setMaxWidth(1.7976931348623157E308);
		containerNotice.setCenter(body);
	}

	private void generateHeaderNotice(Avviso avviso, BorderPane containerNotice) {
		Label header = new Label(avviso.getDataOra() + " " + avviso.getAutore());
		header.setBackground(new Background(new BackgroundFill(Color.rgb(0, 63, 145), new CornerRadii(20.0, 20.0, 0.0, 0.0, false), Insets.EMPTY)));
		header.setTextFill(Color.WHITE);
		header.setPrefHeight(17.0);
		header.setPrefWidth(453.0);
		header.setMaxWidth(1.7976931348623157E308);
		header.setPadding(new Insets(0, 0, 0 , 10));
		containerNotice.setTop(header);
	}
	
	
}