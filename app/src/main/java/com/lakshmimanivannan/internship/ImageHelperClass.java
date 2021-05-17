package com.lakshmimanivannan.internship;

public class ImageHelperClass {
    String image,name,num,video;
    String id,genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public ImageHelperClass(String image, String name) {
        this.image = image;
        this.name = name;
        this.num = "";
    }

    public ImageHelperClass(String image, String name, String video, String id, String genre) {
        this.image = image;
        this.name = name;
        this.num = "";
        this.video = video;
        this.id = id;
        this.genre = genre;
    }

    public ImageHelperClass(String image, String name, String video) {
        this.image = image;
        this.name = name;
        this.num = "";
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
