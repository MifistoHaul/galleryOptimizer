package model;
import java.time.LocalDateTime;
import java.util.Objects;

public class PhotoMetadata {
    private LocalDateTime dateTaken;
    private long fileSize;
    private int width;
    private int height;

    // Конструкторы, геттеры, сеттеры, equals, hashCode
    public PhotoMetadata(LocalDateTime dateTaken, long fileSize, int width, int height) {
        this.dateTaken = dateTaken;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
    }

    // Геттеры
    public LocalDateTime getDateTaken() { return dateTaken; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoMetadata that = (PhotoMetadata) o;
        return fileSize == that.fileSize &&
                width == that.width &&
                height == that.height &&
                Objects.equals(dateTaken, that.dateTaken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTaken, fileSize, width, height);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "dateTaken=" + dateTaken +
                ", fileSize=" + fileSize +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
