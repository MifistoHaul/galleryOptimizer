package model;

import java.nio.file.Path;
import java.util.Objects;
import java.nio.file.Path;
import java.util.Objects;

public class Photo {
    private Path filePath;
    private PhotoMetadata metadata;

    public Photo(Path filePath, PhotoMetadata metadata) {
        this.filePath = filePath;
        this.metadata = metadata;
    }

    // Геттеры и сеттеры
    public Path getFilePath() { return filePath; }
    public void setFilePath(Path filePath) { this.filePath = filePath; }
    public PhotoMetadata getMetadata() { return metadata; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(filePath, photo.filePath) &&
                Objects.equals(metadata, photo.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, metadata);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "filePath=" + filePath +
                ", metadata=" + metadata +
                '}';
    }
}
