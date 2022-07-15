package com.rest.service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.service.UserService;
import com.rest.service.entitys.UserData;
import com.rest.service.storage.StorageService;

@RestController
//@CrossOrigin("*")
class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private final UserService service;

	@Autowired
	StorageService storageService;

	public UserController(UserService service, StorageService storageService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {

		this.service = service;
		this.storageService = storageService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping("/")
	public String home() {
		return "welcome to profile builder api";
	}

	@GetMapping("/users")
	CollectionModel<EntityModel<UserData>> all() {
		List<UserData> userData=service.findAll();
		for(UserData x: userData) {
			x.setPassword("it's safe don't worry just enjoy it");
		}
		List<EntityModel<UserData>> users = userData.stream()
				.map(user -> EntityModel.of(user,
						linkTo(methodOn(UserController.class).all()).withRel("users")))
				.collect(Collectors.toList());

		return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
	}

	

	@PostMapping("/profile/reset/{id}")
	ResponseEntity<?> resetPassword(@PathVariable("id") Long id, @RequestParam("password") String password) {

		UserData user = service.findById(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if ((user.getEmail()).equalsIgnoreCase(username)) {
			user.setPassword(bCryptPasswordEncoder.encode(password));
			service.save(user);
			return ResponseEntity.ok("Password Changed sucessfully");
		}
		return ResponseEntity.badRequest().build();
	}

	@GetMapping(value = "/photo", produces = MediaType.IMAGE_JPEG_VALUE)
	void one(HttpServletResponse response) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		String s = service.findByUsername(username).getFilepath();
		InputStream inputStream = new FileInputStream(s);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(inputStream, response.getOutputStream());
	}

	@GetMapping("/myprofile")
	EntityModel<UserData> profile(Authentication authentication) {

		authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserData userData = service.findByUsername(username);
		userData.setPassword("Don't worry it's safe");
		return EntityModel.of(userData);

	}
	
	@GetMapping("/profile/{id}")
	EntityModel<UserData> pro(@PathVariable("id") Long id,Authentication authentication) {
		
		UserData userData = service.findById(id);
		userData.setPassword("Don't worry it's safe");

		return EntityModel.of(userData);

	}

	@DeleteMapping("/deleteprofile")
	ResponseEntity<?> deleteEmployee(Authentication authentication) {

		String username = authentication.getName();
		UserData user = service.findByUsername(username);
		if (null !=user) {
			service.deleteById(user.getId());
			return ResponseEntity.ok("Profile deleted Succussefully");
		}

		else
			return ResponseEntity.badRequest().body("User Doesn't exist");
	}

}
