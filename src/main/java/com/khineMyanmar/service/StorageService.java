package com.khineMyanmar.service;

import org.springframework.web.multipart.MultipartFile;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.Product;

public interface StorageService 
{
    String saveProfilePicture(MultipartFile file, String firstName, String lastName, long userId, String roleName);

    String saveShopProfilePicture(MultipartFile file,String shopName,long shopId);

    String saveProductPicture(MultipartFile file,Shop shop,Product product);
}
