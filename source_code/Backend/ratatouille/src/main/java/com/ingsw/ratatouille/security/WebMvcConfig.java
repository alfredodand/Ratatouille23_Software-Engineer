package com.ingsw.ratatouille.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ingsw.DAOimplements.UserDAOimp;
import com.ingsw.ratatouille.User;

@EnableWebMvc
@Configuration
public class 	WebMvcConfig implements WebMvcConfigurer {
	UserDAOimp userDao;
	
	@Autowired
	WebMvcConfig(UserDAOimp userDao){
		this.userDao = userDao;
	}
	
	@Override  
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new CustomInterceptor(userDao)).addPathPatterns("/**");
	}
	
}