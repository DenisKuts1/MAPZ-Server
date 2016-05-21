package model;

import java.io.Serializable;

/**
 * Created by Анатолий on 23.04.2016.
 */
public class Course implements Serializable {

    private int id;
    private String title;
    private String description;
    private String link;

    public Course() {
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
