package com.greyscale.ordermanagement.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class OrderId implements Serializable {
    private String customerEmailAddress;

    private LocalDate deliveryDate;

    public OrderId(String customerEmailAddress, LocalDate deliveryDate) {
        this.customerEmailAddress = customerEmailAddress;
        this.deliveryDate = deliveryDate;
    }

    public OrderId() {

    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String toString() {
        return this.customerEmailAddress + "-" + this.deliveryDate.toString();
    }
}
