package com.rest.service.entitys;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserData {

	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	private String name;
	private String email;
	private String password;
	private String[] skills;
	private String[] projects;
	private String[] links;
	private String filepath;
	
	
	
	
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public UserData() {}
	
	public UserData(String name, String email, String password, String[] skills, String[] projects,
			String[] links, String filepath) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.skills = skills;
		this.projects = projects;
		this.links = links;
		this.filepath = filepath;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getProjects() {
		return projects;
	}
	public void setProjects(String[] projects) {
		this.projects = projects;
	}
	public String[] getLinks() {
		return links;
	}
	public void setLinks(String[] links) {
		this.links = links;
	}
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserData other = (UserData) obj;
		return Objects.equals(email, other.email);
	}
	
	
}
