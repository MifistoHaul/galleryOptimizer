package service;

import model.Photo;
import util.FileHashCalculator;

import java.nio.file.Path;
import java.util.*;

public class DuplicateFinder {

    public Map<String, List<Photo>> findDuplicates(List<Photo> photos) {
        // Группируем фото по хешу содержимого
        Map<String, List<Photo>> hashToPhotosMap = new HashMap<>();

        for (Photo photo : photos) {
            String fileHash = FileHashCalculator.calculateHash(photo.getFilePath());
            hashToPhotosMap.computeIfAbsent(fileHash, k -> new ArrayList<>()).add(photo);
        }

        // Оставляем только группы с дубликатами (2 и более фото)
        hashToPhotosMap.entrySet().removeIf(entry -> entry.getValue().size() < 2);
        return hashToPhotosMap;
    }

    public void deleteDuplicates(Map<String, List<Photo>> duplicates, boolean keepOneCopy) {
        for (List<Photo> duplicateGroup : duplicates.values()) {
            // Решаем, какой файл оставить (например, первый в списке)
            Photo photoToKeep = keepOneCopy ? duplicateGroup.get(0) : null;

            for (Photo duplicate : duplicateGroup) {
                if (duplicate != photoToKeep) {
                    try {
                        boolean deleted = java.nio.file.Files.deleteIfExists(duplicate.getFilePath());
                        if (deleted) {
                            System.out.println("Удален дубликат: " + duplicate.getFilePath());
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка при удалении " + duplicate.getFilePath() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}