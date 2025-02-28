package com.khineMyanmar.DTO;

import java.text.DecimalFormat;

import com.khineMyanmar.model.DeliveryItem;
public class DeliveryItemDTO {
    private Long deliveryId;
    private String orderId;
    private String amount;
    private String status;

    // Constructors, getters, and setters
    // Constructor to convert from DeliveryItem
    public DeliveryItemDTO(DeliveryItem deliveryItem) {
        this.deliveryId = deliveryItem.getDeliveryItemId(); // Example formatting
        this.orderId = "O00" + deliveryItem.getOrder().getOrderId(); // Example formatting
        this.amount = formatAmount(deliveryItem.getOrder().getAmount()); // Example formatting
        this.status = deliveryItem.getDeliveryStatus().toString(); // Example formatting
    }

     private String formatAmount(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " Ks";
    }

    // Getters and setters
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
