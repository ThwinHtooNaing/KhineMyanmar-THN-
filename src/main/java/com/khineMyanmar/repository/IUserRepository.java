package com.khineMyanmar.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.khineMyanmar.model.User;
@Repository
public interface IUserRepository extends JpaRepository<User,Long>{

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndPassWord(String em, String passWord);

	@SuppressWarnings("null")
	Page<User> findAll(Pageable pageable);
}
