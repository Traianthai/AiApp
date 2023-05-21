package com.ttcs1.model;

import java.io.Serializable;

public class Item implements Serializable {
    private int img;
    private String label,model,name;

    public Item() {
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Item(int img, String label, String model, String name) {
        this.img = img;
        this.label = label;
        this.model = model;
        this.name = name;
    }
}
