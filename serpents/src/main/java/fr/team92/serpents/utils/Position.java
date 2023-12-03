package fr.team92.serpents.utils;

/**
 * Représente une position dans le jeu
 */
public record Position(int x, int y) {

    /**
     * Déplace la position dans la direction donnée
     * @param direction la direction
     * @return la nouvelle position
     */
    public Position move(Direction direction) {
        return switch(direction) {
            case NORTH -> new Position(this.x, this.y - 1);
            case SOUTH -> new Position(this.x, this.y + 1);
            case EAST -> new Position(this.x + 1, this.y);
            case WEST -> new Position(this.x - 1, this.y);
        };
    }

}
