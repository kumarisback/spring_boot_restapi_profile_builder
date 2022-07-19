package com.rest.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.service.entitys.FileData;
import com.rest.service.repositry.FileRepo;

@Service
@Transactional
public class FileService {
	
	@Autowired
	FileRepo fileRepo;
	
	
	FileData getfile(Long id) {
		return fileRepo.findById(id).get();
		
	}
	
	FileData save(FileData file) {
		FileData f=fileRepo.save(file); 
		return f;
	}
}
