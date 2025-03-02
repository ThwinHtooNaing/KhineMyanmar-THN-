package com.khineMyanmar.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Role;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IUserRoleRepository;

class ShopOwnerServiceTest {

    @InjectMocks
    private ShopOwnerService shopOwnerService;

    @Mock
    private IShopOwnerRepository shopOwnerRep;

    @Mock
    private PasswordEncoder passEnd;

    @Mock
    private IUserRoleRepository roleRep;

    @Mock
    private StorageService storageService;

    @Mock
    private MultipartFile profileImage;

    private ShopOwner owner;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize common objects
        role = new Role();
        role.setRoleName("shopowner");

        owner = new ShopOwner();
        owner.setUserId(1L);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setEmail("john.doe@example.com");
        owner.setPassWord("password123");
        owner.setRole(role);
    }

    @Test
    void testSaveUserWhenUserDoesNotExist() {
        when(shopOwnerRep.findByEmail(owner.getEmail())).thenReturn(Optional.empty());
        when(passEnd.encode(owner.getPassWord())).thenReturn("encodedPassword");
        when(roleRep.findByRoleName("shopowner")).thenReturn(Optional.of(role));
        when(shopOwnerRep.save(owner)).thenReturn(owner);

        ShopOwner savedOwner = shopOwnerService.saveUser(owner);

        assertNotNull(savedOwner);
        assertEquals("encodedPassword", savedOwner.getPassWord());
        assertEquals("/img/profiles/default-profile.jpg", savedOwner.getProfilePic());
    }

    @Test
    void testSaveUserWhenUserAlreadyExists() {
        when(shopOwnerRep.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));

        ShopOwner savedOwner = shopOwnerService.saveUser(owner);

        assertNull(savedOwner);
    }

    @Test
    void testGetUserByUserId() {
        when(shopOwnerRep.findById(owner.getUserId())).thenReturn(Optional.of(owner));

        ShopOwner fetchedOwner = shopOwnerService.getUserByUserId(owner.getUserId());

        assertNotNull(fetchedOwner);
        assertEquals(owner.getUserId(), fetchedOwner.getUserId());
    }

    @Test
    void testUpdateUser() {
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "Jane");
        updates.put("lastName", "Smith");
        updates.put("email", "jane.smith@example.com");
        updates.put("phNo", "1234567890");

        when(shopOwnerRep.findById(owner.getUserId())).thenReturn(Optional.of(owner));
        when(storageService.saveProfilePicture(profileImage, "Jane", "Smith", owner.getUserId(), "shopowner"))
                .thenReturn("/img/profiles/jane_smith.jpg");

        ShopOwner updatedOwner = shopOwnerService.updateUser(owner.getUserId(), updates, profileImage);

        assertEquals("Jane", updatedOwner.getFirstName());
        assertEquals("Smith", updatedOwner.getLastName());
        assertEquals("jane.smith@example.com", updatedOwner.getEmail());
        assertEquals("1234567890", updatedOwner.getPhNo());
        assertEquals("/img/profiles/jane_smith.jpg", updatedOwner.getProfilePic());
    }

    @Test
    void testUpdateUserWhenUserNotFound() {
        when(shopOwnerRep.findById(owner.getUserId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopOwnerService.updateUser(owner.getUserId(), new HashMap<>(), profileImage);
        });

        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void testUpdateUserWithoutProfileImage() {
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "John");
        updates.put("lastName", "Doe");

        when(shopOwnerRep.findById(owner.getUserId())).thenReturn(Optional.of(owner));

        ShopOwner updatedOwner = shopOwnerService.updateUser(owner.getUserId(), updates, null);

        assertEquals("John", updatedOwner.getFirstName());
        assertEquals("Doe", updatedOwner.getLastName());
        assertEquals("/img/profiles/default-profile.jpg", updatedOwner.getProfilePic());
    }
}
