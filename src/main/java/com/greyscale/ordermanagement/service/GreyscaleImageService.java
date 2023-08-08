package com.greyscale.ordermanagement.service;

import com.greyscale.ordermanagement.model.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//TODO apply https://www.baeldung.com/introduction-to-spring-batch
// also see https://github.com/joshrotenberg/spring-batch-image-processing/tree/master
@Service
public class GreyscaleImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreyscaleImageService.class);

    private static final String IMAGE_DIRECTORY = "./src/main/java/com/greyscale/ordermanagement/images";

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
            LOGGER.info("Trying to write file to " + path);
            ImageIO.write(blackAndWhiteImg, "png", new File(path, name + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImagesAsZip(String emailAddress, LocalDate deliveryDate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        File imageFolder = new File(IMAGE_DIRECTORY + "/" + emailAddress + "-" + deliveryDate);

        if(imageFolder.listFiles() == null) {
            return new byte[] {};
        }

        Arrays.stream(Objects.requireNonNull(imageFolder.listFiles())).forEach(file -> {
            try {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                zipOutputStream.write(Files.readAllBytes(file.toPath()));
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
