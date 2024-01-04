package fr.team92.serpents.utils;

public record Direction(double angle) {

    public Direction {
        if (angle < 0) {
            angle += 360;
        } else if (angle >= 360) {
            angle -= 360;
        }
        angle = angle % 360;
    }

    public Direction opposite() {
        return new Direction((this.angle + 180) % 360);
    }

}
