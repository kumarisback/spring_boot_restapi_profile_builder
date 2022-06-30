package com.rest.service.repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.service.entitys.UserData;

public interface UserDataRepo  extends JpaRepository<UserData, Long>{

	UserData findByEmail(String username);
	

}
