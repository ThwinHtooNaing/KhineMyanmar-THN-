package com.khineMyanmar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.User;
import com.khineMyanmar.repository.IUserRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

@Service
public class UserService {
	@Autowired
	private IUserRepository userRep;
	
	@Autowired
	private PasswordEncoder passEnd;
	
	@Autowired
	private IUserRoleRepository roleRep;
	
	public User saveUser(User user) {
		System.err.println(user.getEmail()+" email");
		boolean userexist= userRep.findByEmail(user.getEmail()).isPresent();
		if(!userexist) {
			String pass = passEnd.encode(user.getPassWord());
			user.setPassWord(pass);
			System.out.println(pass+" password");
			Optional<Role> role = roleRep.findByRoleName("user");
			user.setRole(role.get());
			
			if (user.getProfilePic() == null || user.getProfilePic().isEmpty()) {
	            user.setProfilePic("/img/profiles/default-profile.jpg"); // Relative path for Thymeleaf
	        }
			
			return userRep.save(user);
		}else {
			return null;
		}
		
	}

	public User checkLogin(String em, String pass) {
		// TODO Auto-generated method stub
		User user =null;
		if( userRep.findByEmail(em).isPresent()) {
			user =userRep.findByEmail(em).get();
			if(passEnd.matches(pass,user.getPassWord())) {
				user=userRep.findByEmailAndPassWord(em,user.getPassWord()).get();
				return user;
			}
			else 
				return null;
			}
		else
			return  user;
	}
}
