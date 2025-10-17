package ru.vsu.cs.course1;

import ru.vsu.cs.course1.model.Photo;
import ru.vsu.cs.course1.service.GalleryOptimizer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Основной API библиотеки для оптимизации галереи фотографий
 */
public class PhotoGalleryOptimizer {
    private final GalleryOptimizer optimizer;

    public PhotoGalleryOptimizer() {
        this.optimizer = new GalleryOptimizer();
    }

    public List<Photo> loadPhotos(List<Path> imagePaths) {
        return optimizer.loadPhotos(imagePaths);
    }

    public Map<String, List<Photo>> findDuplicates(List<Photo> photos) {
        return optimizer.findDuplicates(photos);
    }

    public void deleteDuplicates(Map<String, List<Photo>> duplicates, boolean keepOneCopy) {
        optimizer.deleteDuplicates(duplicates, keepOneCopy);
    }

    public void organizeByDate(List<Photo> photos, Path targetDirectory) {
        optimizer.organizeByDate(photos, targetDirectory);
    }
}