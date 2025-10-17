package service;

import model.Photo;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GalleryOptimizer {
    private final DuplicateFinder duplicateFinder;
    private final DirectoryManager directoryManager;
    private final MetadataProcessor metadataProcessor;

    public GalleryOptimizer() {
        this.duplicateFinder = new DuplicateFinder();
        this.directoryManager = new DirectoryManager();
        this.metadataProcessor = new MetadataProcessor();
    }

    public List<Photo> loadPhotos(List<Path> imagePaths) {
        return metadataProcessor.loadPhotos(imagePaths);
    }

    public Map<String, List<Photo>> findDuplicates(List<Photo> photos) {
        return duplicateFinder.findDuplicates(photos);
    }

    public void deleteDuplicates(Map<String, List<Photo>> duplicates, boolean keepOneCopy) {
        duplicateFinder.deleteDuplicates(duplicates, keepOneCopy);
    }

    public void organizeByDate(List<Photo> photos, Path targetDirectory) {
        directoryManager.organizeByDate(photos, targetDirectory);
    }

}