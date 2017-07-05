package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ina on 2017-05-31.
 */
public class ServerOpen {

    private ServerSocket server_socket;
    private Socket per_client_socket;

    public ServerOpen(int port) {
        InitServerSocket(port);
        while (true) {
            Socket socket = null;
            WaitForClient();
            WorkStart();
        }
    }
    private void InitServerSocket(int port) {
        try {
            server_socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket Making Failure");
            System.exit(0);
        }
    }
    private void WaitForClient() {
        System.out.println("Waiting For Client...");
        try {
            per_client_socket = server_socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            per_client_socket = null;
        }
    }
    private void WorkStart() {
        if(per_client_socket == null) {
            return;
        }
        System.out.println("Client IP: " + per_client_socket.getInetAddress().getHostAddress());
        System.out.println("Client Port: " + per_client_socket.getPort());
        ServerWorker server_worker = new ServerWorker(per_client_socket);
        Thread worker_thread = new Thread(server_worker);
        worker_thread.start();
    }
}
