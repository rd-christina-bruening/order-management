package com.greyscale.ordermanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.greyscale.ordermanagement.dto.OrderDto;
import com.greyscale.ordermanagement.dto.OrderInputDto;
import com.greyscale.ordermanagement.dto.OrderInputResultDto;
import com.greyscale.ordermanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@RestController()
@RequestMapping(OrderController.ORDERS_API_PATH)
public class OrderController {
    public static final String ORDERS_API_PATH = "/api/orders";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderInputResultDto> uploadImages(
            @RequestParam("orderData") String orderData,
            @RequestParam("files") MultipartFile[] files
    ) {
        LOGGER.info("Received files");
        if (Arrays.stream(files).anyMatch(file -> file.getSize() > (3 * 1024 * 1024))) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }

        OrderInputDto orderDto;

        try {
            orderDto = objectMapper.readValue(orderData, new TypeReference<OrderInputDto>() {
            });
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            orderService.processOrder(orderDto, files);
            OrderInputResultDto orderInputResultDto = new OrderInputResultDto(files.length);
            return new ResponseEntity<>(orderInputResultDto, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrderDtos() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @DeleteMapping("/{emailAddress}/{deliveryDate}")
    public ResponseEntity<OrderDto> deleteOrderDto(
            @PathVariable("emailAddress") String emailAddress,
            @PathVariable("deliveryDate") LocalDate deliveryDate
    ) {
        if (!orderService.doesOrderExist(emailAddress, deliveryDate)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.deleteOrder(emailAddress, deliveryDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO add Controller to download zip file
}
