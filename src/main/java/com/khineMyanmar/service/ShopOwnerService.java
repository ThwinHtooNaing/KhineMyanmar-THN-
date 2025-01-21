package com.khineMyanmar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

@Service
public class ShopOwnerService {

    @Autowired
    private IShopOwnerRepository shopOwnerRep;

    @Autowired
	private PasswordEncoder passEnd;
	
	@Autowired
	private IUserRoleRepository roleRep;

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
}
