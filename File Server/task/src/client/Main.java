package client;

import server.Storage;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Main {
    static String address = "127.0.0.1";
    static int port = 55535;
    static Scanner scanner = new Scanner(System.in);
    static StringBuilder outputStr = new StringBuilder();

    static Storage storage = new Storage();

    public static void main(String[] args) {

        try
                (Socket socket = new Socket(InetAddress.getByName(address), port);
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
            String choose = scanner.nextLine();
            switch (choose) {
                case "1": {
                    getRequest();
                    break;
                }
                case "2": {
                    putRequest();
                    break;
                }
                case "3": {
                    delRequest();
                    break;
                }
                case "exit": {
                    outputStr.append("exit");
                    System.out.println("The request was sent.");
                    break;
                }
            }
            output.writeUTF(outputStr.toString());
            if(!outputStr.toString().equals("exit")){
            String response = input.readUTF();
            System.out.println(response);}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getRequest() {

        System.out.print("Do you want to get the file by name or by id (1 - name, 2- id): ");
        String idName = String.valueOf(scanner.nextInt());
        String nameToSave = null;
        boolean finded = false;
        switch (idName) {
            case "1":
                System.out.print("Enter filename: ");
                String name = scanner.nextLine();
                name = scanner.nextLine();
                outputStr.append("GET ").append("BY_NAME ").append(name);
                finded = Storage.files.containsValue(name);
                break;
            case "2":
                System.out.print("Enter id: ");
                String id = scanner.nextLine();
                id = scanner.nextLine();
                outputStr.append("GET ").append("BY_ID ").append(id);
                 finded = Storage.files.containsKey(id);
                break;
        }
        System.out.println("The request was sent.");
        if (!finded){
            System.out.println("The response says that this file is not found!");
        }
        else{
            System.out.println("The file was downloaded! Specify a name for it:");
            nameToSave= scanner.nextLine();
            if(!nameToSave.isEmpty()){
                outputStr.append("|").append(nameToSave);
            }
        }
    }

    private static void putRequest() {
        System.out.print("Enter name of the file: ");
        String name = scanner.nextLine();
        System.out.print("Enter name of the file to be saved on server: ");
        String secName = scanner.nextLine();
        if (secName.isEmpty()) {
            outputStr.append("PUT ").append(name);
        } else {
            outputStr.append("PUT ").append(name).append("|").append(secName);
        }
        System.out.println("The request was sent.");
    }

    private static void delRequest() {
        System.out.print("Do you want to delete the file by name or by id (1 - name, 2- id): ");
        String idName = String.valueOf(scanner.nextInt());
        switch (idName) {
            case "1":
                System.out.print("Enter filename: ");
                String name = scanner.nextLine();
                name = scanner.nextLine();

                outputStr.append("DELETE ").append("BY_NAME ").append(name);
                break;
            case "2":
                System.out.print("Enter id: ");
                String id = scanner.nextLine();
                id = scanner.nextLine();
                outputStr.append("DELETE ").append("BY_ID ").append(id);
                break;
        }
        System.out.println(outputStr);
        System.out.println("The request was sent.");
    }


}
