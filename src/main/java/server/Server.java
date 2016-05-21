package server;



import DB.DBConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Анатолий on 20.03.2016.
 */
public class Server {

    private ServerSocket serverSocket;
    private boolean isStop;
    private DBConnector connector;

    public Server(DBConnector connector){
        try {
            serverSocket = new ServerSocket(7755);
            isStop = false;
            this.connector = connector;
            catchConnection.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Thread catchConnection = new Thread(()->{
        while (!isStop){
            try {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new AdminDialog(socket,connector));
                thread.start();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    });
}
