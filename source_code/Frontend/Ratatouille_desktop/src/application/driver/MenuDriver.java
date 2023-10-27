package application.driver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Main;
import application.controller.LoginController;
import application.controller.MenuController;
import application.model.CategoriaMenu;
import application.model.Piatto;
import application.model.Utente;

public class MenuDriver {
	private Utente loggedUser = LoginController.loggedUser;
	private ArrayList<CategoriaMenu> categorieRistorante = new ArrayList<CategoriaMenu>();
	private String url = Main.address;
	public MenuDriver() { }
	
	
	public void requestMenuFromServer(MenuController menuController) {
		try {
			getCategorieFromServer(loggedUser.getIdRistorante(), menuController);
			for(CategoriaMenu categoriaMenu : categorieRistorante)
				getPiattiFromCategorie(loggedUser.getIdRistorante(), menuController, categoriaMenu.getNome());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void requestGetPiattiFromServer(MenuController menuController, String categoria) {
		try {
			getPiattiFromCategorie(loggedUser.getIdRistorante(), menuController, categoria);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPiattiFromCategorie(Integer idRistorante, MenuController menuController, String categoria) throws Exception {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu/categoria/piatti?id_ristorante=" + idRistorante + "&&categoria=" + URLEncoder.encode(categoria,"UTF-8"));
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nomeSecondaLingua = "";
				String descrizioneSecondaLingua = "";
				
				if(!jsonObject.isNull("descrizioneSecondaLingua"))
					descrizioneSecondaLingua = jsonObject.getString("descrizioneSecondaLingua");
				
				if(!jsonObject.isNull("nomeSecondaLingua"))
					nomeSecondaLingua = jsonObject.getString("nomeSecondaLingua");
					
				if(jsonObject.has("idElemento") && jsonObject.has("idRistorante")) {
					Piatto a = new Piatto(menuController, jsonObject.getInt("idElemento"), jsonObject.getInt("idRistorante"), jsonObject.getInt("idCategoria"), jsonObject.getString("nome"), jsonObject.getFloat("prezzo"), jsonObject.getString("descrizione"), jsonObject.getString("allergeni"), nomeSecondaLingua, descrizioneSecondaLingua, jsonObject.getInt("posizione"));
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		}
	}

	private void getCategorieFromServer(Integer idRistorante, MenuController menuController) throws Exception {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu/categoria?id_ristorante=" + idRistorante);
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.has("idCategoria") && jsonObject.has("idRistorante")) {
					CategoriaMenu a = new CategoriaMenu(menuController, jsonObject.getInt("idCategoria"), jsonObject.getInt("idRistorante"), jsonObject.getString("nome"), jsonObject.getInt("posizione"));
					categorieRistorante.add(a);
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		}
	}
	
	public ArrayList<CategoriaMenu> getCategorieRistoranteLoggedUserWihoutChangeUI(){
		ArrayList<CategoriaMenu> categorie = new ArrayList<CategoriaMenu>();
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu/categoria?id_ristorante=" + loggedUser.getIdRistorante());
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.has("idCategoria") && jsonObject.has("idRistorante")) {
					CategoriaMenu a = new CategoriaMenu(jsonObject.getInt("idCategoria"), jsonObject.getInt("idRistorante"), jsonObject.getString("nome"), jsonObject.getInt("posizione"));
					categorie.add(a);
				}
			}
			return categorie;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		}
		return null;
	}
	
