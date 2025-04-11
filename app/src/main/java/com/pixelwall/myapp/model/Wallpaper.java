package com.pixelwall.pixelwall.model;

public class Wallpaper {
    private String id;
    private String imageUrl;
    private int width;
    private int height;

    public Wallpaper(String id, String imageUrl, int width, int height) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
} 