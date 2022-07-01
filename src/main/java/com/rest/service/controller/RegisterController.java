package com.rest.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.service.UserService;
import com.rest.service.entitys.UserData;
import com.rest.service.storage.StorageService;

@RestController
public class RegisterController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private final UserService service;

	@Autowired
	StorageService storageService;

	public RegisterController(UserService service, StorageService storageService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {

		this.service = service;
		this.storageService = storageService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping("/register")
	ResponseEntity<?> newUser(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("phone") String phone) {

		UserData newUser = service.findByUsername(email);
		if (newUser == null) {
			service.save(new UserData(name,email,bCryptPasswordEncoder.encode(password),phone)); 
			return ResponseEntity.ok("Register Successfully");
		} else {
			return ResponseEntity.badRequest().body("Email already registerd try using another mail or login ");
		}
	}
}
