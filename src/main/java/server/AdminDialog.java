package server;

import DB.DBConnector;
import model.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Анатолий on 20.03.2016.
 */
public class AdminDialog implements Runnable {


    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Socket socket;
    private final DBConnector connector;
    private boolean isStop;

    public AdminDialog(Socket socket, DBConnector connector) {
        this.socket = socket;
        this.connector = connector;
    }

    @Override
    public void run() {
        try {

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String inputMessage = dataInputStream.readUTF();

            //logger.info("read " + inputMessage);
            String[] parts = inputMessage.split("\n");
            System.out.println(parts[0]);
            switch (parts[0]) {
                case "authorize": {

                    authorize(parts[1], parts[2]);

                    break;
                }
                case "register": {

                    register(parts[1], parts[2]);

                    break;
                }
                case "courses": {

                    courses();
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println(1);
        }

    }

    private void courses() throws IOException{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(connector.getCourses());
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void register(String username, String password) throws IOException {
        User user;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        if((user = connector.getUser(username)) == null){
            user = new User();
            user.setPassword(password);
            user.setUserName(username);
            user.setModerator(false);
            connector.insertUser(user);
            objectOutputStream.writeObject(true);
        } else
        {
            objectOutputStream.writeObject(false);
        }
        objectOutputStream.flush();
        objectOutputStream.close();
}

    private void authorize(String username, String password) throws IOException {
        User user;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        if ((user = connector.getUser(username)) == null) {
            System.out.println("null");
            objectOutputStream.writeObject(null);
            objectOutputStream.flush();
            objectOutputStream.close();
            return;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println(password);
            objectOutputStream.writeObject(null);
            objectOutputStream.flush();
        } else {
            System.out.println("success");
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            System.out.println("success");
        }

        objectOutputStream.close();
    }
}
