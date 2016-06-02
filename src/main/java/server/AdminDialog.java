package server;

import DB.DBConnector;
import model.Comment;
import model.Course;
import model.Mark;
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

                case "course": {
                    course(parts[1]);
                    break;
                }

                case "download": {
                    download(parts[1]);
                    break;
                }

                case "marks": {
                    marks(parts[1]);
                    break;
                }

                case "comments": {
                    comments(parts[1]);
                    break;
                }
                case "comment": {
                    comment(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4]);
                    break;
                }
                case "delComment": {
                    deleteComment(parts[1], parts[2]);
                    break;
                }
                case "updateVideo": {
                    File file = new File("video/" + parts[1] + ".mp4");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    int in;
                    byte[] byteArray = new byte[8192];
                    while ((in = dataInputStream.read(byteArray)) != -1) {
                        bos.write(byteArray, 0, in);
                        bos.flush();
                    }
                    bos.flush();
                    bos.close();
                    System.gc();

                    break;
                }
                case "updateCourse":{
                    updateCourse(parts[1],parts[2], parts[3]);
                    break;
                }

                case "createCourse":{



                    Course course = new Course();
                    course.setTitle(parts[1]);
                    course.setDescription(parts[2]);
                    course.setLink("video/" + parts[1] + ".mp4");
                    File file = new File("video/" + parts[1] + ".mp4");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    int in;
                    byte[] byteArray = new byte[8192];
                    while ((in = dataInputStream.read(byteArray)) != -1) {
                        bos.write(byteArray, 0, in);
                        bos.flush();
                    }
                    bos.flush();
                    bos.close();
                    connector.insertCourse(course);
                    break;
                }
                case "deleteCourse": {
                    connector.deleteCourse(parts[1]);
                }



            }

            socket.close();
        } catch (IOException e) {
            System.out.println(1);
        }

    }

    private void updateCourse(String oldTitle, String title,String description){
        connector.updateCourse(oldTitle, title, description);
    }

    private void deleteComment(String username, String title) {
        connector.deleteComment(username, title);
    }

    private void comment(int mark, String comment, String username, String title) {
        Course course = connector.getCourse(title);
        User user = connector.getUser(username);
        Mark newMark = new Mark();
        newMark.setCourse(course);
        newMark.setUser(user);
        newMark.setMark(mark);
        Comment newComment = new Comment();
        newComment.setCourse(course);
        newComment.setUser(user);
        newComment.setComment(comment);
        connector.insertMark(newMark);
        connector.insertComment(newComment);
    }

    private void comments(String title) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ArrayList<Comment> list = connector.getAllComments(title);
        objectOutputStream.writeObject(list);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void marks(String title) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ArrayList<Mark> list = connector.getAllMarks(title);
        objectOutputStream.writeObject(list);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void download(String link) throws IOException {
        File file = new File(link);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(file.length());
        objectOutputStream.flush();
        byte[] byteArray = new byte[8192];
        int in;
        while ((in = bis.read(byteArray)) != -1) {
            bos.write(byteArray, 0, in);
        }
        bis.close();
        bos.close();
        objectOutputStream.close();
        System.gc();
    }

    private void course(String title) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(connector.getCourse(title));
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void courses() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(connector.getCourses());
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void register(String username, String password) throws IOException {
        User user;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        if ((user = connector.getUser(username)) == null) {
            user = new User();
            user.setPassword(password);
            user.setUserName(username);
            user.setModerator(false);
            connector.insertUser(user);
            objectOutputStream.writeObject(true);
        } else {
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
