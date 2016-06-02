package DB;

import model.Comment;
import model.Course;
import model.Mark;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Анатолий on 18.03.2016.
 */
public class DBConnector {

    private Connection dbConnection;

    public DBConnector() {
        dbConnection = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(1);
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:mydb.db");
        } catch (SQLException e) {
            System.out.println(2);
        }
    }

    private boolean insert(String insertTableSQL) {
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute(insertTableSQL);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void updateCourse(String oldTitle, String title, String description) {
        try {
            Statement statement = dbConnection.createStatement();
            Course course = getCourse(oldTitle);
            if(!course.getLink().equals("video/" + title + ".mp4")) {
                //File newFile = new File("video/" + title + ".mp4");
                if(!oldTitle.equals(title)){
                    File file = new File(course.getLink());
                    file.delete();
                }
                /*try {
                    FileInputStream inputStream = new FileInputStream(file);
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
                    byte[] byteArray = new byte[8196];
                    int in;
                    while ((in = inputStream.read(byteArray)) != -1) {
                        outputStream.write(byteArray, 0, in);
                    }

                    inputStream.close();
                    outputStream.close();
                    System.gc();
                } catch (IOException e) {
                }*/
            }



            ResultSet rs = statement.executeQuery("UPDATE Courses SET title = '" + title + "', description = '" + description
                    + "', link = '" + "video/" + title + ".mp4" + "' WHERE title = '" + oldTitle + "'");
        } catch (SQLException e) {
        }
    }

    public boolean insertUser(User user) {
        String insertTableSQL = "INSERT INTO Users"
                + "( userName, password, isModerator) " + "VALUES"
                + "('" + user.getUserName() + "','" + user.getPassword() + "',"
                + (user.isModerator() ? 0 : -1) + ");";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }


    public User getUser(String userName) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Users WHERE userName = '" + userName + "'");
            User user = new User();
            user.setUserName(userName);
            user.setPassword(rs.getString("password"));
            user.setId(rs.getInt("id"));
            if (rs.getInt("isModerator") == -1) {
                user.setModerator(false);
            } else {
                user.setModerator(true);
            }
            System.out.println(user.isModerator());
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public User getUserById(int id) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Users WHERE id = '" + id + "'");
            User user = new User();
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setId(rs.getInt("id"));
            user.setModerator(rs.getBoolean("isModerator"));
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean insertCourse(Course course) {
        String insertTableSQL = "INSERT INTO Courses"
                + "(title, description, link) " + "VALUES"
                + "('" + course.getTitle() + "','" + course.getDescription() + "','"
                + course.getLink() + "');";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }


    public Course getCourse(String name) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Courses WHERE title = '" + name + "'");
            Course course = new Course();
            course.setDescription("description");
            course.setTitle(rs.getString("title"));
            course.setId(rs.getInt("id"));
            course.setLink(rs.getString("link"));
            return course;
        } catch (SQLException e) {
            return null;
        }
    }


    public ArrayList<String> getCourses() {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Courses");

            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                list.add(rs.getString("title"));
            }
            return list;

        } catch (SQLException e) {
            return null;
        }
    }

    public boolean insertMark(Mark mark) {
        String insertTableSQL = "INSERT INTO Marks"
                + "(mark, userID, courseID) " + "VALUES"
                + "(" + mark.getMark() + "," + mark.getUser().getId() + "," + mark.getCourse().getId() + ");";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }

    public Mark getMark(User user, Course course) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Marks WHERE userID = " + user.getId() + " AND courseID = " + course.getId());
            Mark mark = new Mark();
            mark.setCourse(course);
            mark.setUser(user);
            mark.setMark(rs.getInt("mark"));
            mark.setId(rs.getInt("id"));
            return mark;
        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<Mark> getAllMarks(String title) {
        try {
            Statement statement = dbConnection.createStatement();
            Course course = getCourse(title);
            ResultSet rs = statement.executeQuery("Select * FROM Marks WHERE courseID = " + course.getId());
            ArrayList<Mark> list = new ArrayList<Mark>();
            while (rs.next()) {
                Mark mark = new Mark();
                mark.setCourse(course);
                mark.setUser(getUserById(rs.getInt("userID")));
                mark.setMark(rs.getInt("mark"));
                mark.setId(rs.getInt("id"));
                list.add(mark);
            }
            return list;
        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<Comment> getAllComments(String title) {
        try {
            Course course = getCourse(title);
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Comments WHERE courseID = " + course.getId());
            ArrayList<Comment> list = new ArrayList<Comment>();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setCourse(course);
                comment.setUser(getUserById(rs.getInt("userID")));
                comment.setComment(rs.getString("comment"));
                comment.setId(rs.getInt("id"));
                list.add(comment);
            }
            return list;
        } catch (SQLException e) {
            return null;
        }
    }


    public boolean insertComment(Comment comment) {
        String insertTableSQL = "INSERT INTO Comments"
                + "( comment, userID, courseID) " + "VALUES"
                + "('" + comment.getComment() + "'," + comment.getUser().getId() + "," + comment.getCourse().getId() + ");";
        return insert(insertTableSQL);
    }

    public Comment getComment(User user, Course course) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Comments WHERE userID = " + user.getId() + " AND courseID = " + course.getId());
            Comment comment = new Comment();
            comment.setCourse(course);
            comment.setUser(user);
            comment.setComment(rs.getString("comment"));
            comment.setId(rs.getInt("id"));
            return comment;
        } catch (SQLException e) {
            return null;
        }
    }

    public void deleteComment(String username, String title) {
        try {
            Course course = getCourse(title);
            User user = getUser(username);
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("DELETE FROM Comments WHERE userID = " + user.getId() + " AND courseID = " + course.getId());
        } catch (SQLException e) {
        }
    }
}
