package com.khineMyanmar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.repository.IDeliveryRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

import jakarta.transaction.Transactional;

@Service
public class DeliveryService {

    @Autowired
    private IDeliveryRepository deliveryRepository;

    @Autowired
    private PasswordEncoder passEnd;

    @Autowired
    private IUserRoleRepository RoleRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StorageService storageService;

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

    public String save(Delivery delivery,Long shopId){
        boolean deliveryexist= deliveryRepository.findByUserId(delivery.getUserId()).isPresent();
        if(!deliveryexist){
            String EncodedPass = passEnd.encode(delivery.getPassWord());
            delivery.setPassWord(EncodedPass);
            Optional<Role> role = RoleRepository.findByRoleName("delivery");
            delivery.setRole(role.get());
            if (delivery.getProfilePic() == null || delivery.getProfilePic().isEmpty()) {
	            delivery.setProfilePic("/img/profiles/default-profile.jpg"); // Relative 
	        }
            delivery = deliveryRepository.save(delivery);
            Shop shop = shopService.getShopById(shopId);
            delivery.setShop(shop);
            deliveryRepository.save(delivery);
            return "delivery saved";
        }else{
            return "delivery already exists";
        }
    }

    public List<Delivery> getAllDeliveriesByShopId(Shop shop){
        return deliveryRepository.findByShop(shop);
    }

    public Delivery getUserByUserId(Long userId){
        return deliveryRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public Delivery updateUser(Long userId, Map<String, String> updates, MultipartFile profileImage) {
        Optional<Delivery> optionalUser = deliveryRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        Delivery user = optionalUser.get();

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

        return deliveryRepository.save(user); 
    }


    
}