	public ArrayList<Piatto> getPiattiFromCategorieWithoutChangeUI(String categoria) throws Exception {
		ArrayList<Piatto> piatti = new ArrayList<Piatto>();
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu/categoria/piatti?id_ristorante=" + loggedUser.getIdRistorante() + "&&categoria=" + URLEncoder.encode(categoria, "UTF-8"));
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nomeSecondaLingua = "";
				String descrizioneSecondaLingua = "";
				
				if(!jsonObject.isNull("descrizioneSecondaLingua"))
					descrizioneSecondaLingua = jsonObject.getString("descrizioneSecondaLingua");
				
				if(!jsonObject.isNull("nomeSecondaLingua"))
					nomeSecondaLingua = jsonObject.getString("nomeSecondaLingua");
					
				if(jsonObject.has("idElemento") && jsonObject.has("idRistorante")) {
					Piatto piatto = new Piatto(jsonObject.getInt("idElemento"), jsonObject.getInt("idRistorante"), jsonObject.getInt("idCategoria"), jsonObject.getString("nome"), jsonObject.getFloat("prezzo"), jsonObject.getString("descrizione"), jsonObject.getString("allergeni"), nomeSecondaLingua, descrizioneSecondaLingua, jsonObject.getInt("posizione"));
					piatti.add(piatto);
				}
			}
			return piatti;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		}
		return null;
	}

	public ArrayList<Piatto> getAllPiattiOfMenu() throws Exception {
		ArrayList<Piatto> piatti = new ArrayList<>();
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu?id_ristorante=" + loggedUser.getIdRistorante());
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nomeSecondaLingua = "";
				String descrizioneSecondaLingua = "";
				
				if(!jsonObject.isNull("descrizioneSecondaLingua"))
					descrizioneSecondaLingua = jsonObject.getString("descrizioneSecondaLingua");
				
				if(!jsonObject.isNull("nomeSecondaLingua"))
					nomeSecondaLingua = jsonObject.getString("nomeSecondaLingua");
					
				if(jsonObject.has("idElemento") && jsonObject.has("idRistorante")) {
					Piatto a = new Piatto(jsonObject.getInt("idElemento"), jsonObject.getInt("idRistorante"), jsonObject.getInt("idCategoria"), jsonObject.getString("nome"), jsonObject.getFloat("prezzo"), jsonObject.getString("descrizione"), jsonObject.getString("allergeni"), nomeSecondaLingua, descrizioneSecondaLingua, jsonObject.getInt("posizione"));
					piatti.add(a);
				}
			}
			return piatti;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		}
		return null;
	}
	
	public boolean requestDeletePiatto(MenuController menuController, Integer idPiatto) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpDelete httpDelete = new HttpDelete(url + "/menu/deletePlate/" + idPiatto.toString());
			httpDelete.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpDelete);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		} catch (ClientProtocolException e) {
			System.out.print("Errore nel client");
		} catch (IOException e) {
			System.out.print("Errore IO Exception");
		}
		return false;
	}

	
	public ArrayList<Piatto> requestSearchPiatto(String nomePiatto) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			String restUrl = url + "/menu/ristorante/piatto?id_ristorante="+String.valueOf(loggedUser.getIdRistorante())+"&&nomePiatto="+ URLEncoder.encode(nomePiatto, "UTF-8");
			HttpGet httpget = new HttpGet(restUrl);
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			ArrayList<Piatto> result = new ArrayList<Piatto>();
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nomeSecondaLingua = "";
				String descrizioneSecondaLingua = "";
				
				if(!jsonObject.isNull("descrizioneSecondaLingua"))
					descrizioneSecondaLingua = jsonObject.getString("descrizioneSecondaLingua");
				
				if(!jsonObject.isNull("nomeSecondaLingua"))
					nomeSecondaLingua = jsonObject.getString("nomeSecondaLingua");
					
				if(jsonObject.has("idElemento") && jsonObject.has("idRistorante")) {
					Piatto a = new Piatto(jsonObject.getInt("idElemento"), jsonObject.getInt("idRistorante"), jsonObject.getInt("idCategoria"), jsonObject.getString("nome"), jsonObject.getFloat("prezzo"), jsonObject.getString("descrizione"), jsonObject.getString("allergeni"), nomeSecondaLingua, descrizioneSecondaLingua, jsonObject.getInt("posizione"));
					result.add(a);
				}
			}
			return result;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		} catch (UnsupportedEncodingException e) {
			System.out.print("Encoding non supportato");
		} catch (ParseException e) {
			System.out.print("Errore nel parsing del JSON");
		} catch (IOException e) {
			System.out.print("Errore IO Exception");
		}
		return null;
	}
	


	public boolean requestUpdatePositionCategoria(Integer idCategoria, Integer posizione) throws Exception {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url + "/categoria-update-posizione");
		httpput.setHeader("Authorization", loggedUser.getToken());
		httpput.setHeader("Content-type", "application/json");
		
		
		JSONObject requestparams = new JSONObject();
		requestparams.put("idCategoria", idCategoria.toString());
		requestparams.put("posizione", posizione.toString());
		httpput.setEntity(new StringEntity(requestparams.toString()));
		
		
		HttpResponse response = httpclient.execute(httpput);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(response.getEntity());
		
		if(json.equals("true")) {
			return true;
		}
		return false;
	}

	public boolean requestUpdatePositionInMenuPiatto(Integer idPiatto, Integer posizione) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPut httpput = new HttpPut(url + "/menu/piatto-update-posizione");
			httpput.setHeader("Authorization", loggedUser.getToken());
			httpput.setHeader("Content-type", "application/json");
		
			
			JSONObject requestparams = new JSONObject();
			requestparams.put("idElemento", idPiatto.toString());
			requestparams.put("posizione", posizione.toString());
			httpput.setEntity(new StringEntity(requestparams.toString()));
			
			HttpResponse response = httpclient.execute(httpput);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			System.out.print(json);
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean requestDeleteMenuSorting() {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpDelete httpdelete = new HttpDelete(url + "/menu/delete-ordine-menu-precedente/" + String.valueOf(loggedUser.getIdRistorante()));
			httpdelete.setHeader("Authorization", loggedUser.getToken());
		
			HttpResponse response = httpclient.execute(httpdelete);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			System.out.print(json);
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public Piatto getPiattoInfoFromServer(Integer idPiatto) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/menu/ristorante/piatto/" + idPiatto.toString());
			httpget.setHeader("Authorization", loggedUser.getToken());
		
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			
			JSONObject jsonObject = new JSONObject(json);
			String nomeSecondaLingua = "";
			String descrizioneSecondaLingua = "";
			
			if(!jsonObject.isNull("descrizioneSecondaLingua"))
				descrizioneSecondaLingua = jsonObject.getString("descrizioneSecondaLingua");
			
			if(!jsonObject.isNull("nomeSecondaLingua"))
				nomeSecondaLingua = jsonObject.getString("nomeSecondaLingua");
				
			if(jsonObject.has("idElemento") && jsonObject.has("idRistorante")) {
				return new Piatto(jsonObject.getInt("idElemento"), jsonObject.getInt("idRistorante"), jsonObject.getInt("idCategoria"), jsonObject.getString("nome"), jsonObject.getFloat("prezzo"), jsonObject.getString("descrizione"), jsonObject.getString("allergeni"), nomeSecondaLingua, descrizioneSecondaLingua, jsonObject.getInt("posizione"));
			}
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public boolean modificaPiatto(String nomePiatto, String descrizione, String costo, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua, String categoriaScelta, Integer idPiatto) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPut httpput = new HttpPut(url + "/menu/updatePlate/" + idPiatto.toString());
			httpput.setHeader("Authorization", loggedUser.getToken());
			httpput.setHeader("Content-type", "application/json;charset=UTF-8");
		
			JSONObject requestparams = new JSONObject();
			requestparams.put("idRistorante", String.valueOf(loggedUser.getIdRistorante()));
			requestparams.put("nomeCategoria", categoriaScelta);
			requestparams.put("nome", nomePiatto);
			requestparams.put("prezzo", costo);
			requestparams.put("descrizione", descrizione);
			requestparams.put("allergeni", allergeni);
			requestparams.put("nomeSecondaLingua", nomeSecondaLingua);
			requestparams.put("descrizioneSecondaLingua", descrizioneSecondaLingua);
			httpput.setEntity(new StringEntity(requestparams.toString(), "UTF-8"));
			
			HttpResponse response = httpclient.execute(httpput);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			System.out.println(json);
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean creaPiatto(String nomePiatto, String descrizione, String costo, String allergeni, String nomeSecondaLingua, String descrizioneSecondaLingua, String categoriaScelta) {		
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url + "/menu/newPlate");
			httppost.setHeader("Authorization", loggedUser.getToken());
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("idRistorante", String.valueOf(loggedUser.getIdRistorante())));
			params.add(new BasicNameValuePair("categoria", categoriaScelta));
			params.add(new BasicNameValuePair("nome", nomePiatto));
			params.add(new BasicNameValuePair("prezzo", costo));
			params.add(new BasicNameValuePair("descrizione", descrizione));
			params.add(new BasicNameValuePair("allergeni", allergeni));
			params.add(new BasicNameValuePair("nomeSecondaLingua", nomeSecondaLingua));
			params.add(new BasicNameValuePair("descrizioneSecondaLingua", descrizioneSecondaLingua));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			System.out.println(json);
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public boolean creaCategoria(String nomeCategoriaNuova) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url + "/menu/newCategoria");
			httppost.setHeader("Authorization", loggedUser.getToken());
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("nomeNuovaCategoria", nomeCategoriaNuova));
			params.add(new BasicNameValuePair("idRistorante", String.valueOf(loggedUser.getIdRistorante())));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			System.out.println(json);
			if(json.equals("true"))
				return true;
			else
				return false;
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.print("Errore nel parsing del JSON");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public JSONObject autocompletamentoProdotto(String nomeProdotto, Integer resultIndex) throws Exception{
		String nome = nomeProdotto, categoria = "Non trovata", descrizione = "Non trovata", allergeni = "";
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://it.openfoodfacts.org/cgi/search.pl?search_terms=" + nomeProdotto + "&&json=true");
		httpget.setHeader("Authorization", loggedUser.getToken());
		
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(response.getEntity());
		JSONObject jsonObject1 = new JSONObject(json);
		JSONArray nestedJsonA = jsonObject1.getJSONArray("products");
		JSONObject jsonObject = nestedJsonA.getJSONObject(resultIndex);
		JSONObject result = new JSONObject();
		
		if(jsonObject.has("categories")) {
			categoria = jsonObject.getString("categories");
			if(categoria.length() >= 28) {
				categoria = categoria.substring(0, 28);
				categoria = categoria.replaceAll("'", "");
				categoria = categoria.replaceAll("-", "");
				categoria = categoria.replaceAll(",", "");
//				if(!categoria.matches("[a-zA-Z]+")) {
//					categoria = "Non trovata";
//				}
			}else {
				categoria = "Non trovata";
			}

		}
		

		if (jsonObject.has("generic_name_it")) {
            nome = jsonObject.getString("generic_name_it");
            if (nome.equals(""))
                nome = nomeProdotto;
            nome = nome.replaceAll("'", "");
        }
		
		if(jsonObject.has("quantity") && jsonObject.has("brands") && jsonObject.has("countries") && jsonObject.has("packaging")) {
            descrizione = "Quantità: " + jsonObject.getString("quantity") + "\nBrands: " + jsonObject.getString("brands") + "\nPaesi di produzione: " + jsonObject.getString("countries") + "\nConfezionamento: " + jsonObject.getString("packaging");
            descrizione = descrizione.replaceAll("'", " ");
        }
		
		if(jsonObject.has("allergens")) {
			allergeni = jsonObject.getString("allergens");
		}
		
		result.put("categoria", categoria);
		result.put("nome", nome);
		result.put("descrizione", descrizione);
		result.put("allergeni", allergeni);
		
		return result;

	}

}
