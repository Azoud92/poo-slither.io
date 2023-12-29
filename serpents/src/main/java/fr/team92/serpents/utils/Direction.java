package fr.team92.serpents.utils;

public class Direction {
    private double angle;

    public Direction(double angle) {
        this.angle = angle;
    }

    public Direction opposite() {
        return new Direction((this.angle + Math.PI) % (2 * Math.PI));
    }

    public double getAngle() {
        return angle;
    }
}
