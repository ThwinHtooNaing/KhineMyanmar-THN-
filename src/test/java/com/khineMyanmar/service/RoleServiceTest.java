package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.repository.IUserRoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    
    @Mock
    private IUserRoleRepository roleRep;
    
    @InjectMocks
    private RoleService roleService;
    
    private List<Role> roleList;
    
    @BeforeEach
    void setUp() {
        Role role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("ADMIN");
        
        Role role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("USER");
        
        roleList = Arrays.asList(role1, role2);
    }
    
    @Test
    void testGetAllRoles() {
        when(roleRep.findAll()).thenReturn(roleList);
        List<Role> result = roleService.getAllRoles();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getRoleName());
        assertEquals("USER", result.get(1).getRoleName());
        
        verify(roleRep, times(1)).findAll();
    }
}
