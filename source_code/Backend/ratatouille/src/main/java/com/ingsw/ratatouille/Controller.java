package com.ingsw.ratatouille;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import com.ingsw.DAOimplements.AvvisoDAOimp;
import com.ingsw.DAOimplements.BusinessDAOimp;
import com.ingsw.DAOimplements.MenuDAOimp;
import com.ingsw.DAOimplements.ProdottoDAOimp;
import com.ingsw.DAOimplements.UserDAOimp;
import com.ingsw.DAOimplements.CategoriaMenuDAOimp;
import com.ingsw.DAOinterface.AvvisoDAOint;
import com.ingsw.DAOinterface.BusinessDAOint;
import com.ingsw.DAOinterface.CategoriaMenuDAOint;
import com.ingsw.DAOinterface.MenuDAOint;
import com.ingsw.DAOinterface.ProdottoDAOint;
import com.ingsw.DAOinterface.UserDAOint;


@RestController
public class Controller {
	DatabaseConnection db = DatabaseConnection.getInstance();
	UserDAOint userDao = new UserDAOimp(db);
	AvvisoDAOint avvisoDao = new AvvisoDAOimp(db);
	MenuDAOint menuDao = new MenuDAOimp(db);
	CategoriaMenuDAOint categoriaMenuDao = new CategoriaMenuDAOimp(db);
	ProdottoDAOint prodottoDao = new ProdottoDAOimp(db);
	BusinessDAOint businessDao = new BusinessDAOimp(db);
	

	@GetMapping("/user")
	public ArrayList<User> getUser(@RequestParam(required = false) Integer id_ristorante){
		if(id_ristorante == null) 
			return userDao.getUser();
		else {
			return userDao.getUserOfResturant(id_ristorante);
		}
	}
	
	@PutMapping("/user/account")
	public User modifyUserAccount(@RequestBody(required = true) User user){
		return userDao.modifyUserNameSurnameDate(user.getIdUtente(), user.getNome(), user.getCognome(), user.getDataNascita());
	}
	
	@PutMapping("/user/accountDesktop")
	public User modifyUserAccountDesktop(@RequestBody(required = true) User user){
		return userDao.modifyUserNameSurnameEmail(user.getIdUtente(), user.getNome(), user.getCognome(), user.getEmail());
	}
	
	@PutMapping("/user/upgradeRole")
	public User upgradeUserRole(@RequestBody(required = true) User user){
		return userDao.upgradeUserRole(user.getIdUtente(), user.getRuolo());
	}
	
	@PutMapping("/user/downgradeRole/{ruoloLogged}")
	public User downgradeUserRole(@PathVariable String ruoloLogged, @RequestBody(required = true) User user){
		return userDao.downgradeUserRole(ruoloLogged, user.getIdUtente(), user.getRuolo());
	}
	
	@PostMapping("/user/fire")
	public void fireUser(@RequestParam(required = true) int idUtente, String ruolo, String ruoloLogged){
		userDao.fireUser(ruoloLogged, idUtente, ruolo);
	}

	@PostMapping("/signup-admin")
    public User createAdmin(@RequestParam (required = true) String nome, String cognome, String email, String password, String dataNascita, String nomeAttivita) {
		return userDao.createAdmin(nome, cognome, email, password, dataNascita, nomeAttivita);
	}
	
	@PostMapping("/signup/newEmployee")
    public User createUser(@RequestParam (required = true) String nome, String cognome, String passwordTemporanea, String email, String dataNascita, String ruolo, Integer idUtente, Integer idRistorante) {
		return userDao.createEmployee(nome, cognome, passwordTemporanea, email, dataNascita, ruolo, idUtente, idRistorante);
	}
	
	@PostMapping("/verify")
    public User verifyUser(@RequestParam (required = true) String email, String nomeRistorante) {
		return userDao.verifyEmployee(email, nomeRistorante);
	}
	
