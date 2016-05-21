package model;

import java.io.Serializable;

/**
 * Created by Анатолий on 23.04.2016.
 */
public class Mark implements Serializable {


    private int id;
    private int mark;
    private User user;
    private Course course;

    public Mark() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
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
