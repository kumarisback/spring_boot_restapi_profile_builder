package com.rest.service.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rest.service.UserService;
import com.rest.service.entitys.FileData;
import com.rest.service.entitys.UserData;
import com.rest.service.repositry.FileRepo;
import com.rest.service.storage.StorageService;

@RestController
//@CrossOrigin
public class PutController {

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private final UserService service;
	
//	@Autowired
//	private final FileRepo fileRepo;

	@Autowired
	StorageService storageService;

	public PutController(UserService service, StorageService storageService,
			BCryptPasswordEncoder bCryptPasswordEncoder,FileRepo fileRepo) {

		this.service = service;
		this.storageService = storageService;
	}
	@PutMapping("/create/{id}")
	ResponseEntity<?> CreateProfile(@PathVariable Long id, @RequestParam(required = false) MultipartFile file,
			@RequestParam("socialLinks") String[] links, @RequestParam("projects") String[] projects,
			@RequestParam("skills") String[] skills,@RequestParam("about") String about) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		UserData user1 = service.findByUsername(username);
		Long tempId = user1 != null ? user1.getId() : null;
		UserData user = service.findByIdFullData(id);
		FileData filedb=null;
		if (user != null && tempId == id) {
			if(file!=null) {	
				try {
					byte[] fileInbinary=file.getBytes();
					filedb=new FileData(fileInbinary);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				user.setFilepath(storageService.store(file));
			}
			if(filedb!=null) {
				user.setFiledata(filedb);
			}
			user.setSocialLinks(links);
			user.setProjects(projects);
			user.setAbout(about);
			user.setSkills(skills);
			service.save(user);
			System.out.println(user.toString());
			return ResponseEntity.ok("Profile Created Successfully");

		}

		else {
			return ResponseEntity.badRequest().body("ho");
		}
	}
	
	
//	ResponseEntity<?> CreateProfiles(@PathVariable Long id,@RequestBody Map<String, String> input) {
//		System.out.println("hi");
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		
//		String username = authentication.getName();
//		for(String s:input.keySet()) {
//			System.out.println(input.get(s));
//		}
//		return null;
//		
//	}
	

//	@PutMapping("/profileupdate/{id}")
//	ResponseEntity<?> UpdateProfile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
//			@RequestParam("links") String[] links, @RequestParam("projects") String[] projects,
//			@RequestParam("skills") String[] skills,@RequestParam("phone") String phone ) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String username = authentication.getName();
//		UserData user1 = service.findByUsername(username);
//		Long tempId = user1 != null ? user1.getId() : null;
//		UserData user = service.findByIdFullData(id);
//		if (user != null && tempId == id) {
//			System.out.println(file.isEmpty());
//			if(!file.isEmpty()) {
//				user.setFilepath(storageService.store(file));
//			}
//			System.out.println("===");
//			user.setMobileno(phone);
//			user.setLinks(links);
//			user.setProjects(projects);
//			user.setSkills(skills);
//			service.save(user);
//			return ResponseEntity.ok("Profile Updated Successfully");
//
//		}
//
//		else {
//			return ResponseEntity.badRequest().build();
//		}
//	}
	
//			}
//			System.out.println("===");
//			user.setMobileno(phone);
//			user.setLinks(links);
//			user.setProjects(projects);
//			user.setSkills(skills);
//			service.save(user);
//			return ResponseEntity.ok("Profile Updated Successfully");
//
//		}
//
//		else {
//			return ResponseEntity.badRequest().build();
//		}
//	}
	
	
	
	
	
	
	
	
}
