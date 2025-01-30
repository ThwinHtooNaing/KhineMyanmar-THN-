package com.khineMyanmar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.repository.IUserRoleRepository;

@Service
public class RoleService {

    @Autowired
    private IUserRoleRepository roleRep;

    public List<Role> getAllRoles() {
        return roleRep.findAll();
    }
    
    
}
