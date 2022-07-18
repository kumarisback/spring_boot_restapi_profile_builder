package com.rest.service.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.service.entitys.FileData;


@Repository
public interface FileRepo extends JpaRepository<FileData, Long> {

}
