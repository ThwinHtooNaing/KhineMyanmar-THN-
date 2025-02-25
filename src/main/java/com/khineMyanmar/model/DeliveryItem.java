package com.khineMyanmar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class DeliveryItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long deliveryItemId;
	
	@Enumerated(EnumType.STRING) // Store enum values as strings in the database
	private DeliveryStatus deliveryStatus;
	
	@OneToOne(mappedBy = "deliveryItem")
	@JsonIgnore
    private Order order;
	
	@ManyToOne
    @JoinColumn(name = "delivery_person_id", nullable = true) // Can be NULL for automated deliveries
	@JsonIgnore
    private Delivery deliveryPerson;

	public Long getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(Long deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Delivery getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setDeliveryPerson(Delivery deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	public DeliveryItem(DeliveryStatus deliveryStatus, Order order,Delivery deliveryPerson) {
		super();
		this.deliveryStatus = deliveryStatus;
		this.order = order;
		this.deliveryPerson = deliveryPerson;
	}

	public DeliveryItem() {
		super();
	}	
	
}
