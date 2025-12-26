package com.example.baseproduct.model;


import java.io.Serializable;

public class MusicModel implements Serializable {
    private int id;
    private String name;
    private String image;
    private String singer;
    private String category;
    private String link;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getSinger() {
        return singer;
    }

    public String getCategory() {
        return category;
    }

    public String getLink() {
        return link;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
