package com.khineMyanmar.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private final String BASE_UPLOAD_DIR = "src/main/resources/static/img/profiles/";
    private final String BASE_UPLOAD_DIR_SHOP = "src/main/resources/static/img/shopprofiles/";
    private final String BASE_UPLOAD_DIR_PRODUCT = "src/main/resources/static/img/products/";


    @Override
    public String saveProfilePicture(MultipartFile file, String firstName, String lastName, long userId, String roleName) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or null");
        }

        try {
           
            String sanitizedName = (firstName + "_" + lastName + "_" + userId).toLowerCase().replaceAll("\\s+", "");
            String UPLOAD_DIR = BASE_UPLOAD_DIR + "/" + roleName;
            Path userDir = Paths.get(UPLOAD_DIR, sanitizedName);
            Files.createDirectories(userDir);
            
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = userDir.resolve(fileName);
            
            Files.write(filePath, file.getBytes());
            return "/img/profiles/"+roleName+"/" + sanitizedName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving profile picture", e);
        }
    }

    @Override
    public String saveShopProfilePicture(MultipartFile file, String shopName,long shopId){
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or null");
        }
        try {

            String sanitizedShopName = (shopName+"_"+shopId).toLowerCase().replaceAll("\\s+", "");
            String UPLOAD_DIR = BASE_UPLOAD_DIR_SHOP + sanitizedShopName;
    
            Path shopDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(shopDir);
    
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = shopDir.resolve(fileName);
            Files.write(filePath, file.getBytes()); // Save the file
    
            return "/img/shopprofiles/" + sanitizedShopName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving shop profile picture", e);
        }
    }

    @Override
    public String saveProductPicture(MultipartFile file,Shop shop,Product product){
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or null");
        }
        try{

            String sanitizedProductName = (product.getProductName()+"_"+product.getProductId()).toLowerCase().replaceAll("\\s+", "");
            String UPLOAD_DIR = BASE_UPLOAD_DIR_PRODUCT + shop.getShopId() + "/" + sanitizedProductName;
            Path productDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(productDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = productDir.resolve(fileName);
            Files.write(filePath, file.getBytes()); // Save the file

            return "/img/products/" + shop.getShopId() + "/" + sanitizedProductName + "/" + fileName; // Set the product image path
        }catch (IOException e) {
            throw new RuntimeException("Error saving product picture", e);
        }
        
    }   
}
