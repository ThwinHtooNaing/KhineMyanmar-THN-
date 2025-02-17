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

import jakarta.transaction.Transactional;

@Service
public class ShopService {
	@Autowired
	private IShopRepository shopRep;

	@Autowired
	private StorageService storageService;

	@Autowired
	private IShopOwnerRepository shopOwnerRepository;

	public List<Shop> allShop() {
		
		return shopRep.findAll();
	}

	public List<Shop> getFirst6Shops() {
        return shopRep.findTop6ByOrderByShopIdAsc();
    }

	@Transactional
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


	@Transactional
	public Shop updateShop(Shop existShop,Map<String,String> updates,MultipartFile shopProfileImage)
	{
		if(existShop == null){
			throw new RuntimeException("Shop not found");
		}
		updates.forEach((key, value) -> {
            switch (key) {
                case "shopName": existShop.setShopName(value); break;
                case "address": existShop.setAddress(value); break;
                case "contactNumber": existShop.setContactNumber(value); break;
            }
        });
		if (shopProfileImage != null && !shopProfileImage.isEmpty()) {
            String imageUrl = storageService.saveShopProfilePicture(shopProfileImage,existShop.getShopName(),existShop.getShopId());
            existShop.setShopImagePath(imageUrl);
        }
		return shopRep.save(existShop);
	}

	public List<Shop> getAllShops() {
		return shopRep.findAll();
	}

}
