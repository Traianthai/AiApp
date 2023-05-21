package com.ttcs1.model;


import java.io.Serializable;

public class Leaf implements Serializable {
    private int id;
    private String name,inf;
    private String img;
    private String date;
    private byte[] imgBitmap;

    public Leaf(int id, String name, String inf, String img, String date,byte[] imgBitmap) {
        this.id = id;
        this.name = name;
        this.inf = inf;
        this.img = img;
        this.date = date;
        this.imgBitmap = imgBitmap;
    }
    public Leaf(String name, String inf, String img, String date) {
        this.name = name;
        this.inf = inf;
        this.img = img;
        this.date = date;
    }



    public Leaf() {
    }

    public int getId() {
        return id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public byte[] getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(byte[] imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getDate() {
        return date;
    }


    public String getImg() {
        return img;
    }

}
