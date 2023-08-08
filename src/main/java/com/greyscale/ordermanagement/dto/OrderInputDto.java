package com.greyscale.ordermanagement.dto;

import java.time.LocalDate;

public record OrderInputDto(
        String customerEmailAddress,
        LocalDate deliveryDate,
        String customerName,
        String reference
) {}
