package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.User;
import com.khineMyanmar.repository.IUserRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @InjectMocks
    private UserService userService;
    
    @Mock
    private IUserRepository userRep;
    
    @Mock
    private PasswordEncoder passEnd;
    
    @Mock
    private IUserRoleRepository roleRep;
    
    @Mock
    private StorageService storageService;
    
    private User user;
    private Role role;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setPassWord("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhNo("1234567890");
        
        role = new Role();
        role.setRoleName("user");
        user.setRole(role);
    }
    
    @Test
    void testSaveUser_Success() {
        when(userRep.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passEnd.encode(anyString())).thenReturn("encodedPassword");
        when(roleRep.findByRoleName("user")).thenReturn(Optional.of(role));
        when(userRep.save(any(User.class))).thenReturn(user);
        
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
    }
    
    @Test
    void testSaveUser_UserAlreadyExists() {
        when(userRep.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        
        User savedUser = userService.saveUser(user);
        assertNull(savedUser);
    }
    
    @Test
    void testCheckLogin_Success() {
        when(userRep.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passEnd.matches("password", user.getPassWord())).thenReturn(true);
        when(userRep.findByEmailAndPassWord(user.getEmail(), user.getPassWord())).thenReturn(Optional.of(user));
        
        User loggedInUser = userService.checkLogin("test@example.com", "password");
        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
    }
    
    @Test
    void testCheckLogin_WrongPassword() {
        when(userRep.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passEnd.matches("wrongpassword", user.getPassWord())).thenReturn(false);
        
        User loggedInUser = userService.checkLogin("test@example.com", "wrongpassword");
        assertNull(loggedInUser);
    }
    
    @Test
    void testGetAllUsers() {
        when(userRep.findAll()).thenReturn(List.of(user));
        
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }
    
    @Test
    void testGetUsers() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRep.findAll(any(Pageable.class))).thenReturn(page);
        
        Page<User> usersPage = userService.getUsers(Pageable.unpaged());
        assertEquals(1, usersPage.getTotalElements());
    }
    
    @Test
    void testUpdateUser_Success() {
        when(userRep.findById(1L)).thenReturn(Optional.of(user));
        when(userRep.save(any(User.class))).thenReturn(user);
        
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "Jane");
        updates.put("email", "jane@example.com");
        
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        when(storageService.saveProfilePicture(any(), any(), any(), anyLong(), any())).thenReturn("/img/profiles/user/test.jpg");
        
        User updatedUser = userService.updateUser(1L, updates, file);
        assertNotNull(updatedUser);
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("jane@example.com", updatedUser.getEmail());
    }
}
