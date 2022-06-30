package com.rest.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rest.service.entitys.MyUserPrincipal;
import com.rest.service.entitys.UserData;
import com.rest.service.repositry.UserDataRepo;


@Service
@Transactional
public class UserService implements UserDetailsService {

	private final UserDataRepo repository;

	public UserService(UserDataRepo repository) {
		this.repository = repository;
	}

	public List<UserData> findAll() {
		List<UserData> userDatas=repository.findAll();
		for(UserData u: userDatas) {
			u.setPassword("your password is safe");
		}
		return userDatas;
	}

	public UserData save(UserData newUser) {
		UserData userData=repository.save(newUser);
		userData.setPassword("your password is safe");
		return  userData;
	}

	public UserData findById(Long id) {
		Optional<UserData> userData=repository.findById(id);
		UserData userData1=null;
		if(userData!=null) {
			userData1=userData.get();
			userData1.setPassword("your passwoord is safe");
		}
		return userData1;
	}

	public void deleteById(Long id) {
	
		repository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserData userData=repository.findByEmail(username);
		return  new MyUserPrincipal(userData);
			
	}

	public String findByUsername(String  username) {
		UserData userData=repository.findByEmail(username);
		return userData.getFilepath();
	}

	
}
