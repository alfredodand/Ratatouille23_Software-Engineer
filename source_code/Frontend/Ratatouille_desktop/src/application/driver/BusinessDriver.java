package application.driver;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Main;
import application.controller.LoginController;
import application.model.Business;
import application.model.Utente;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;


public class BusinessDriver {
	public static Business business;
	private String url = Main.address;
	private Utente loggedUser = LoginController.loggedUser;
	
	public BusinessDriver() {
		
	}
	
	public Business requestBusinessToServer(){
		HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url + "/business/nomeAttivita?id_ristorante=" + loggedUser.getIdRistorante());
        httpget.setHeader("Authorization", loggedUser.getToken());
		try {
			HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(json);
            business = new Business(jsonObject.getInt("idRistorante"), jsonObject.getString("nome"), jsonObject.optString("numeroTelefono"), jsonObject.optString("indirizzo"), jsonObject.optString("nomeImmagine"), jsonObject.getInt("idProprietario"));
            return business;
        }catch (JSONException e) {
            System.out.print("Errore nel parsing del JSON");
        } catch (ClientProtocolException e) {
        	System.out.print("Errore nell'esecuzione del protocollo del JSON");
		} catch (IOException e) {
			System.out.print("Errore nel parsing del JSON");
		}
        return null;
	}
	
	public Business requestModifyBusinessToServer(String nome, String indirizzo, String numeroTelefono){
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPut httpput = new HttpPut(url + "/business");
			httpput.setHeader("Authorization", loggedUser.getToken());
			httpput.setHeader("Content-type", "application/json");
		
			
			JSONObject requestparams = new JSONObject();
			requestparams.put("idRistorante", String.valueOf(loggedUser.getIdRistorante()));
			requestparams.put("nome", nome);
			requestparams.put("indirizzo", indirizzo);
			requestparams.put("numeroTelefono", numeroTelefono);
			httpput.setEntity(new StringEntity(requestparams.toString()));

			HttpResponse response = httpclient.execute(httpput);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(json);
			business = new Business(jsonObject.getInt("idRistorante"), jsonObject.getString("nome"), jsonObject.getString("numeroTelefono"), jsonObject.getString("indirizzo"), jsonObject.getString("nomeImmagine"), jsonObject.getInt("idProprietario"));
            return business;
		}catch (Exception e) {
			System.out.print("Errore nella connessione");
		}
		return null;
	}
	
	public Image requestGetLogoToServer() {
		try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url + "/business/getImage?idUtente=" + loggedUser.getIdUtente() + "&idRistorante=" + loggedUser.getIdRistorante());
            httpget.setHeader("Authorization", loggedUser.getToken());

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(response.getEntity());
            if(!json.equals("")) {
            	JSONObject jsonObject = new JSONObject(json);
            	String encodedImage = jsonObject.optString("image");
                byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                return image;
            }
            return null;
         
		}catch (JSONException e) {
            System.out.print("Errore nel parsing del JSON");
        } catch (ClientProtocolException e) {
        	System.out.print("Errore nell'esecuzione del protocollo del JSON");
		} catch (IOException e) {
			System.out.print("Errore nel parsing del JSON");
        } catch (ParseException e) {
        	System.out.print("Errore nel parsing del JSON");
        }
        return null;
	}
	

	@SuppressWarnings("deprecation")
	public Business setLogoInDatabase(File image, String fileName) throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

	    HttpPost httppost = new HttpPost(url + "/business/image");
	    httppost.setHeader("Authorization", loggedUser.getToken());

	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(image, "image/jpg");
	    mpEntity.addPart("image", cbFile);
	    
	    mpEntity.addPart("fileName",new StringBody(fileName, Charset.forName("utf-8")));
	    mpEntity.addPart("idUtente",new StringBody(String.valueOf(loggedUser.getIdUtente()), Charset.forName("utf-8")));
	    mpEntity.addPart("idRistorante",new StringBody(String.valueOf(loggedUser.getIdRistorante()), Charset.forName("utf-8")));


	    httppost.setEntity(mpEntity);
	    System.out.println("executing request " + httppost.getRequestLine());
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity resEntity = response.getEntity();
	    
	    return null;
	}
}	