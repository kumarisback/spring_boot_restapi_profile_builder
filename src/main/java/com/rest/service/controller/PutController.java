package com.rest.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rest.service.UserService;
import com.rest.service.entitys.UserData;
import com.rest.service.storage.StorageService;

@RestController
public class PutController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private final UserService service;

	@Autowired
	StorageService storageService;

	public PutController(UserService service, StorageService storageService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {

		this.service = service;
		this.storageService = storageService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	@PutMapping("/profile/{id}")
	ResponseEntity<?> CreateProfile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
			@RequestParam("links") String[] links, @RequestParam("projects") String[] projects,
			@RequestParam("skills") String[] skills) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserData user1 = service.findByUsername(username);
		Long tempId = user1 != null ? user1.getId() : null;
		UserData user = service.findByIdFullData(id);
		if (user != null && tempId == id) {
			user.setFilepath(storageService.store(file));
			user.setLinks(links);
			user.setProjects(projects);
			user.setSkills(skills);
			service.save(user);
			return ResponseEntity.ok("Profile Created Successfully");

		}

		else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	

	@PutMapping("/profileupdate/{id}")
	ResponseEntity<?> UpdateProfile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
			@RequestParam("links") String[] links, @RequestParam("projects") String[] projects,
			@RequestParam("skills") String[] skills,@RequestParam("phone") String phone ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserData user1 = service.findByUsername(username);
		Long tempId = user1 != null ? user1.getId() : null;
		UserData user = service.findByIdFullData(id);
		if (user != null && tempId == id) {
			System.out.println(file.isEmpty());
			if(!file.isEmpty()) {
				user.setFilepath(storageService.store(file));
			}
			System.out.println("===");
			user.setMobileno(phone);
			user.setLinks(links);
			user.setProjects(projects);
			user.setSkills(skills);
			service.save(user);
			return ResponseEntity.ok("Profile Updated Successfully");

		}

		else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	
	
	
	
	
	
	
}
