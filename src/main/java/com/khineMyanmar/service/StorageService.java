package com.khineMyanmar.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService 
{
    String saveProfilePicture(MultipartFile file, String firstName, String lastName, long userId, String roleName);

    String saveShopProfilePicture(MultipartFile file,String shopName,long shopId);
}
