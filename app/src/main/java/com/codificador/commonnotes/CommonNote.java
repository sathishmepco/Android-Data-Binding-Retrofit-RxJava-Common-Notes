package com.codificador.commonnotes;

import android.graphics.Color;
import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class CommonNote implements Serializable{

    private String title;
    private String id;
    private String description;
    private int colorCode;
    private long time;

    public CommonNote(){
        id = UUID.randomUUID().toString();
        time = System.currentTimeMillis();
        colorCode = generateRandomColor();
    }

    public CommonNote(String title, String id, String description, int colorCode, long time) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.colorCode = colorCode;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int generateRandomColor(){
        Random random = new Random();
        return Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
    }
}