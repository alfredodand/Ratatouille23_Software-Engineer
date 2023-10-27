package application.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import application.driver.MenuDriver;
import application.model.CategoriaMenu;
import application.model.Piatto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OrdinaMenuController {
	public OrdinaMenuController() {	}
	@FXML
	ComboBox<String> comboboxInput;
	@FXML
	VBox vBoxLayout2;
	@FXML
	Label errorLabel, nomeCategoriaAttuale;
	@FXML
	Button aggiungiBtn;
	@FXML
	Button categoriaSuccessivaBtn;
	@FXML
	Button categoriaPrecedenteBtn;
	@FXML
	HBox hBoxLayout, hBoxLayout1;
	@FXML
	BorderPane panelLayout;
	@FXML
	Button resetBtn;
	
	@FXML
	Button applica;
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	
	private MenuDriver menuDriver = new MenuDriver();
	private ArrayList<CategoriaMenu> categorieForUI;
	private ArrayList<CategoriaMenu> categorie;
	private Integer categoriaCounter = 1;
	private ArrayList<Piatto> piatti = null;
	private ArrayList<Piatto> allPiattiOfResturant = new ArrayList<Piatto>();
	private ArrayList<VBox> categorieVBoxs = new ArrayList<>();
	private Stack<VBox> savedStateLeft = new Stack<VBox>();
	private Stack<VBox> savedStateRight = new Stack<VBox>();
	private Integer pagesDiscovered = 1, numOfCategorie = 0;
	private ArrayList<CategoriaMenu> queueCategoria = new ArrayList<CategoriaMenu>();
	private Integer indexOfCategoria = 0;
	private boolean backPressed = false, resetPressed = false, existsMenu = false;
	private VBox menuVBox;
	
	
	public void backToMenu() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/MenuScene.fxml"));
		stage = (Stage) (errorLabel.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private boolean existAlreadyAnMenu(List<Piatto> menu) {
		for (Piatto piatto : menu) {
			if(piatto.getPosizione() != null)
				return true;
		}
		return false;
	}
	
	@FXML
	public void initialize(){
		try {
			errorLabel.setTextFill(Color.RED);
			categoriaPrecedenteBtn.setDisable(true);
			nomeCategoriaAttuale.setText("Categoria " + indexOfCategoria);
			allPiattiOfResturant = menuDriver.getAllPiattiOfMenu();
			categorieForUI = menuDriver.getCategorieRistoranteLoggedUserWihoutChangeUI();
			categorie = new ArrayList<>(categorieForUI);
			numOfCategorie = categorieForUI.size();
			if(numOfCategorie == 1){
				categoriaSuccessivaBtn.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(allPiattiOfResturant.size() == 0) {
			errorLabel.setText("Non puoi ordinare un menù vuoto");
			comboboxInput.setDisable(true);
			aggiungiBtn.setDisable(true);
			categoriaPrecedenteBtn.setDisable(true);
			categoriaSuccessivaBtn.setDisable(true);
			applica.setDisable(true);
			resetBtn.setDisable(true);
		}
		existsMenu = existAlreadyAnMenu(allPiattiOfResturant);
		if(existsMenu) {
			visualizzaMenuAttuale();
		}
		
		for (CategoriaMenu categoriaMenu : categorieForUI) {
			comboboxInput.getItems().add(categoriaMenu.getNome());
		}
		comboboxInput.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			comboboxInput.setDisable(true);
			
		}); 
		
		aggiungiBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			aggiungiBtn.setDisable(true);
			try {
				primaCategoriaScelta();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		categoriaSuccessivaBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			categoriaSuccessivaBtn.setDisable(false);
            categoriaPrecedenteBtn.setDisable(false);
            if((savedStateLeft.size() == numOfCategorie - 2)) {
                categoriaSuccessivaBtn.setDisable(true);
            }
			resetBtn.setDisable(false);
			errorLabel.setText("");
			if(isSelectedSomethingInMainCombobox()) {
				if(!savedStateLeft.contains((VBox)panelLayout.getCenter()))
					savedStateLeft.add((VBox)panelLayout.getCenter());
				indexOfCategoria = savedStateLeft.size();
				if (savedStateLeft.size() < numOfCategorie) {
					if(backPressed) {
						if(savedStateRight.size() > 0) {
							panelLayout.setCenter(savedStateRight.pop());
						}
						else {
							backPressed = false;
							pagesDiscovered++;
							comboboxInput.getItems().clear();
							fillComboBoxWithListCategorie(comboboxInput, categorieForUI);
							panelLayout.setCenter(null);
							comboboxInput.setDisable(false);
							aggiungiBtn.setDisable(false);	
						}
					} else { 
							pagesDiscovered++;
							comboboxInput.getItems().clear();
							comboboxInput.setPromptText("Categoria " + indexOfCategoria);
							fillComboBoxWithListCategorie(comboboxInput, categorieForUI);
							panelLayout.setCenter(null);
							comboboxInput.setDisable(false);
							aggiungiBtn.setDisable(false);		
					}
				} else {
					errorLabel.setText("Non ci sono altre categorie");
				}
			} else {
				errorLabel.setText("Per andare avanti devi compilare la categoria");
			}
			nomeCategoriaAttuale.setText("Categoria " + indexOfCategoria);
		});
		
		categoriaPrecedenteBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			categoriaPrecedenteBtn.setDisable(false);
            categoriaSuccessivaBtn.setDisable(false);
            if(savedStateLeft.size() == 1) {
                categoriaPrecedenteBtn.setDisable(true);
            }
			if(isSelectedSomethingInMainCombobox()){
				resetBtn.setDisable(false);
				errorLabel.setText("");
				backPressed = true;
				if(!savedStateRight.contains((VBox)panelLayout.getCenter()))
					savedStateRight.add((VBox)panelLayout.getCenter());
				if(savedStateLeft.size() > 0) {
					panelLayout.setCenter(savedStateLeft.pop());
					indexOfCategoria = savedStateLeft.size();
					comboboxInput.setDisable(true);
					aggiungiBtn.setDisable(true);				
				}else {
					errorLabel.setText("Non ci sono altre categorie");
				}
			}else {
				errorLabel.setText("Per andare avanti devi compilare la categoria");
			}
			nomeCategoriaAttuale.setText("Categoria " + indexOfCategoria);
		});
		
		resetBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			if(isSelectedSomethingInMainCombobox()){
				comboboxInput.getItems().clear();
				if(queueCategoria.size() > 0) {
					CategoriaMenu actualCategoria = queueCategoria.get(indexOfCategoria);
					queueCategoria.remove(actualCategoria);
					VBox vBox = findVBoxContainsCategoria(actualCategoria.getNome());
					if(vBox != null) {
						vBox.getChildren().clear();
						vBox = null;
						categorieVBoxs.remove(vBox);
						categorieForUI.add(actualCategoria);
					}
				}
				fillComboBoxWithListCategorie(comboboxInput, categorieForUI);
				
				panelLayout.setCenter(null);
				comboboxInput.setDisable(false);
				aggiungiBtn.setDisable(false);
			} else {
				errorLabel.setText("Ancora non hai compilato niente");
			}
			
		});
		
		applica.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			boolean success = true;
			menuDriver.requestDeleteMenuSorting();
			int posizioneCategoria = 1, posizionePiatto = 1;
			for (VBox vBox : categorieVBoxs) {
				posizionePiatto = 1;
				if(!vBox.getChildren().isEmpty()) {
					Label nomeCategoria = (Label) vBox.getChildren().get(0);
					Integer idCategoria = getCategoriaIdFromNome(nomeCategoria.getText());
					try {
						success = success && menuDriver.requestUpdatePositionCategoria(idCategoria, posizioneCategoria);
					} catch (Exception e) {
						e.printStackTrace();
					}
					for(int i = 1; i < vBox.getChildren().size(); i++) {
						Label nomePiatto = (Label) vBox.getChildren().get(i);
						Integer idPiatto = getPiattoIdFromNome(nomePiatto.getText());
						success = success && menuDriver.requestUpdatePositionInMenuPiatto(idPiatto, posizionePiatto);
						posizionePiatto++;
					}
				}
				posizioneCategoria++;
			}
			if(success && existsMenu) {
				hBoxLayout1.getChildren().remove(menuVBox);
				try {
					categorie = menuDriver.getCategorieRistoranteLoggedUserWihoutChangeUI();
					allPiattiOfResturant = menuDriver.getAllPiattiOfMenu();
				} catch (Exception e) {
					e.printStackTrace();
				}
				visualizzaMenuAttuale();
			}
		});
	}

	private void visualizzaMenuAttuale() {
		menuVBox = new VBox();
		menuVBox.setPrefWidth(250);
		menuVBox.setMinWidth(180);
		menuVBox.setMaxWidth(Double.MAX_VALUE);
		Label spacingLabel = new Label();
		menuVBox.getChildren().add(spacingLabel);
		Label oldMenuLabel = new Label("MENU ATTUALE");
		oldMenuLabel.setMaxWidth(Double.MAX_VALUE);
		oldMenuLabel.setAlignment(Pos.CENTER);
		menuVBox.getChildren().add(oldMenuLabel);
		Collections.sort(allPiattiOfResturant, (o1, o2) -> o1.getPosizione().compareTo(o2.getPosizione()));
		
		for(CategoriaMenu categoriaMenu : categorie) {
			if(categoriaMenu.getPosizione() != 0) {
				Label categoriaLabel = new Label(categoriaMenu.getNome());
				categoriaLabel.setStyle("-fx-text-fill: #003F91");
				menuVBox.getChildren().add(categoriaLabel);
				for(Piatto piatto : allPiattiOfResturant) {
					if(categoriaMenu.getIdCategoria() == piatto.getIdCategoria()) {
						if(piatto.getPosizione() != 0) {
							Label piattoLabel = new Label(piatto.getNome());
							menuVBox.getChildren().add(piattoLabel);
						}
					}
				}
			}
		}
		hBoxLayout1.getChildren().add(menuVBox);
	}

	private Integer getCategoriaIdFromNome(String nomeInput) {
		for(CategoriaMenu categoriaMenu : categorie) {
			if(categoriaMenu.getNome().equals(nomeInput)) {
				return categoriaMenu.getIdCategoria();
			}
		}
		return null;
	}
	
	private Integer getPiattoIdFromNome(String nomeInput) {
		for(Piatto piatto : allPiattiOfResturant) {
			if(piatto.getNome().equals(nomeInput)) {
				return piatto.getIdElemento();
			}
		}
		return null;
	}

	private boolean isSelectedSomethingInMainCombobox() {
		return !(comboboxInput.getSelectionModel().getSelectedItem() == null && !comboboxInput.isDisabled());
	}
	
	public void primaCategoriaScelta() throws Exception {
		ComboBox<String> comboBox = new ComboBox<String>();
		String categoriaScelta = comboboxInput.getSelectionModel().getSelectedItem();	
		
		if(categoriaScelta == null) {
			comboboxInput.getSelectionModel().select(0);
			categoriaScelta = comboboxInput.getSelectionModel().getSelectedItem();
			generateInputStructureForSortOfMenu(comboBox, categoriaScelta); 
		} else {
			generateInputStructureForSortOfMenu(comboBox, categoriaScelta); 
		}
		
	}

	private void generateInputStructureForSortOfMenu(ComboBox<String> comboBox, String categoriaScelta) throws Exception {
		removeFromListCategoria(categoriaScelta, categorieForUI);
		Label categoria = new Label(categoriaScelta);
		categoria.setStyle("-fx-text-fill: #003F91");
		VBox categoriaVBox = new VBox();
		categoriaVBox.getChildren().add(categoria);
		categorieVBoxs.add(categoriaVBox);
		Integer tmp = indexOfCategoria + 2;
		vBoxLayout2.getChildren().add(tmp, categoriaVBox);
		piatti = fillPiattiOfCategoria(categoriaScelta);
		VBox vBox = new VBox();
		panelLayout.setCenter(vBox);
		generatePiattoChooseCombobox(comboBox, vBox, categoriaVBox);
	}
	
	private VBox findVBoxContainsCategoria(String nomeCategoria) {
		for(VBox v : categorieVBoxs) {
			if(!v.getChildren().isEmpty()) {
				Label nomeCategoriaLabel = (Label) v.getChildren().get(0);
				if(nomeCategoriaLabel.getText().toString().contains(nomeCategoria)) {
					return v;
				}	
			}
		}
		return null;
	}

	private void fillComboBoxWithListPiatti(ComboBox<String> comboBox) {
		for (Piatto piatto : piatti) {
			comboBox.getItems().add(piatto.getNome());
		}
	}
	
	private void fillComboBoxWithListCategorie(ComboBox<String> comboBox, List<CategoriaMenu> listCategoria) {
		for (CategoriaMenu categoriaMenu : listCategoria) {
			comboBox.getItems().add(categoriaMenu.getNome());
		}
	}
	

	private void generatePiattoChooseCombobox(ComboBox<String> comboBox, VBox vBoxLayout, VBox categoriaVBox) {
		if(piatti.size() != 0) {
			fillComboBoxWithListPiatti(comboBox);
			vBoxLayout.getChildren().add(comboBox);
			comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
				comboBox.setDisable(true);
				String piattoScelto = comboBox.getSelectionModel().getSelectedItem();
				removeFromListPiatto(piattoScelto, piatti);
				if(piattoScelto == null) {
					errorLabel.setText("Errore");
				}else {
					Label piatto = new Label(piattoScelto);
					categoriaVBox.getChildren().add(piatto);
					generatePiattoChooseCombobox(new ComboBox<String>(), vBoxLayout, categoriaVBox);
				}
			});
		}else {
			errorLabel.setText("Non ci sono più elementi in questa categoria");
		}
	}

	private void removeFromListPiatto(String piattoScelto, List<Piatto> piatti) {
		for (Iterator iterator = piatti.iterator(); iterator.hasNext();) {
			Piatto piatto = (Piatto) iterator.next();
			if(piatto.getNome() == piattoScelto) {
				iterator.remove();
			}
		}
	}
	
	public CategoriaMenu removeFromListCategoria(String categoriaScelta, List<CategoriaMenu> categorie) {
		if(categorie != null) {
			for (Iterator iterator = categorie.iterator(); iterator.hasNext();) {
				CategoriaMenu categoria = (CategoriaMenu) iterator.next();
				if(categoria != null) {
					if(categoria.getNome().equals(categoriaScelta)) {
						queueCategoria.add(indexOfCategoria, categoria);
						iterator.remove();
						return categoria;
					}
				}
			}
		}
		return null;
	}

	private ArrayList<Piatto> fillPiattiOfCategoria(String categoriaScelta) throws Exception {
		ArrayList<Piatto> piatti = menuDriver.getPiattiFromCategorieWithoutChangeUI(categoriaScelta);
		return piatti;
	}
	
}