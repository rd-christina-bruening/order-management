package com.greyscale.ordermanagement.repository;

import com.greyscale.ordermanagement.model.OrderEntity;
import com.greyscale.ordermanagement.model.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, OrderId> {
}
