package fr.team92.serpents.utils;

/**
 * Représente une position dans le jeu
 */
public record Position(double x, double y) {

    /**
     * Déplace la position dans la direction donnée
     * 
     * @param direction la direction
     * @return la nouvelle position
     */
    public Position move(Direction direction, double delta) {
        double dx = Math.cos(direction.getAngle()) * delta;
        double dy = Math.sin(direction.getAngle()) * delta;
        return new Position(this.x + dx, this.y + dy);
    }

    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
