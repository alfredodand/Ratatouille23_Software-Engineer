package application.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Main;
import application.controller.AvvisiController;
import application.controller.LoginController;
import application.model.Avviso;
import application.model.Utente;

public class AvvisiDriver {
	private Utente loggedUser = LoginController.loggedUser;
	private String url = Main.address;
	public AvvisiDriver() { }
	
	public void requestAvvisiFromServer(AvvisiController avvisiController) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url + "/avvisi?id_ristorante=" + loggedUser.getIdRistorante());
			httpget.setHeader("Authorization", loggedUser.getToken());
			
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.has("idAvviso") && jsonObject.has("testo")) {
					Avviso a = new Avviso(avvisiController, jsonObject.getInt("idAvviso"), jsonObject.getInt("idUtente"), jsonObject.getInt("idRistorante"), jsonObject.getString("testo"), jsonObject.getString("dataOra"), jsonObject.getString("autore"));
				}
			}
		}catch (JSONException e) {
            System.out.print("Errore nel parsing del JSON");
        } catch (ClientProtocolException e) {
        	System.out.print("Errore nell'esecuzione del protocollo del JSON");
		} catch (IOException e) {
			System.out.print("Errore nel parsing del JSON");
		}
	}
	

	public void requestScriviAvvisiFromServer(AvvisiController avvisiController, String testo) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url + "/avviso/crea");
			httppost.setHeader("Authorization", loggedUser.getToken());
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("id_ristorante", String.valueOf(loggedUser.getIdRistorante())));
			params.add(new BasicNameValuePair("testo", testo));
			params.add(new BasicNameValuePair("idUtente", String.valueOf(loggedUser.getIdUtente())));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has("idAvviso") && jsonObject.has("testo")) {
				Avviso a = new Avviso(avvisiController, jsonObject.getInt("idAvviso"), jsonObject.getInt("idUtente"), jsonObject.getInt("idRistorante"), jsonObject.getString("testo"), jsonObject.getString("dataOra"), jsonObject.getString("autore"));
			}
		}catch (JSONException e) {
            System.out.print("Errore nel parsing del JSON");
        } catch (ClientProtocolException e) {
        	System.out.print("Errore nell'esecuzione del protocollo del JSON");
		} catch (IOException e) {
			System.out.print("Errore nel parsing del JSON");
		}
	}

	public boolean requestDeleteAvviso(Integer idAvviso) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpDelete httpdelete = new HttpDelete(url + "/avviso/cancella/" + idAvviso.toString());
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
            System.out.print("Errore nel parsing del JSON");
        } catch (ClientProtocolException e) {
        	System.out.print("Errore nell'esecuzione del protocollo del JSON");
		} catch (IOException e) {
			System.out.print("Errore nel parsing del JSON");
		}
		return false;
	}
}
