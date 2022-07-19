package com.rest.service.entitys;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})  
public class FileData {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
	private byte[] file;
	


	public FileData() {
	}

	public FileData(byte[] fileInbinary) {
		this.file=fileInbinary;
	}

	@Override
	public String toString() {
		return "FileData [id=" + id + ", file=" + Arrays.toString(file) + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
	
}
