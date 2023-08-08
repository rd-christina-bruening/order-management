package com.greyscale.ordermanagement.service;

import com.greyscale.ordermanagement.model.OrderId;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//TODO apply https://www.baeldung.com/introduction-to-spring-batch
// also see https://github.com/joshrotenberg/spring-batch-image-processing/tree/master
@Service
public class GreyscaleImageService {

    private static final String IMAGE_DIRECTORY = "./src/main/java/com/greyscale/ordermanagement/images/";

    public void convertAndStoreImage(BufferedImage image, OrderId orderId, String name) {
        BufferedImage greyscaleImage = convertToGreyscale(image);

        new File(IMAGE_DIRECTORY + "/" + orderId.toString()).mkdirs();

        writeFile(greyscaleImage, IMAGE_DIRECTORY + "/" + orderId, name);
    }

    private static BufferedImage convertToGreyscale(BufferedImage originalImage) {
        BufferedImage greyscaleImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = greyscaleImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        return greyscaleImage;
    }

    private static void writeFile(BufferedImage blackAndWhiteImg, String path, String name) {
        try {
            ImageIO.write(blackAndWhiteImg, "png", new File(path, name + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
