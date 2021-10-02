import java.io.*;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static String path = "C://Надя//Games//";
    static String[] directories = new String[]{"src", "res", "savegames", "temp", "//src//main", "//src//test", "//res//drawables", "//res//vectors", "//res//icons"};
    static String[] files = new String[]{"//src//main//Main.java", "//src//main//Utils.java", "temp//temp.txt"};
    static StringBuilder log = new StringBuilder();

    public static void main(String[] args) {

        for (int i = 0; i < directories.length; i++) {
            if (new File(path + directories[i]).mkdir())
                log.append(LocalDateTime.now() + " создана директория: " + (path + directories[i]) + "\n");
            else {
                log.append(LocalDateTime.now() + " ошибка при создании директории: " + (path + directories[i]) + "\n");
            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                if (new File(path + files[i]).createNewFile())
                    log.append(LocalDateTime.now() + " создан фаил: " + (path + files[i]) + "\n");
            } catch (IOException e) {
                log.append(LocalDateTime.now() + " ошибка при создании файла: " + (path + files[i]) + " " + " " + e.getMessage() + "\n");
            }
        }


        GameProgress gameProgress1 = new GameProgress(100, 10, 100, 154.12);
        GameProgress gameProgress2 = new GameProgress(80, 8, 90, 283.0);
        GameProgress gameProgress3 = new GameProgress(90, 6, 80, 789.0);

        saveGame(path + directories[2] + "//game1.dat", gameProgress1);
        saveGame(path + directories[2] + "//game2.dat", gameProgress2);
        saveGame(path + directories[2] + "//game3.dat", gameProgress3);

        String[] filesPath = new String[]{path + directories[2] + "//game1.dat", path + directories[2] + "//game2.dat", path + directories[2] + "//game3.dat"};
        zipFiles(path + directories[2] + "//games.zip", filesPath);
        deleteFiles(filesPath);

        try (FileWriter writer = new FileWriter("C://Надя//Games//temp//temp.txt")) {
            writer.write(log.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            log.append(LocalDateTime.now() + " игра успешно сохнарена.\n");
        } catch (Exception e) {
            log.append(LocalDateTime.now() + " произошла ошибка при сохранении игры: " + e.getMessage() + "\n");
        }
    }


    public static void zipFiles(String path, String[] filesPath) {
        FileInputStream[] fis = new FileInputStream[filesPath.length];

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path))) {
            for (int i = 0; i < filesPath.length; i++) {
                fis[i] = new FileInputStream(filesPath[i]);
                ZipEntry entry = new ZipEntry(filesPath[i]);
                zos.putNextEntry(entry);
                byte[] buffer = new byte[fis[i].available()];
                fis[i].read(buffer);
                zos.write(buffer);
                zos.closeEntry();
                log.append(LocalDateTime.now() + " Файл: " + filesPath[i] + " добавлен в zip архив\n");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            for (FileInputStream f : fis) {
                if (f != null) {
                    try {
                        f.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }


    public static void deleteFiles(String[] filesPath) {
        for (String f : filesPath) {
            File file = new File(f);
            if(file.delete()){
                log.append(LocalDateTime.now() + " Файл: " + file + " удален\n");
            }
            else {
                log.append(LocalDateTime.now() + " Ошиюбка при удалении файла: " + file + "\n");
            }
        }
    }


}
