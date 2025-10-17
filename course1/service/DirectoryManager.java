package service;

import model.Photo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DirectoryManager {

    public void organizeByDate(List<Photo> photos, Path targetDirectory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Photo photo : photos) {
            if (photo.getMetadata() != null && photo.getMetadata().getDateTaken() != null) {
                // Создаем путь по шаблону: /год-месяц/оригинальное_имя_файла
                String directoryName = photo.getMetadata().getDateTaken().format(formatter);
                Path newDirectory = targetDirectory.resolve(directoryName);
                Path newFilePath = newDirectory.resolve(photo.getFilePath().getFileName());

                try {
                    // Создаем целевую директорию, если её нет
                    Files.createDirectories(newDirectory);
                    // Перемещаем файл
                    Files.move(photo.getFilePath(), newFilePath);
                    // Обновляем путь у объекта Photo
                    photo.setFilePath(newFilePath);
                    System.out.println("Перемещен: " + newFilePath);
                } catch (Exception e) {
                    System.err.println("Ошибка при перемещении " + photo.getFilePath() + ": " + e.getMessage());
                }
            } else {
                System.out.println("Пропущен (нет даты): " + photo.getFilePath());
            }
        }
    }
}