	@PutMapping("/user/first-access")
    public User firstAccess(@RequestBody (required = true) User user) {
		return userDao.firstAccess(user.getIdUtente(), user.getPassword());
	}

	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable Integer id){
		return userDao.getUserById(id);
	}
	
	
	@PostMapping("/login")
	public User checkLogin(HttpServletRequest request, @RequestParam(required = true) String email, String password){
		return (LoggedUser)request.getSession().getAttribute("attributeToPass");
	}
	
	
	@GetMapping("/avvisi")
	public ArrayList<Avviso> getAvvisi(@RequestParam(required = false) Integer id_ristorante){
		if(id_ristorante == null) 
			return avvisoDao.getAvvisi();
		else {
			return avvisoDao.getAvvisiOfResturant(id_ristorante);
		}
	}		

	@GetMapping("/avvisi-hidden/{id_user}")
	public ArrayList<AvvisoNascostoVisto> getAvvisiHidden(@PathVariable Integer id_user){
		return avvisoDao.getAvvisiHiddenOf(id_user);
	}
	
	
	@GetMapping("/avvisi-viewed/{id_user}")
	public ArrayList<AvvisoNascostoVisto> getAvvisiViewed(@PathVariable Integer id_user){
		return avvisoDao.getAvvisiViewedOf(id_user);
	}
	
	@PostMapping("/avviso/segna-come-letto/{id_avviso}")
	public AvvisoNascostoVisto setAvvisoViewed(@PathVariable Integer id_avviso, Integer idUtente) {
		return avvisoDao.setAvvisoViewed(id_avviso, idUtente);
	}
	
	@PostMapping("/avviso/segna-come-non-letto/{id_avviso}")
	public AvvisoNascostoVisto setAvvisoNotViewed(@PathVariable Integer id_avviso, Integer idUtente) {
		return avvisoDao.setAvvisoNotViewed(id_avviso, idUtente);
	}
	
	@PostMapping("/avviso/segna-come-nascosto/{id_avviso}")
	public AvvisoNascostoVisto setAvvisoHidden(@PathVariable Integer id_avviso, Integer idUtente) {
		return avvisoDao.setAvvisoHidden(id_avviso, idUtente);
	}
	
	@PostMapping("/avviso/segna-come-non-nascosto/{id_avviso}")
	public AvvisoNascostoVisto setAvvisoNotHidden(@PathVariable Integer id_avviso, Integer idUtente) {
		return avvisoDao.setAvvisoNotHidden(id_avviso, idUtente);
	}

	@GetMapping("/avviso/numero-di-avvisi-da-leggere")
	public Integer getNumberOfAvvisiToRead(@RequestParam Integer id_utente, Integer id_ristorante) {
		return avvisoDao.getNumberOfAvvisiToRead(id_utente, id_ristorante);
	}
	
	@PostMapping("/avviso/crea")
	public Avviso createNewAvviso(@RequestParam(required = true) Integer id_ristorante, String testo, Integer idUtente) {
		return avvisoDao.createNewAvviso(id_ristorante, testo, idUtente);
	}

	@DeleteMapping("/avviso/cancella/{id_avviso}")
	public boolean deleteAvviso(@PathVariable(required = true) Integer id_avviso) {
		return avvisoDao.deleteAvviso(id_avviso);
	}
	
	@GetMapping("/menu")
	public ArrayList<Menu> getMenu(@RequestParam(required = false) Integer id_ristorante){
		if(id_ristorante == null) 
			return menuDao.getMenu();
		else {
			return menuDao.getMenuFromRestaurant(id_ristorante);
		}
	}
	
	@GetMapping("/menu/categoria")
	public ArrayList<CategoriaMenu> getMenuCategories(@RequestParam(required = false) Integer id_ristorante){
		if(id_ristorante == null) 
			return categoriaMenuDao.getMenuCategories();
		else {
			return categoriaMenuDao.getMenuCategoriesFromRestaurant(id_ristorante);
		}
	}
	
	@GetMapping("/menu/categoria/piatti")
	public ArrayList<Menu> getMenuPlateByCategory(@RequestParam(required = true) Integer id_ristorante, String categoria){
		return menuDao.getMenuCategories(id_ristorante, categoria);
	}

	@PutMapping("/menu/piatto-update-posizione")
	public boolean updatePosizionePiatto(@RequestBody(required = true) Menu menu){
		return menuDao.updatePosizionePiatto(menu.getIdElemento(), menu.getPosizione());
	}

	@DeleteMapping("/menu/delete-ordine-menu-precedente/{id_ristorante}")
	public boolean deleteSortingMenu(@PathVariable(required = true) Integer id_ristorante){
		return menuDao.deleteSortingMenu(id_ristorante) && categoriaMenuDao.deleteSortingMenu(id_ristorante);
	}

	@PutMapping("/categoria-update-posizione")
	public boolean updatePosizioneCategoria(@RequestBody(required = true) CategoriaMenu categoriaMenu){
		return categoriaMenuDao.updatePosizioneCategoria(categoriaMenu.getIdCategoria(), categoriaMenu.getPosizione());
	}
	
	@GetMapping("/menu/ristorante/piatto")
	public ArrayList<Menu> getMenuPlateFromRestaurant(@RequestParam(required = true) Integer id_ristorante, String nomePiatto){
		return menuDao.getMenuPlateInRestaurant(id_ristorante, nomePiatto);
	}

	@GetMapping("/menu/ristorante/piatto/{id_piatto}")
	public Menu getPlateById(@PathVariable Integer id_piatto){
		return menuDao.getPiattoById(id_piatto);
	}

	@GetMapping("/menu/piatto")
	public ArrayList<Menu> getMenuPlateByName(@RequestParam(required = true) String nomePiatto){
		return menuDao.getMenuPlate(nomePiatto);
	}

	@PostMapping("/menu/newCategoria")
	public boolean createCategoria(@RequestParam(required = true) String nomeNuovaCategoria, Integer idRistorante){
		return categoriaMenuDao.createCategoria(idRistorante, nomeNuovaCategoria);
	}

	@PostMapping("/menu/newPlate")
    public boolean createPlate(@RequestParam (required = true) Integer idRistorante, String categoria, String nome, float prezzo, String descrizione, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua) {
		return menuDao.createPlate(idRistorante, categoria, nome, prezzo, descrizione, allergeni, nomeSecondaLingua, descrizioneSecondaLingua);
	}

	@PutMapping("/menu/updatePlate/{id_piatto}")
	public boolean updatePlate(@PathVariable Integer id_piatto, @RequestBody (required = true) Menu menu) {
		return menuDao.updatePlate(id_piatto, menu.getIdRistorante(), menu.getNomeCategoria(), menu.getNome(), menu.getPrezzo(), menu.getDescrizione(), menu.getAllergeni(), menu.getNomeSecondaLingua(), menu.getDescrizioneSecondaLingua());
	}

	@DeleteMapping("/menu/deletePlate/{idPiatto}")
	public boolean deletePlate(@PathVariable (required = true) Integer idPiatto) {
		return menuDao.deletePlate(idPiatto);
	}
		
	
	@PostMapping("/menu/piatto/secondaLingua")
	public Menu addSecondLanguage (@RequestParam (required = true) Integer idRistorante, int idProdotto, String nomeSecondaLingua, String descrizoineSecondaLingua) {
		return menuDao.addSecondLanguage(idRistorante, idProdotto, nomeSecondaLingua, descrizoineSecondaLingua);
	}
	
	@GetMapping("/dispensa")
	public ArrayList<Prodotto> getDispensa(@RequestParam(required = false) Integer id_ristorante){
		if(id_ristorante == null) 
			return prodottoDao.getDispensa();
		else {
			return prodottoDao.getDispensaFromRestaurant(id_ristorante);
		}
	}
	
	@GetMapping("/dispensa/categoria")
	public ArrayList<Prodotto> getDispensaProductsByCategory(@RequestParam(required = true) Integer id_ristorante, String categoria){
		return prodottoDao.getDispensaProduct(id_ristorante, categoria);
	}
	
	@GetMapping("/dispensa/prodotto")
	public ArrayList<Prodotto> getDispensaProductByName(@RequestParam(required = true)Integer id_ristorante, String nomeProdotto){
		return prodottoDao.getDispensaProductByName(id_ristorante, nomeProdotto);
	}
	
	@GetMapping("/dispensa/prodotto/disponibili")
	public ArrayList<Prodotto> getAvaiableDispensaProduct(@RequestParam(required = true) Integer id_ristorante){
		return prodottoDao.getAvaiableDispensaProduct(id_ristorante);
	}
	
	@PostMapping("/dispensa/newProduct")
    public Prodotto createProduct(@RequestParam (required = true) Integer idRistorante, String nome, Integer stato, String descrizione, Double prezzo, Double quantita, String unitaMisura, String categoria) {
		return prodottoDao.createProduct(idRistorante, nome, stato, descrizione, prezzo, quantita, unitaMisura, categoria);
	}
	
	@PutMapping("/dispensa/modifyProduct")
    public Prodotto modifyProduct(@RequestBody (required = true) Prodotto prodotto) {
		return prodottoDao.modifyProduct(prodotto.getIdProdotto(), prodotto.getNome(), prodotto.getStato(), prodotto.getDescrizione(), prodotto.getPrezzo(), prodotto.getQuantita(), prodotto.getUnitaMisura(), prodotto.getCategoria());
	}
	
	@PostMapping("/dispensa/deleteProduct")
    public Prodotto deleteProduct(@RequestParam (required = true) Integer idProdotto) {
		return prodottoDao.deleteProduct(idProdotto);
	}
	
	@GetMapping("/error")
	public String getError() {
		return "errore";
	}
	
	@PostMapping("/logout")
	public String logout(@RequestParam (required = true) Integer idUtente) {
		return "logged out";
	}
	
	@GetMapping("/business/nomeAttivita")
	public Business getBusinessFromBusinessId(@RequestParam(required = true) Integer id_ristorante){
		return businessDao.getBusinessFromBusinessId(id_ristorante);
	}
	
	@PutMapping("/business")
	public Business modifyBusinessInfo(@RequestBody(required = true) Business business){
		return businessDao.modifyBusinessInfo(business.getIdRistorante(), business.getNome(), business.getIndirizzo(), business.getNumeroTelefono());
	}
	
	@PostMapping("/business/image")
	public void saveBusinessImage(@RequestParam(required = true) Integer idUtente, Integer idRistorante, MultipartFile image, String fileName){
		try {
			businessDao.saveBusinessImage(idUtente, idRistorante, image, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/business/getImage")
	public String getBusinessImage(@RequestParam(required = true) Integer idUtente, Integer idRistorante){
		return businessDao.getBusinessImage(idUtente, idRistorante);
	}
	
}