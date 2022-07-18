package com.rest.service.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.service.entitys.UserData;

@Repository
public interface UserDataRepo extends JpaRepository<UserData, Long> {

	UserData findByEmail(String username);

}
