package com.khineMyanmar.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class User {
	
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	
	@Column(nullable = true)
	private String profilePic;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(nullable=false)
	private String phNo;
	
	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String passWord;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Order> orders;

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhNo() {
		return phNo;
	}

	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}	

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public User(String profilePic, String firstName, String lastName, String phNo, String email, String passWord,
			Role role) {
		super();
		this.profilePic = profilePic;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phNo = phNo;
		this.email = email;
		this.passWord = passWord;
		this.role = role;
	}
	
	public User(String profilePic, String firstName, String lastName, String phNo, String email, String passWord,
			Role role, List<Order> orders) {
		super();
		this.profilePic = profilePic;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phNo = phNo;
		this.email = email;
		this.passWord = passWord;
		this.role = role;
		this.orders = orders;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}
	public User() {
		super();
	}
		
}
