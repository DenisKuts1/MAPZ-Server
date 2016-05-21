package model;

import java.io.Serializable;

/**
 * Created by Анатолий on 23.04.2016.
 */
public class Comment implements Serializable {

    private int id;
    private String comment;
    private User user;
    private Course course;

    public Comment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


}
