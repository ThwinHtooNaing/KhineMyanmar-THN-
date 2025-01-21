package com.khineMyanmar.repository;

import com.khineMyanmar.model.ShopOwner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShopOwnerRepository extends JpaRepository<ShopOwner,Long> {
    
    Optional<ShopOwner> findByEmail(String email);

	Optional<ShopOwner> findByEmailAndPassWord(String em, String passWord);
}
