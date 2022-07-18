package com.rest.service.entitys;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_data")
public class UserData {

	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	private String name;
	private String email;
	private String password;
	private String mobileno;
	private String[] skills;
	private String[] projects;
	private String[] socialLinks;
	private String filepath;
	private String about;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
	private FileData filedata;

	public FileData getFiledata() {
		return filedata;
	}

	public void setFiledata(FileData filedata) {
		this.filedata = filedata;
	}

	public UserData() {
	}

	public UserData(String name, String email, String password, String mobileno) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.mobileno = mobileno;
	}

	public UserData(String name, String email, String password, String mobileno, String[] skills, String[] projects,
			String[] socialLinks, String filepath,String about) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.mobileno = mobileno;
		this.skills = skills;
		this.projects = projects;
		this.socialLinks = socialLinks;
		this.filepath = filepath;
		this.about=about;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
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

	@Override
	public String toString() {
		return "UserData [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", mobileno="
				+ mobileno + ", skills=" + Arrays.toString(skills) + ", projects=" + Arrays.toString(projects)
				+ ", socialLinks=" + Arrays.toString(socialLinks) + ", filepath=" + filepath + ", about=" + about
				+ ", filedata=" + filedata + "]";
	}

	public String[] getSocialLinks() {
		return socialLinks;
	}

	public void setSocialLinks(String[] socialLinks) {
		this.socialLinks = socialLinks;
	}

}
