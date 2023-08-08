package com.greyscale.ordermanagement.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @EmbeddedId
    private OrderId orderId;

    private String customerName;

    private String reference;

    public OrderId getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReference() {
        return reference;
    }

    public OrderEntity(OrderId orderId, String customerName, String reference) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.reference = reference;
    }


    public OrderEntity() {

    }
}
