package com.khineMyanmar.DTO;

public class TopCustomerDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String profilePic;
    private Double totalAmount;

    public TopCustomerDTO(Long userId, String firstName, String lastName, String profilePic, Double totalAmount) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
        this.totalAmount = totalAmount;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getProfilePic() { return profilePic; }
    public Double getTotalAmount() { return totalAmount; }
}

