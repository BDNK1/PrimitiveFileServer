package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    private static final int PORT = 55535;
    private static String address = "127.0.0.1";


    public static void main(String[] args) {
        Storage storage = new Storage();
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(address))   ){
            Executor executor = Executors.newFixedThreadPool(5);
            while (!server.isClosed()) {
                try {
                    executor.execute(new Session(server, server.accept()));

                } catch (IOException e) {
                }
            }
        }catch(SocketException e){

        } catch (IOException e ) {

        }

    }
}