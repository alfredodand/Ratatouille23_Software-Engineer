package application.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import javafx.scene.control.ProgressIndicator;

import application.driver.MenuDriver;
import application.model.CategoriaMenu;
import application.model.Piatto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ModificaCreaPiattoController {
	@FXML
	TextArea descrizioneInput, descrizioneSecondaLinguaInput;
	@FXML
	TextField nomePiattoInput, nomePiattoAuto, costoInput, allergeniInput, secondaLinguaInput, nomeSecondaLinguaInput;
	@FXML
	ComboBox<String> categoriaInput;
	@FXML
	Label errorLabel;
	@FXML
	Button btnSalvaCambiamenti, autocompletamentoBtn;
	@FXML
	ProgressIndicator progressBar;
	
	private Integer resultIndex = 0;
	private Integer idPiatto = null;
	private ArrayList<CategoriaMenu> categorie = new ArrayList<CategoriaMenu>();
	private Stage stage;
	private Scene scene;
	private Parent parent;
	private String nomeCategoriaNuova;
	private MenuDriver menuDriver = new MenuDriver();
	
	public ModificaCreaPiattoController() {	
		
	}
	
	public ModificaCreaPiattoController(Integer idPiatto) {
		this.idPiatto = idPiatto;
	}

	@FXML
	public void initialize() {

		categorie = menuDriver.getCategorieRistoranteLoggedUserWihoutChangeUI();
		fillComboBoxWithListCategorie(categoriaInput, categorie);
		categoriaInput.getSelectionModel().select(0);
		
		categoriaInput.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if(newValue == "Nuova Categoria") {
				stage = (Stage) (errorLabel.getScene().getWindow());
		        TilePane pane = new TilePane();
		        TextInputDialog td = new TextInputDialog("Nome categoria");
		        td.setHeaderText("Nuova Categoria");
		        Button okButton = (Button) td.getDialogPane().lookupButton(ButtonType.OK);
		        Button annullaButton = (Button) td.getDialogPane().lookupButton(ButtonType.CANCEL);
		        TextField nomeCategoriaNuovaInput = (TextField) td.getEditor();
		        
		        okButton.addEventFilter(ActionEvent.ACTION, MouseEvent -> {
		        	nomeCategoriaNuova = nomeCategoriaNuovaInput.getText();
		        	menuDriver.creaCategoria(nomeCategoriaNuova);
		        	categoriaInput.getItems().add(nomeCategoriaNuova);
		        	categoriaInput.getSelectionModel().select(nomeCategoriaNuova);
		        });
		        
		        annullaButton.addEventFilter(ActionEvent.ACTION, MouseEvent -> {
		        	categoriaInput.getSelectionModel().select(0);
		        });
		        td.show();
		        
			}
		}); 
		
		if(piattoIsUnderModification()) {
			nascondiAutocompletamentoField();
			
			Piatto piatto = menuDriver.getPiattoInfoFromServer(idPiatto);
			
			if(piatto != null) {
				nomePiattoInput.setText(piatto.getNome());
				costoInput.setText(String.valueOf(piatto.getPrezzo()));
				allergeniInput.setText(piatto.getAllergeni());
				descrizioneInput.setText(piatto.getDescrizione());
				descrizioneInput.setWrapText(true);
				
				nomeSecondaLinguaInput.setText(piatto.getNomeSecondaLingua());
				descrizioneSecondaLinguaInput.setText(piatto.getDescrizioneSecondaLingua());
				descrizioneSecondaLinguaInput.setWrapText(true);
				
				categoriaInput.getSelectionModel().select(findCategoriaOfPiatto(piatto.getIdCategoria()).getNome());
			}
			
		} else {
			autocompletamentoBtn.addEventFilter(ActionEvent.ACTION, MouseEvent -> {
				if(!nomePiattoAuto.getText().isBlank()) {
					errorLabel.setText("");
					progressBar.setVisible(true);
					Thread t = new Thread() {
					    public void run() {
							JSONObject result;
							try {
								result = menuDriver.autocompletamentoProdotto(nomePiattoAuto.getText(), resultIndex);
								if(result != null) {
									Platform.runLater(()->{
										nomePiattoInput.setText(result.getString("nome"));
										allergeniInput.setText(result.getString("allergeni"));
										descrizioneInput.setText(result.getString("descrizione"));
										categoriaInput.getItems().add(result.getString("categoria"));
										categoriaInput.getSelectionModel().selectLast();
										descrizioneInput.setWrapText(true);
										autocompletamentoBtn.setText("Non sono soddisfatto del risultato");
										autocompletamentoBtn.getStyleClass().add("buttonRounded1");
										progressBar.setVisible(false);
									});
								}
								resultIndex++;
							} catch (Exception e) {
								Platform.runLater(()->{errorLabel.setText("Errore");
								errorLabel.setTextFill(Color.RED);
								progressBar.setVisible(false);
								});
							}
					    }
					};
					t.start();
				}else {
					errorLabel.setText("Inserisci un nome");
					errorLabel.setTextFill(Color.RED);
				}
			});
			
			
			
		}
		
		btnSalvaCambiamenti.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent -> {
			salvaPiatto();
		});
		
	}

	private void nascondiAutocompletamentoField() {
		autocompletamentoBtn.setMaxHeight(0);
		autocompletamentoBtn.setVisible(false);
		nomePiattoAuto.setMaxHeight(0);
		nomePiattoAuto.setVisible(false);
	}
	
	public void backToMenu() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxmls/MenuScene.fxml"));
		stage = (Stage) (errorLabel.getScene().getWindow());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void fillComboBoxWithListCategorie(ComboBox<String> comboBox, List<CategoriaMenu> listCategoria) {
		comboBox.getItems().add("Categoria");
		for (CategoriaMenu categoriaMenu : listCategoria) {
			comboBox.getItems().add(categoriaMenu.getNome());
		}
		comboBox.getItems().add("Nuova Categoria");
	}

	private CategoriaMenu findCategoriaOfPiatto(Integer idCategoria) {
		for(CategoriaMenu categoriaMenu : categorie) {
			if (categoriaMenu.getIdCategoria() == idCategoria) {
				return categoriaMenu;
			}
		}
		return null;
	}
	
	
	private boolean piattoIsUnderModification() {
		return idPiatto != null;
	}
	
	
	private void salvaPiatto() {
		String nomePiatto = nomePiattoInput.getText();
		String costo = costoInput.getText();
		String descrizione = descrizioneInput.getText();
		String allergeni = allergeniInput.getText();
		String nomeSecondaLingua = nomeSecondaLinguaInput.getText();
		String descrizioneSecondaLingua = descrizioneSecondaLinguaInput.getText();
		String categoriaScelta = categoriaInput.getSelectionModel().getSelectedItem().toString();
		
		if (!(nomePiatto.isBlank() || costo.isBlank() || descrizione.isBlank())) {
			if(piattoIsUnderModification()) {
				if(menuDriver.modificaPiatto(nomePiatto, descrizione, costo, allergeni, nomeSecondaLingua, descrizioneSecondaLingua, categoriaScelta, idPiatto)) {
					errorLabel.setText("Piatto modificato con successo");
					errorLabel.setTextFill(Color.GREEN);
				}else {
					errorLabel.setText("Errore nella modifica del piatto");
					errorLabel.setTextFill(Color.RED);
				}
			}				
			else {
				if(menuDriver.creaPiatto(nomePiatto, descrizione, costo, allergeni, nomeSecondaLingua, descrizioneSecondaLingua, categoriaScelta)) {
					errorLabel.setText("Piatto creato con successo");
					errorLabel.setTextFill(Color.GREEN);
				}else {
					errorLabel.setText("Errore nella creazione del piatto");
					errorLabel.setTextFill(Color.RED);
				}
			}
		} else {
			errorLabel.setText("I campi nome, costo, descrizione e allergeni non possono essere vuoti");
		}
	}
}
