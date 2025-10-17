package course1;
import model.Photo;
import service.GalleryOptimizer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static GalleryOptimizer optimizer = new GalleryOptimizer();
    private static List<Photo> currentPhotos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Оптимизатор галереи фотографий ===");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loadPhotos();
                    break;
                case "2":
                    findAndHandleDuplicates();
                    break;
                case "3":
                    organizePhotos();
                    break;
                case "4":
                    showCurrentPhotos();
                    break;
                case "0":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Загрузить фотографии");
        System.out.println("2. Найти и удалить дубликаты");
        System.out.println("3. Организовать фотографии по каталогам");
        System.out.println("4. Показать текущие фотографии");
        System.out.println("0. Выход");
        System.out.print("Выберите опцию: ");
    }

    private static void loadPhotos() {
        System.out.print("Введите путь к директории с фотографиями: ");
        String directoryPath = scanner.nextLine();

        try {
            // Простой способ получить список файлов изображений
            List<Path> imagePaths = new ArrayList<>();
            java.nio.file.Files.walk(Paths.get(directoryPath))
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        return fileName.endsWith(".jpg") ||
                                fileName.endsWith(".jpeg") ||
                                fileName.endsWith(".png");
                    })
                    .forEach(imagePaths::add);

            currentPhotos = optimizer.loadPhotos(imagePaths);
            System.out.println("Загружено фотографий: " + currentPhotos.size());

        } catch (Exception e) {
            System.err.println("Ошибка при загрузке фотографий: " + e.getMessage());
        }
    }

    private static void findAndHandleDuplicates() {
        if (currentPhotos.isEmpty()) {
            System.out.println("Сначала загрузите фотографии.");
            return;
        }

        Map<String, List<Photo>> duplicates = optimizer.findDuplicates(currentPhotos);

        if (duplicates.isEmpty()) {
            System.out.println("Дубликаты не найдены.");
            return;
        }

        System.out.println("Найдено групп дубликатов: " + duplicates.size());
        for (Map.Entry<String, List<Photo>> entry : duplicates.entrySet()) {
            System.out.println("Дубликаты (" + entry.getValue().size() + " файлов):");
            for (Photo photo : entry.getValue()) {
                System.out.println("  - " + photo.getFilePath());
            }
        }

        System.out.print("Удалить дубликаты? (y/n): ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("y")) {
            System.out.print("Оставить одну копию каждого файла? (y/n): ");
            boolean keepOne = scanner.nextLine().equalsIgnoreCase("y");
            optimizer.deleteDuplicates(duplicates, keepOne);
            System.out.println("Дубликаты обработаны.");
        }
    }

    public static void organizePhotos() {
        if (currentPhotos.isEmpty()) {
            System.out.println("Сначала загрузите фотографии.");
            return;
        }

        System.out.println("1. Организовать по дате");

        System.out.print("Выберите опцию: ");
        String choice = scanner.nextLine();

        System.out.print("Введите целевую директорию: ");
        String targetDir = scanner.nextLine();

        if (choice.equals("1")) {
            optimizer.organizeByDate(currentPhotos, Paths.get(targetDir));
        } else {
            System.out.println("Доступна только организация по дате.");
        }
    }

    private static void showCurrentPhotos() {
        if (currentPhotos.isEmpty()) {
            System.out.println("Нет загруженных фотографий.");
            return;
        }

        System.out.println("Текущие фотографии (" + currentPhotos.size() + "):");
        for (Photo photo : currentPhotos) {
            System.out.println(" - " + photo.getFilePath());
            if (photo.getMetadata() != null) {
                System.out.println("   Метаданные: " + photo.getMetadata());
            }
        }
    }
}