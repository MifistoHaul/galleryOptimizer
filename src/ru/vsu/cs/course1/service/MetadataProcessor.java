package service;

import model.Photo;
import model.PhotoMetadata;

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
                photos.add(new Photo(path, metadata));
            } catch (Exception e) {
                System.err.println("Ошибка загрузки метаданных для " + path + ": " + e.getMessage());
            }
        }
        return photos;
    }

    private PhotoMetadata extractMetadata(Path filePath) {
        try {
            // В реальном приложении здесь используется библиотека, например, Apache Sanselan или ExifTool
            // Для демонстрации используем базовые атрибуты файла
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);

            LocalDateTime dateTaken = LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault()
            );

            long fileSize = attrs.size();

            // Заглушка для размеров изображения
            // В реальности нужно использовать ImageIO или аналоги
            int width = 0;
            int height = 0;

            return new PhotoMetadata(dateTaken, fileSize, width, height);

        } catch (Exception e) {
            System.err.println("Ошибка извлечения метаданных для " + filePath + ": " + e.getMessage());
            return null;
        }
    }
}