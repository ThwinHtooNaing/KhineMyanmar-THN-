package com.khineMyanmar.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IShopRepository;

@Service
public class ShopService {
	@Autowired
	private IShopRepository shopRep;

	@Autowired
	private StorageService storageService;

	@Autowired
	private IShopOwnerRepository shopOwnerRepository;

	public List<Shop> allShop() {
		// TODO Auto-generated method stub
		return shopRep.findAll();
	}

	public Shop createShop(Map<String,String> updates,MultipartFile shopProfileImage,Long ownerId){

		Shop shop = new Shop();
        shop.setShopName(updates.get("shopName"));
        shop.setAddress(updates.get("address"));
        shop.setContactNumber(updates.get("contactNumber"));

		ShopOwner owner = shopOwnerRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found"));

		shop.setShopOwner(owner);
		if (shop.getShopImagePath() == null || shop.getShopImagePath().isEmpty()) {
			 // Relative 
			shop.setShopImagePath("/img/shopprofiles/default_shop_profile.png");
		}
		shop = shopRep.save(shop); 
		owner.setShop(shop);
		shopOwnerRepository.save(owner);

		if (shopProfileImage != null && !shopProfileImage.isEmpty()) {
			String imageUrl = storageService.saveShopProfilePicture(shopProfileImage,shop.getShopName(),shop.getShopId());
            shop.setShopImagePath(imageUrl);
        }
		return shopRep.save(shop);
	}

}
