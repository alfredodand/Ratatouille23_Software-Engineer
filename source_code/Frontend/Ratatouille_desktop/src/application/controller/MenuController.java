package application.controller;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import application.Main;
import application.driver.MenuDriver;
import application.model.Avviso;
import application.model.CategoriaMenu;
import application.model.Piatto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class MenuController implements Observer{
	
	boolean isInvisible = true;
	@FXML
	Button accountBtn, homeBtn, noticeBtn, menuBtn, personaleBtn, personalizzaBtn;
	@FXML
	ImageView sidebarBtn;
	@FXML
	Label errorLabel;
	@FXML
	TextField inputField;
	@FXML
	Button filterBtn;
	@FXML
	VBox vBoxLayout;

	@FXML
	private ArrayList<VBox> categorie = new ArrayList<VBox>();
	private ArrayList<Piatto> piatti = new ArrayList<Piatto>();
	private ArrayList<Label> removedTop = new ArrayList<Label>();
	private ArrayList<Label> removedCenter = new ArrayList<Label>();
	private ArrayList<Pane> removedLeft = new ArrayList<Pane>();
	private ArrayList<BorderPane> removedRight = new ArrayList<BorderPane>();
	private ArrayList<BorderPane> removedPane = new ArrayList<BorderPane>();
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private MenuDriver menuDriver = new MenuDriver();
	private SideBarController sideBar = new SideBarController();

	public MenuController()  {
		
	}
	
	@FXML
	public void initialize() {
		menuDriver.requestMenuFromServer(this);
		inputField.setOnKeyPressed(event -> {
			
			if(event.getCode() == KeyCode.BACK_SPACE) {
				int i = 0;
				for(BorderPane pane : removedPane) {
					pane.setTop(removedTop.get(i));
					pane.setRight(removedRight.get(i));
					pane.setLeft(removedLeft.get(i));
					pane.setCenter(removedCenter.get(i));
					pane.setStyle("-fx-border-color: #003F91; -fx-border-radius: 20; -fx-border-width: 2; -fx-border-insets: 3px;");
					i++;
				}
				searchPiatto();
			}
			if (event.getCode().isLetterKey()) {
				searchPiatto();
			}
		});
		filterBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			try {
				goToOrdinaScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
	}
	
	private void searchPiatto() {
		String input = inputField.getText().toString();
		if(!input.isBlank()) {
			ArrayList<Piatto> result = menuDriver.requestSearchPiatto(input);

			for(VBox v : categorie) {
				for(int i = 1; i < v.getChildren().size(); i++) { 
					BorderPane pane = (BorderPane) v.getChildren().get(i);
					Label label = (Label) pane.getTop();
					if(label != null) {
						String textLabel = label.getText().toString();
						
						if(!isPresentInResult(textLabel, result)) {
							//pane.setVisible(false);
							removedCenter.add((Label)pane.getCenter());
							removedTop.add((Label)pane.getTop());
							removedRight.add((BorderPane)pane.getRight());
							removedLeft.add((Pane)pane.getLeft());
							removedPane.add(pane);
							pane.setCenter(null);
							pane.setTop(null);
							pane.setLeft(null);
							pane.setRight(null);
							pane.setLeft(null);
							pane.setStyle("");
							//v.getChildren().remove(i);
						}
					}
				}
			}
			
		}
	}
	

	
	private boolean isPresentInResult(String nomePiatto, ArrayList<Piatto> piatti) {
		for(Piatto piatto : piatti) {
			if (nomePiatto.contains(piatto.getNome())) {
				return true;
			}
		}
		return false;
		
	}
	
	public void goToAggiungiPiatto(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxmls/AggiungiModificaPiattoScene.fxml"));
		ModificaCreaPiattoController modificaCreaPiattoController = new ModificaCreaPiattoController();
		loader.setController(modificaCreaPiattoController);
		Parent root = loader.load();
		stage = (Stage) (accountBtn.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void goToOrdinaScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/OrdinaMenuScene.fxml"));
		stage = (Stage) (filterBtn.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
	
	private VBox findVBox(Integer idCategoria) {
		for(VBox v : categorie) {
			if(Integer.parseInt(v.getId()) == idCategoria) {
				return v;
			}
		}
		return null;
	}
	
	private VBox findVBoxForPiatto(Piatto p) {
		for(VBox vBox : categorie) {
			if(Integer.parseInt(vBox.getId()) == p.getIdCategoria()) {
				return vBox;
			}
		}
		return null;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Piatto) {
			Piatto piatto = (Piatto) o;
			piatti.add(piatto);
			BorderPane containerPiatto = new BorderPane();
			containerPiatto.setStyle("-fx-border-color: #003F91; -fx-border-radius: 20; -fx-border-width: 2; -fx-border-insets: 3px;");
			generateHeaderNotice(piatto, containerPiatto);
			generateBodyNotice(piatto, containerPiatto);
			ImageView imageView = generateLeftGarbageButtonNotice(containerPiatto, piatto.getIdElemento());
			generateRightPositionButtons(piatto, containerPiatto);
			VBox vBox = findVBox(piatto.getIdCategoria());
			vBox.getChildren().add(1, containerPiatto);
		}else if (o instanceof CategoriaMenu) {
			CategoriaMenu categoriaMenu = (CategoriaMenu) o;
			VBox v = findVBox(categoriaMenu.getIdCategoria());
			if (v == null) {
				v = new VBox();
			}
			categorie.add(v);
			v.setId(String.valueOf(categoriaMenu.getIdCategoria()));
			vBoxLayout.getChildren().add(v);
			Label nomeCategoriaLabel = new Label(categoriaMenu.getNome().toUpperCase());
			nomeCategoriaLabel.setAlignment(Pos.CENTER);
			nomeCategoriaLabel.setMaxWidth(Double.MAX_VALUE);
			nomeCategoriaLabel.setTextAlignment(TextAlignment.CENTER);
			nomeCategoriaLabel.setPrefWidth(300);
			nomeCategoriaLabel.getStyleClass().add("menuCategoria");
			String path = "src/application/img/down_arrow.png";
			String path2 = "src/application/img/right_arrow.png";
			InputStream iStream = null;
			InputStream iStream2 = null;
			try {
				iStream = new FileInputStream(path);
				iStream2 = new FileInputStream(path2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	        Image image = new Image(iStream);
	        Image image2 = new Image(iStream2);
			ImageView imageView = new ImageView();
			imageView.setFitWidth(25.0);
			imageView.setFitHeight(34.0);
			imageView.setPickOnBounds(true);
			imageView.setPreserveRatio(true);
			imageView.setImage(image);
			HBox h = new HBox();
			h.setMaxWidth(Float.MAX_VALUE);
			h.setAlignment(Pos.CENTER);
			h.getChildren().add(imageView);
			h.getChildren().add(nomeCategoriaLabel);
			v.getChildren().add(0, h);
			imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
				VBox vBox = findVBox(categoriaMenu.getIdCategoria());
				if(existPiattiInCategoria(vBox)) {
					vBox.getChildren().clear();
					imageView.setImage(image2);
					vBox.getChildren().add(0, h);
				} else {
					imageView.setImage(image);
					menuDriver.requestGetPiattiFromServer(MenuController.this, categoriaMenu.getNome());
				}

			});
		}
	}

	private void generateRightPositionButtons(Piatto piatto, BorderPane containerPiatto) {
		BorderPane pane1 = new BorderPane();
		pane1.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none; ");
		String path = "src/application/img/editing.png";
		ImageView imageView = setImageView(path, 20.0, 20.0, 0.0, 0.0);
		
		pane1.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			try {
				goToModificaPiattoScene(piatto.getIdElemento());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		pane1.setCenter(imageView);
		pane1.addEventFilter(MouseEvent.MOUSE_ENTERED, MouseEvent -> {
			pane1.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 60% , #287EED, #FFFBF3); -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none; ");
		});
		pane1.addEventFilter(MouseEvent.MOUSE_EXITED, MouseEvent -> {
			pane1.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none; ");
		});
		BorderPane.setAlignment(pane1, Pos.CENTER);
		pane1.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
		containerPiatto.setRight(pane1);
	}

	private ImageView setImageView(String path, Double width, Double height, Double x, Double y) {
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        Image image = new Image(iStream);
		ImageView imageView = new ImageView();
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		imageView.setLayoutX(x);
		imageView.setLayoutY(y);
		imageView.setPickOnBounds(true);
		imageView.setPreserveRatio(true);
		imageView.setImage(image);
		return imageView;
	}

	private boolean existPiattiInCategoria(VBox vBox) {
		return vBox.getChildren().size() > 1;
	}
	
	private ImageView generateLeftGarbageButtonNotice(BorderPane containerNotice, Integer idAvviso) {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none;");
		String path = "src/application/img/garbage.png";
        ImageView imageView = setImageView(path, 30.0, 60.0, 10.0, 23.0);
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			if(menuDriver.requestDeletePiatto(MenuController.this, idAvviso)) {
				containerNotice.getChildren().clear();
				containerNotice.setPrefHeight(0);
				containerNotice.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 20;");
				Label label = new Label("Piatto eliminato");
				containerNotice.setCenter(label);
			} else {
				errorLabel.setText("Errore nella cancellazione del messaggio");
				errorLabel.setTextFill(Color.RED);
			}
        });
		pane.getChildren().add(imageView);
		pane.addEventFilter(MouseEvent.MOUSE_ENTERED, MouseEvent -> {
			pane.setStyle("-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 60% , #287EED, #FFFBF3); -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none;");
		});
		
		pane.addEventFilter(MouseEvent.MOUSE_EXITED, MouseEvent -> {
			pane.setStyle("-fx-background-color: #FFFBF3; -fx-background-radius: 20; -fx-border-color: #003F91; -fx-border-width: 2; -fx-border-style: solid none none none;");
		});
		pane.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
		containerNotice.setLeft(pane);
		BorderPane.setAlignment(imageView, Pos.CENTER);
		return imageView;
	}

	private void generateBodyNotice(Piatto piatto, BorderPane containerNotice) {
		Label body = new Label(piatto.getDescrizione());
		body.setWrapText(true);
		body.setAlignment(Pos.TOP_LEFT);
		body.setStyle("-fx-border-color: #003F91; -fx-border-style: solid solid none solid; -fx-border-width: 2; -fx-background-color: #FFF; -fx-background-radius: 20;");
		body.setPrefHeight(87.0);
		body.setPrefWidth(453.0);
		body.setMaxWidth(Double.MAX_VALUE);
		body.setPadding(new Insets(0.0, 0.0, 2.0, 2.0));
		containerNotice.setCenter(body);
	}

	private void generateHeaderNotice(Piatto piatto, BorderPane containerNotice) {
		Label header = new Label(piatto.getNome() + " " + piatto.getPrezzo() + "€");
		header.setBackground(new Background(new BackgroundFill(Color.rgb(0, 63, 145), new CornerRadii(20.0, 20.0, 0.0, 0.0, false), Insets.EMPTY)));
		header.setTextFill(Color.WHITE);
		header.setPrefHeight(17.0);
		header.setPrefWidth(453.0);
		header.setMaxWidth(Double.MAX_VALUE);
		header.setPadding(new Insets(0, 0, 0 , 10));
		containerNotice.setTop(header);
	}
	
	private void goToModificaPiattoScene(Integer idPiatto) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxmls/AggiungiModificaPiattoScene.fxml"));
		ModificaCreaPiattoController modificaCreaPiattoController = new ModificaCreaPiattoController(idPiatto);
		loader.setController(modificaCreaPiattoController);
		Parent root = loader.load();
		stage = (Stage) (accountBtn.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
}
