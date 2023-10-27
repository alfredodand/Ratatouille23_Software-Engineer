package com.ingsw.ratatouille.security;


import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.juli.logging.Log;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ingsw.DAOimplements.UserDAOimp;
import com.ingsw.ratatouille.LoggedUser;
import com.ingsw.ratatouille.RatatuilleApplication;
import com.ingsw.ratatouille.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomInterceptor implements HandlerInterceptor {
	private UserDAOimp userDao;
	private ArrayList<LoggedUser> loggedUsers = new ArrayList<LoggedUser>();
	ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	final private String[] unsafeEndpoint = new String[] {"/login", "/verify", "/signup-admin"};
	final private int TOKEN_EXPIRATION_DELAY = 10;
	@Autowired
	CustomInterceptor(UserDAOimp userDao){
		Runnable periodicTask = new Runnable() {
			public void run() {
				clearExpiredToken();
				logActualLoggedUser();
			}
		};
		executor.scheduleAtFixedRate(periodicTask, 0, 60, TimeUnit.SECONDS);
		this.userDao = userDao;
	}

	private void clearExpiredToken() {
		System.out.println("Clearing expired token...");
		Iterator it = loggedUsers.iterator();
		while(it.hasNext()){
			LoggedUser loggedUser = (LoggedUser) it.next();
			if(!isValidTokenWithoutExtendExpiration(loggedUser.getToken())){
				System.out.println("Utente scaduto: " + loggedUser.getEmail() + " " + loggedUser.getToken());
				it.remove();
			}
		}

	}

	private boolean isValidTokenWithoutExtendExpiration(String token) {
		for(LoggedUser u : loggedUsers) {
			if(u.getToken().equals(token)) {
				ZoneId z = ZoneId.of("Europe/Paris");
				ZonedDateTime zdt = ZonedDateTime.now(z);
				ZonedDateTime tk_timestamp = ZonedDateTime.parse(u.getTk_expiration_timestamp());
				if(tk_timestamp.compareTo(zdt) > 0){
					return true;
				}
			}
		}
		return false;
	}

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if(request.getHeader("Authorization") == null) {
    		if(!(isEndpointUnsafe(request))){
    			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    			throw new RestClientException("Richiesta rifiutata : Token non presente\n");
    		} else {
    			if(request.getRequestURI().equals("/login")) {
					setupLoggedUser(request);
				}
    		}
    	} else {
    		if(isValidToken(request.getHeader("Authorization"))) {
    			System.out.print("Richiesta accettata : Token valido " + request.getHeader("Authorization") + "\n");
    			if(request.getRequestURI().equals("/logout")) {
    				LoggedUser u = removeLoggedUser(Integer.valueOf(request.getParameter("idUtente")));
    			}
    		} else {
    			response.sendError(HttpServletResponse.SC_FORBIDDEN);
    			throw new RestClientException("Richiesta rifiutata : Token non valido\n");
    		}
    	}
		return true;
    }

	private void logActualLoggedUser() {
		System.out.println("Utenti attualmente loggati : ");
		for (LoggedUser u : loggedUsers) {
			System.out.println(u.getEmail() + " - " + u.getToken() + " - " + u.getTk_expiration_timestamp());
		}
		System.out.println("\n");
	}

	private void setupLoggedUser(HttpServletRequest request) {
		String email = request.getParameter("email");
		removeIfTokenIsNotExpiredAndUserIsTryingToLogin(email);
		String token = generateToken();
		String expirationTime = generateExpirationTime();
		LoggedUser u = userDao.login(email, request.getParameter("password"), token, expirationTime);
		u.setToken(token);
		u.setTk_expiration_timestamp(expirationTime);
		HttpSession session = request.getSession();
		session.setAttribute("attributeToPass", u);
		loggedUsers.add(u);
	}

	private String generateExpirationTime() {
		ZoneId z = ZoneId.of("Europe/Paris");
		ZonedDateTime zdt = ZonedDateTime.now(z);
		ZonedDateTime later = zdt.plusMinutes(TOKEN_EXPIRATION_DELAY);
		return later.toString();
	}


	private boolean isValidToken(String token) {
		for(LoggedUser u : loggedUsers) {
			if(u.getToken().equals(token)) {
    			ZoneId z = ZoneId.of("Europe/Paris");
    			ZonedDateTime zdt = ZonedDateTime.now(z);
    			ZonedDateTime tk_timestamp = ZonedDateTime.parse(u.getTk_expiration_timestamp());
    			if(tk_timestamp.compareTo(zdt) > 0){
	    			ZonedDateTime later = zdt.plusMinutes(TOKEN_EXPIRATION_DELAY);
	    			u.setTk_expiration_timestamp(later.toString());
					return true;
    			}
			}
		}
		return false;
	}

	@Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }
    
    private boolean isEndpointUnsafe(HttpServletRequest request){
		for(String endpoint : unsafeEndpoint){
			if(request.getRequestURI().equals(endpoint)){
				return true;
			}
		}
		return false;
	}

    private String generateToken() {
    	SecureRandom secureRandom = new SecureRandom(); //threadsafe
    	Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
    	for (LoggedUser u : loggedUsers) {
    		if(u.getToken().equals(token)) {
    			generateToken();
    		}
    	}
    	return token;
    }
    
    
    private LoggedUser removeLoggedUser(Integer idUtente) {
    	System.out.println(loggedUsers);
    	for(LoggedUser u : loggedUsers) {
    		if(u.getIdUtente() == idUtente) {
    			loggedUsers.remove(u);
    			return u;
    		}
    	}
    	return null;
    }

	private void removeIfTokenIsNotExpiredAndUserIsTryingToLogin(String email){
		loggedUsers.removeIf(loggedUser -> loggedUser.getEmail().equals(email));
	}

} 
