package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Session implements Runnable {
    String choose;
    ServerSocket server;
    Socket socket;
    Scanner scanner = new Scanner(System.in);
    DataInputStream input;
    DataOutputStream output;
    String[] msg;

    public Session(ServerSocket server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
            msg = input.readUTF().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        switch (msg[0]) {
            case "GET": {
                get();
                break;
            }
            case "PUT": {
                put();
                break;
            }
            case "DELETE": {
                delete();
                break;
            }
            case "exit": {
                try {
                    server.close();
                } catch (IOException e) {
                    System.out.println("hi");
                    e.printStackTrace();
                }
            }
        }

    }

    public void get() {
        String res = Storage.getFromStorage(msg[1], msg[2]);
        String[] resArr = res.split(" ");
        try {
            if (resArr[0].equals("200")) {
                output.writeUTF("File saved on the hard drive!");
            } else {
                output.writeUTF("The response says that the file was not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void put() {

        String[] res = Storage.addToStorage(msg[1]).split(" ");
        try {
            if (res[0].equals("200")) {
                output.writeUTF("Response says that file is saved!" + " ID = " + res[1]);
            } else {
                output.writeUTF("The response says that creating the file was forbidden!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        String res = Storage.deleteFromStorage(msg[1], msg[2]);
        try {
            if (res.equals("200")) {
                output.writeUTF("The response says that the file was deleted succesfully!");
            } else {
                output.writeUTF("The response says that the file was not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
