package com.rest.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rest.service.entitys.MyUserPrincipal;
import com.rest.service.entitys.UserData;
import com.rest.service.exception.UserNotFoundException;
import com.rest.service.repositry.UserDataRepo;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private final UserDataRepo repository;

	public UserService(UserDataRepo repository) {
		this.repository = repository;
	}

	public List<UserData> findAll() {
		List<UserData> userDatas = repository.findAll();
		return userDatas;
	}

	public UserData save(UserData newUser) throws UsernameNotFoundException {
		UserData userData = repository.save(newUser);
		return userData;
	}

	public UserData findById(Long id) throws UsernameNotFoundException {
		Optional<UserData> userData = repository.findById(id);
		return userData.get();
	}

	public UserData findByIdFullData(Long id) {
		Optional<UserData> userData = repository.findById(id);
		UserData userData1 = null;
		if (userData != null) {
			userData1 = userData.get();
		}
		return userData1;
	}

	public void deleteById(Long id) {

		repository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserData userData = repository.findByEmail(username);
		if (userData == null)
			throw new UserNotFoundException(username);
		return new MyUserPrincipal(userData);

	}

	public UserData findByUsername(String username) throws UsernameNotFoundException {
		UserData userData = repository.findByEmail(username);
		return userData;
	}

}
