package dev.mevur.com.scrollview;

public class Data {
    private double direction;
    private String text;
    private int img;

    // 0 for text & img
    // 1 for img only
    // default type is 0
    private int type = 0;

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
