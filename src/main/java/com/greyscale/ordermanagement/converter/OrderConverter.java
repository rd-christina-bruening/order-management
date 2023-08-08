package com.greyscale.ordermanagement.converter;

import com.greyscale.ordermanagement.dto.OrderDto;
import com.greyscale.ordermanagement.dto.OrderInputDto;
import com.greyscale.ordermanagement.model.OrderEntity;
import com.greyscale.ordermanagement.model.OrderId;

public class OrderConverter {

    public static OrderEntity convertToEntity(OrderInputDto orderInputDto, int numberOfFiles) {
        OrderId orderId = new OrderId(
                orderInputDto.customerEmailAddress(),
                orderInputDto.deliveryDate()
        );

        return new OrderEntity(
                orderId,
                orderInputDto.customerName(),
                orderInputDto.reference(),
                numberOfFiles
        );
    }

    public static OrderDto convertToDto(OrderEntity orderEntity) {
        return new OrderDto(
                orderEntity.getOrderId().getCustomerEmailAddress(),
                orderEntity.getOrderId().getDeliveryDate(),
                orderEntity.getCustomerName(),
                orderEntity.getReference(),
                orderEntity.getNumberOfFilesSaved()
        );
    }
}
