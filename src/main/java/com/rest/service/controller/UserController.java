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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rest.service.UserService;
import com.rest.service.entitys.UserData;
import com.rest.service.exception.UserNotFoundException;
import com.rest.service.storage.StorageService;

@RestController
//@RequestMapping("/users")
class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private final UserService service;
	
	@Autowired
	StorageService storageService;

	public UserController(UserService service,StorageService storageService) {
	
	this.service = service;
	this.storageService= storageService;
	}
	
	
	@GetMapping("/")
	public String home() {
		return "hi";
	}
	
	
@GetMapping("/users")
CollectionModel<EntityModel<UserData>> all() {
	List<EntityModel<UserData>> users = service.findAll().stream()
		      .map(user -> EntityModel.of(user,
			          linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
			          linkTo(methodOn(UserController.class).all()).withRel("users")))
			      .collect(Collectors.toList());

			  return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
}

//@PostMapping("/users")
//UserData newEmployee(@RequestParam("file") MultipartFile file,@RequestBody UserData newUser) {
//	newUser.setFilepath(storageService.store(file));
//	newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
//	
//  return service.save(newUser);
//}

@PostMapping("/users")
UserData newUser(@RequestParam("file") MultipartFile file,@RequestParam("name") String name,
		@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("links") String[] links,
		@RequestParam("projects") String[] projects,@RequestParam("skills") String[] skills) {
	UserData newUser=new UserData();
	newUser.setName(name);
	newUser.setFilepath(storageService.store(file));
	newUser.setEmail(email);
	newUser.setLinks(links);
	newUser.setProjects(projects);
	newUser.setSkills(skills);
	newUser.setPassword(bCryptPasswordEncoder.encode(password));
	
  return service.save(newUser);
}


@GetMapping(value="/photo",produces =MediaType.IMAGE_JPEG_VALUE)
void one(HttpServletResponse response) throws IOException {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	 String username = authentication.getName();
	String s= service.findByUsername(username);
	InputStream  inputStream=new FileInputStream(s);
	System.out.println(inputStream);
	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	StreamUtils.copy(inputStream, response.getOutputStream());
}

@GetMapping("/users/{id}")
EntityModel<UserData> one(@PathVariable Long id) {
  
  UserData userData= service.findById(id);


return EntityModel.of(userData, //
	      linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
	      linkTo(methodOn(UserController.class).all()).withRel("users"));
	
}

@DeleteMapping("/users/{id}")
ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

	service.deleteById(id);

  return ResponseEntity.noContent().build();
}


@PutMapping("/users/{id}")
EntityModel<UserData> updateUsers(@RequestBody UserData user, @PathVariable Long id) {

	UserData userData=service.findById(id); 
	user.setId(id);
    if(userData!=null) {
       
    	service.save(user);
    }
    else  new UserNotFoundException(id);


  return EntityModel.of(userData, 
	      linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
	      linkTo(methodOn(UserController.class).updateUsers(user,id)).withRel("users"));
}


}

