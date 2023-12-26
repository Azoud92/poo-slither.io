package fr.team92.serpents.utils;

/**
 * Représente une position dans le jeu
 */
public record Position(double x, double y) {

    /**
     * Déplace la position dans la direction donnée
     * @param direction la direction
     * @return la nouvelle position
     */
    public Position move(Direction direction, double delta) {
        return switch(direction) {
            case NORTH -> new Position(this.x, this.y - delta);
            case SOUTH -> new Position(this.x, this.y + delta);
            case EAST -> new Position(this.x + delta, this.y);
            case WEST -> new Position(this.x - delta, this.y);
        };
    }

    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
