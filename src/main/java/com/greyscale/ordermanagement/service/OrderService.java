package com.greyscale.ordermanagement.service;

import com.greyscale.ordermanagement.controller.OrderController;
import com.greyscale.ordermanagement.converter.OrderConverter;
import com.greyscale.ordermanagement.dto.OrderDto;
import com.greyscale.ordermanagement.dto.OrderInputDto;
import com.greyscale.ordermanagement.dto.OrderInputResultDto;
import com.greyscale.ordermanagement.model.OrderEntity;
import com.greyscale.ordermanagement.model.OrderId;
import com.greyscale.ordermanagement.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final GreyscaleImageService greyscaleImageService;
    private final OrderRepository orderRepository;

    public OrderService(GreyscaleImageService greyscaleImageConverter, OrderRepository orderRepository) {
        this.greyscaleImageService = greyscaleImageConverter;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<OrderInputResultDto> processOrder(OrderInputDto orderDto, MultipartFile[] files) throws IOException {
        processImages(files, new OrderId(orderDto.customerEmailAddress(), orderDto.deliveryDate()));
        saveOrderDto(orderDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveOrderDto(OrderInputDto orderDto) {
        OrderEntity orderEntity = OrderConverter.convertToEntity(orderDto);
        orderRepository.save(orderEntity);
    }

    private void processImages(MultipartFile[] files, OrderId orderId) {
        AtomicInteger positionOfFile = new AtomicInteger(1);
        Arrays.stream(files).forEachOrdered(file -> {
            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
                greyscaleImageService.convertAndStoreImage(image, orderId, "image-" + positionOfFile);
                positionOfFile.getAndIncrement();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public List<OrderDto> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderEntities.stream().map(OrderConverter::convertToDto).toList();
    }

    public void deleteOrder(String emailAddress, LocalDate deliveryDate) {
        orderRepository.deleteById(new OrderId(emailAddress, deliveryDate));
    }

    public boolean doesOrderExist(String emailAddress, LocalDate deliveryDate) {
        return orderRepository.existsById(new OrderId(emailAddress, deliveryDate));
    }
}
