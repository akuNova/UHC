package me.frozen.uhc.Models;

public class Stage {

    private int time;
    private int size;
    private int yFloor, yRoof;
    private boolean borderMove;
    private String message;

    public int getyFloor() {
        return yFloor;
    }

    public void setyFloor(int yFloor) {
        this.yFloor = yFloor;
    }

    public int getyRoof() {
        return yRoof;
    }

    public void setyRoof(int yRoof) {
        this.yRoof = yRoof;
    }

    public Stage(int time, int size, int yFloor, int yRoof, boolean borderMove, String message) {
        this.time = time;
        this.size = size;
        this.yFloor = yFloor;
        this.yRoof = yRoof;
        this.borderMove = borderMove;
        this.message = message;
    }

    public Stage(int time, int size, int yFloor, int yRoof, boolean borderMove) {
        this.time = time;
        this.size = size;
        this.yFloor = yFloor;
        this.yRoof = yRoof;
        this.borderMove = borderMove;
        this.message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "time=" + time +
                ", size=" + size +
                ", borderMove=" + borderMove +
                '}';
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isBorderMove() {
        return borderMove;
    }

    public void setBorderMove(boolean borderMove) {
        this.borderMove = borderMove;
    }
}
