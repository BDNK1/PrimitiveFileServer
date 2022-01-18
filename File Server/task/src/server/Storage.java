package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Storage {
    public static String serverPath =   System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;
    public static String clientPath =   System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;
    public static Map<String, String> files = new HashMap<>();

    public static Map<String, String> getFiles() {
        return files;
    }

    public Storage() {
        File serverDir = new File(serverPath);
        File clientDir = new File(clientPath);
        File filesMap = new File(System.getProperty("user.dir") +
                File.separator + "src" + File.separator+ "files.map");
        serverDir.mkdirs();
        clientDir.mkdirs();
        try {

            if (filesMap.exists() && filesMap.length() > 0) {
                FileInputStream fis = new FileInputStream(filesMap);
                ObjectInputStream ois = new ObjectInputStream(fis);

                files = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String getFromStorage(String by, String filename) {
        String name = filename;
        String nameToSave= filename;
        if (filename.contains("|")) {
            String[] buff = filename.split("\\|");
            name = buff[0];
            nameToSave = buff[1];
        }
        if (by.equals("BY_ID")) {
            name = files.get(name);
        }

        File file = new File(Storage.serverPath + name);
        File fileSave = new File(Storage.clientPath + nameToSave);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                FileOutputStream outputStream = new FileOutputStream(fileSave);
                byte[] content = inputStream.readAllBytes();
                boolean res = fileSave.createNewFile();
                outputStream.write(content);
                outputStream.close();
                inputStream.close();
                return "200";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "404";

    }

    public static String deleteFromStorage(String by, String filename) {
        if (by.equals("BY_ID")) {
            filename = files.get(filename);
        }
        //File file = new File(Storage.serverPath + filename);
        try {
            Files.deleteIfExists(Paths.get(Storage.serverPath+filename));
            String finalFilename = filename;
            files.values().removeIf(v -> v.equals(finalFilename));
            reloadMap();
            return "200";
        } catch (IOException e) {
            e.printStackTrace();
            return "404";
        }
    }


    public static String addToStorage(String filename) {
        String id = null;
        String nameToSave = null;
        String name = null;
        if (filename.contains("|")) {
            String[] buff = filename.split("\\|");
            name = buff[0];
            nameToSave = buff[1];
        } else {
            name = filename;
        }
        File fileSave = new File(Storage.clientPath + name);
        File file = null;
        if (nameToSave == null) {
            file = new File(Storage.serverPath + name);
        } else {
            file = new File(Storage.serverPath + nameToSave);
        }
        if (file.exists() || !fileSave.exists()) {
            return "403";
        } else {
            try {
                FileInputStream inputStream = new FileInputStream(fileSave);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] content = inputStream.readAllBytes();
                boolean res = file.createNewFile();
                outputStream.write(content);
                outputStream.close();
                inputStream.close();
                id = String.valueOf((int)(Math.random()* (99 - 10) + 10));
                files.put(id, nameToSave == null ? name : nameToSave);
                System.out.println(files.toString());
                reloadMap();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "200 " + id;

        }
    }

    private static void reloadMap() {
        try {
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") +
                    File.separator + "src" + File.separator+ "files.map");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(files);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
