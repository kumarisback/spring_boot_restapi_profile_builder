package com.rest.service.pre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rest.service.UserService;
import com.rest.service.entitys.UserData;
import com.rest.service.repositry.UserDataRepo;
import com.rest.service.storage.StorageService;

@Configuration
public class PreRunner {
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;

//	private static final Logger log = LoggerFactory.getLogger(PreRunner.class);

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner initDatabase(UserDataRepo repository) {

		return args -> {
			storageService.init();
			
			if(userService.findByUsername("kumarisbeck@gmail.com")==null) { 
			 repository.save(new UserData("Arun Kumar", "kumarisbeck@gmail.com",
					passwordEncoder.encode("hardtoguess"), "7009740089",
					new String[] { "C", "C++", "Java", "Spring", "Spring Boot", "ReactJs", "MongoDb", "NodeJs" },
					new String[] { "Hotel Managment", "DevConnect", "Cart", "Playlist Maker" }, new String[] {
							"https://github.com/kumarisback", "https://in.linkedin.com/in/arun-kumar-49a155120" },
					"images/admin.jpg","Hi i'm a software developer and currently working on Spring boot"));
			}
		};
	}

	public PreRunner(PasswordEncoder passwordEncoder, StorageService storageService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.storageService = storageService;
	}
}
