package service;

import model.Photo;
import model.PhotoMetadata;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MetadataProcessor {

    public List<Photo> loadPhotos(List<Path> imagePaths) {
        List<Photo> photos = new ArrayList<>();

        for (Path path : imagePaths) {
            try {
                PhotoMetadata metadata = extractMetadata(path);
                if (metadata != null) {
                    photos.add(new Photo(path, metadata));
                    System.out.println("Загружено: " + path.getFileName() +
                            " | Размер: " + metadata.getWidth() + "x" + metadata.getHeight());
                }
            } catch (Exception e) {
                System.err.println("Ошибка загрузки метаданных для " + path + ": " + e.getMessage());
            }
        }
        return photos;
    }

    private PhotoMetadata extractMetadata(Path filePath) {
        try {
            // Базовые атрибуты файла
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);

            LocalDateTime dateTaken = LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault()
            );

            long fileSize = attrs.size();

            // РЕАЛЬНОЕ получение размеров изображения
            int[] dimensions = extractImageDimensions(filePath);
            int width = dimensions[0];
            int height = dimensions[1];

            return new PhotoMetadata(dateTaken, fileSize, width, height);

        } catch (Exception e) {
            System.err.println("Ошибка извлечения метаданных для " + filePath + ": " + e.getMessage());
            return createFallbackMetadata(filePath);
        }
    }

    private int[] extractImageDimensions(Path filePath) {
        try {
            // Используем ImageIO для получения реальных размеров изображения
            BufferedImage image = ImageIO.read(filePath.toFile());
            if (image != null) {
                return new int[]{image.getWidth(), image.getHeight()};
            } else {
                System.err.println("Не удалось прочитать изображение: " + filePath);
                return new int[]{0, 0};
            }
        } catch (Exception e) {
            System.err.println("Ошибка получения размеров для " + filePath + ": " + e.getMessage());
            return new int[]{0, 0};
        }
    }

    private PhotoMetadata createFallbackMetadata(Path filePath) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            LocalDateTime fallbackDate = LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault()
            );
            return new PhotoMetadata(fallbackDate, attrs.size(), 0, 0);
        } catch (Exception e) {
            return new PhotoMetadata(null, 0, 0, 0);
        }
    }
}