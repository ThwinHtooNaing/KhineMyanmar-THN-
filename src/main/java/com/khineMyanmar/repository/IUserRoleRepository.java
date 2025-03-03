package com.khineMyanmar.repository;
import java.util.Optional;
import com.khineMyanmar.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRoleRepository extends JpaRepository<Role,Long>{
	Optional<Role> findByRoleName(String roleName);
}
