package com.rest.service.pre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rest.service.entitys.UserData;
import com.rest.service.repositry.UserDataRepo;
import com.rest.service.storage.StorageService;

@Configuration
public class PreRunner {
	@Autowired
	StorageService storageService;
	
	 private static final Logger log = LoggerFactory.getLogger(PreRunner.class);

	 @Autowired
	 PasswordEncoder passwordEncoder;
	  @Bean
	  CommandLineRunner initDatabase(UserDataRepo repository) {

		  
	    return args -> {
//	    	storageService.deleteAll();
	    	storageService.init();
	      log.info("Preloading " + repository.save(new UserData("Arun","arun@wipro.com",passwordEncoder.encode("pass"),new String[] {"c","java"},new String[] {"home","spring"},new String[] {"link1","link2"},"images/admin")));
//	      log.info("Preloading " + repository.save(new UserData("avtar","avtar@wipro.com",passwordEncoder.encode("pass"),new String[] {"c++","python"},new String[] {"home","boot"},new String[] {"link3","link5"},null)));
	    };
	  }
	public PreRunner(PasswordEncoder passwordEncoder,StorageService storageService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.storageService=storageService;
	}
}
