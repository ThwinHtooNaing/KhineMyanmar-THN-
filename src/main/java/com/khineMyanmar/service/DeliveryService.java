package com.khineMyanmar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.repository.IDeliveryRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

@Service
public class DeliveryService {

    @Autowired
    private IDeliveryRepository deliveryRepository;

    @Autowired
    private PasswordEncoder passEnd;

    @Autowired
    private IUserRoleRepository RoleRepository;

    public String save(Delivery delivery){
        boolean deliveryexist= deliveryRepository.findByUserId(delivery.getUserId()).isPresent();
        if(!deliveryexist){
            String EncodedPass = passEnd.encode(delivery.getPassWord());
            delivery.setPassWord(EncodedPass);
            Optional<Role> role = RoleRepository.findByRoleName("delivery");
            delivery.setRole(role.get());
            if (delivery.getProfilePic() == null || delivery.getProfilePic().isEmpty()) {
	            delivery.setProfilePic("/img/profiles/default-profile.jpg"); // Relative 
	        }
            deliveryRepository.save(delivery);
            return "delivery saved";
        }else{
            return "delivery already exists";
        }
    }

    public List<Delivery> getAllDeliveriesByShopId(Shop shop){
        return deliveryRepository.findByShop(shop);
    }
    
}
