package com.khineMyanmar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long roleId;
	
	@Column(nullable=false)
	private String roleName;
	
	@OneToMany(mappedBy = "role")
	@JsonIgnore 
	private Set<User> users;

	public Role(String roleName, Set<User> users) {
		super();
		this.roleName = roleName;
		this.users = users;
	}

	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public Role() {
		super();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
		
}
