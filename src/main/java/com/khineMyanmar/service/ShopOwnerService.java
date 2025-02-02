package com.khineMyanmar.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

import jakarta.transaction.Transactional;

@Service
public class ShopOwnerService {

    @Autowired
    private IShopOwnerRepository shopOwnerRep;

    @Autowired
	private PasswordEncoder passEnd;
	
	@Autowired
	private IUserRoleRepository roleRep;

	@Autowired
	private StorageService storageService;

    public ShopOwner saveUser(ShopOwner owner) {
		
		boolean ownerexist = shopOwnerRep.findByEmail(owner.getEmail()).isPresent();
		if(!ownerexist) {
			String pass = passEnd.encode(owner.getPassWord());
			owner.setPassWord(pass);
			System.out.println(pass+" password");
			Optional<Role> role = roleRep.findByRoleName("shopowner");
			owner.setRole(role.get());
			
			if (owner.getProfilePic() == null || owner.getProfilePic().isEmpty()) {
	            owner.setProfilePic("/img/profiles/default-profile.jpg"); 
	        }
			
			return shopOwnerRep.save(owner);
		}else {
			return null;
		}
		
	}

	public ShopOwner getUserByUserId(long id){
		return shopOwnerRep.findById(id).get();
	}

	@Transactional
    public ShopOwner updateUser(Long userId, Map<String, String> updates, MultipartFile profileImage) {
        Optional<ShopOwner> optionalUser = shopOwnerRep.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        ShopOwner user = optionalUser.get();

        // Update user details based on provided data
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName": user.setFirstName(value); break;
                case "lastName": user.setLastName(value); break;
                case "email": user.setEmail(value); break;
                case "phNo": user.setPhNo(value); break;
            }
        });

        
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = storageService.saveProfilePicture(profileImage, 
                user.getFirstName(), user.getLastName(), user.getUserId(),user.getRole().getRoleName());
            user.setProfilePic(imageUrl);
        }

        return shopOwnerRep.save(user); 
    }


}
