package com.khineMyanmar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.model.User;
import com.khineMyanmar.repository.IDeliveryRepository;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IUserRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private IUserRepository userRep;

	@Autowired
	private IDeliveryRepository deliRep;

	@Autowired
	private IShopOwnerRepository shopOwnRep;
	
	@Autowired
	private PasswordEncoder passEnd;
	
	@Autowired
	private IUserRoleRepository roleRep;

	@Autowired
    private StorageService storageService;
	
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
	            user.setProfilePic("/img/profiles/default-profile.jpg"); // Relative 
	        }
			return userRep.save(user);
		}else {
			return null;
		}
		
	}

	public String save(User user, String roleName) {
		System.err.println(user.getEmail()+" email");
		boolean userexist= userRep.findByEmail(user.getEmail()).isPresent();
		if(!userexist) {
			String pass = passEnd.encode(user.getPassWord());
			user.setPassWord(pass);
			System.out.println(pass+" password");
			Optional<Role> role = roleRep.findByRoleName(roleName);
			user.setRole(role.get());
			
			if (user.getProfilePic() == null || user.getProfilePic().isEmpty()) {
	            user.setProfilePic("/img/profiles/default-profile.jpg"); 
	        }

			if(roleName.equalsIgnoreCase("shopowner")) {
				ShopOwner shopOwner = new ShopOwner();
				BeanUtils.copyProperties(user, shopOwner);
				shopOwnRep.save(shopOwner);
				return "ShopOwner saved";
			}
			if(roleName.equalsIgnoreCase("delivery")) {
				Delivery delivery = new Delivery();
				BeanUtils.copyProperties(user, delivery);
				deliRep.save(delivery);
				return "Delivery saved";
			}

			userRep.save(user);
			return "User saved";
		}else {
			return "User already exist";
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

	public List<User> getAllUsers() {
		return userRep.findAll();
	}

	public Page<User> getUsers(Pageable pageable) {
        return userRep.findAll(pageable);
    }

	public User getUserByUserId(long id){
		return userRep.findById(id).get();
	}

	@Transactional
    public User updateUser(Long userId, Map<String, String> updates, MultipartFile profileImage) {
        Optional<User> optionalUser = userRep.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = optionalUser.get();

        // Update user details based on provided data
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName": user.setFirstName(value); break;
                case "lastName": user.setLastName(value); break;
                case "email": user.setEmail(value); break;
                case "phNo": user.setPhNo(value); break;
            }
        });

        // Handle profile image update
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = storageService.saveProfilePicture(profileImage, 
                user.getFirstName(), user.getLastName(), user.getUserId(),user.getRole().getRoleName());
            user.setProfilePic(imageUrl);
        }

        return userRep.save(user); 
    }
}
