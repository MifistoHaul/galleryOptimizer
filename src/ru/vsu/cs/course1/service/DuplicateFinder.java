package service;

import model.Photo;
import util.FileHashCalculator;

import java.nio.file.Path;
import java.util.*;

public class DuplicateFinder {

    public Map<String, List<Photo>> findDuplicates(List<Photo> photos) {
        Map<String, List<Photo>> hashToPhotosMap = new HashMap<>();

        for (Photo photo : photos) {
            String fileHash = FileHashCalculator.calculateHash(photo.getFilePath());
            hashToPhotosMap.computeIfAbsent(fileHash, k -> new ArrayList<>()).add(photo);
        }

        // Оставляем только группы с дубликатами
        hashToPhotosMap.entrySet().removeIf(entry -> entry.getValue().size() < 2);

        // Дополнительная проверка: удаляем группы, где фото имеют разные размеры
        // (это не должны быть дубликаты)
        hashToPhotosMap.entrySet().removeIf(entry -> hasDifferentSizes(entry.getValue()));

        return hashToPhotosMap;
    }

    private boolean hasDifferentSizes(List<Photo> photos) {
        if (photos.isEmpty()) return false;

        Photo first = photos.get(0);
        int firstWidth = first.getMetadata().getWidth();
        int firstHeight = first.getMetadata().getHeight();

        for (Photo photo : photos) {
            if (photo.getMetadata().getWidth() != firstWidth ||
                    photo.getMetadata().getHeight() != firstHeight) {
                System.out.println("Найдены файлы с одинаковым хешем но разными размерами:");
                System.out.println("  " + first.getFilePath() + " - " + firstWidth + "x" + firstHeight);
                System.out.println("  " + photo.getFilePath() + " - " +
                        photo.getMetadata().getWidth() + "x" + photo.getMetadata().getHeight());
                return true;
            }
        }
        return false;
    }

    public void deleteDuplicates(Map<String, List<Photo>> duplicates, boolean keepOneCopy) {
        for (List<Photo> duplicateGroup : duplicates.values()) {
            Photo photoToKeep = keepOneCopy ? duplicateGroup.get(0) : null;

            for (Photo duplicate : duplicateGroup) {
                if (duplicate != photoToKeep) {
                    try {
                        boolean deleted = java.nio.file.Files.deleteIfExists(duplicate.getFilePath());
                        if (deleted) {
                            System.out.println("Удален дубликат: " + duplicate.getFilePath() +
                                    " (" + duplicate.getMetadata().getWidth() +
                                    "x" + duplicate.getMetadata().getHeight() + ")");
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка при удалении " + duplicate.getFilePath() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}