package fr.team92.serpents.utils;

/**
 * Contient les directions possibles pour un déplacement
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    /**
     * Retourne la direction opposée
     * @return la direction opposée
     */
    public Direction opposite() {
        switch (this) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            case WEST: return EAST;
            default: throw new IllegalStateException("Direction inconnue");
        }
    }
}
