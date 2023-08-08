package com.greyscale.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.greyscale.ordermanagement.AbstractServiceTest;
import com.greyscale.ordermanagement.dto.OrderInputDto;
import com.greyscale.ordermanagement.dto.OrderInputResultDto;
import com.greyscale.ordermanagement.model.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;

import static com.greyscale.ordermanagement.controller.OrderController.ORDERS_API_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerITTest extends AbstractServiceTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void happy_path_create_or_update_order_works() throws Exception {
        // GIVEN
        File file = new File("./src/test/resources/images/", "puppmass.jpg");
        BufferedImage image = ImageIO.read(file);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageAsByteArr = baos.toByteArray();

        OrderInputDto orderInputDto = new OrderInputDto(
                "test@mail.com",
                LocalDate.now().plusDays(1),
                "Laura Palmer",
                "Cherry Pie"
        );

        // WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(ORDERS_API_PATH)
                        .file("files", imageAsByteArr)
                        .param("orderData", objectMapper.writeValueAsString(orderInputDto)
                        )
                )

                // THEN
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        String json = result.getResponse().getContentAsString();
        OrderInputResultDto resultDto = new ObjectMapper().readValue(json, OrderInputResultDto.class);
        assertEquals(1, resultDto.numberOfUploadedImages());

        OrderId id = new OrderId(orderInputDto.customerEmailAddress(), orderInputDto.deliveryDate());
        assertTrue(orderRepository.findById(id).isPresent());
    }
}