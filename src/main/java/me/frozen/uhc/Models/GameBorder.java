package me.frozen.uhc.Models;

import org.bukkit.Location;
import org.bukkit.World;

import static me.frozen.uhc.Models.GameBorder.status.STATIONARY;

public class GameBorder  {

    private World world;
    private double borderSize;
    private double borderRoof;
    private double borderFloor;
    private Location center;
    public enum status {STATIONARY, MOVING};
    private GameBorder.status status;


    public GameBorder(World world, double borderSize) {
        this.world = world;
        this.borderSize = borderSize;
        this.borderRoof = 320;
        this.borderFloor = -64;
        this.center = new Location(this.world, 0, 0, 0);
        this.status = STATIONARY;
    }

    public GameBorder(World world, double borderSize, Location center) {
        this.world = world;
        this.borderSize = borderSize;
        this.borderRoof = 320;
        this.borderFloor = -64;
        this.center = center;
        this.status = STATIONARY;
    }

    public double getBorderRoof() {
        return borderRoof;
    }

    public void setBorderRoof(double borderRoof) {
        this.borderRoof = borderRoof;
    }

    public double getBorderFloor() {
        return borderFloor;
    }

    public void setBorderFloor(double borderFloor) {
        this.borderFloor = borderFloor;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(double borderSize) {
        this.borderSize = borderSize;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public GameBorder.status getStatus() {
        return status;
    }

    public void setStatus(GameBorder.status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GameBorder{" +
                "borderSize=" + borderSize +
                ", borderRoof=" + borderRoof +
                ", borderFloor=" + borderFloor +
                ", center=" + center +
                '}';
    }
}
