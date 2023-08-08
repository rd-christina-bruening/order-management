package com.greyscale.ordermanagement.dto;

import java.time.LocalDate;

public record OrderDto(
        String customerEmailAddress,
        LocalDate deliveryDate,
        String customerName,
        String reference
) {}
