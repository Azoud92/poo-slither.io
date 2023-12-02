package fr.team92.serpents.utils;

/**
 * Une position représente une paire de coordonnées X et Y dans un plan
 */
public final class Position implements Cloneable {

    /**
     * Représente les coordonnées x et y d'une position
     */
    private int x, y;

    /**
     * Créer une nouvelle position à partir des coordonnées x et y
     * @param x la coordonnée x
     * @param y la coordonnée y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Déplacer la position dans la direction donnée
     * @param direction la direction dans laquelle déplacer la position
     * @return la nouvelle position
     * @throws IllegalArgumentException si la direction est inconnue
     */
    public Position move(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Position(this.x, this.y - 1);
            case SOUTH:
                return new Position(this.x, this.y + 1);
            case EAST:
                return new Position(this.x + 1, this.y);
            case WEST:
                return new Position(this.x - 1, this.y);
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    /**
     * Obtenir la coordonnée X de la position
     * @return la coordonnée X
     */
    public int getX() {
        return this.x;
    }

    /**
     * Modifier la coordonnée X de la position
     * @param x la nouvelle coordonnée X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Obtenir la coordonnée Y de la position
     * @return la coordonnée Y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Modifier la coordonnée Y de la position
     * @param y la nouvelle coordonnée Y
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Position clone() {
        return new Position(this.x, this.y);
    }

    /**
     * Vérifier si la position est égale à une autre position
     * @param pos la position à comparer
     * @return true si les positions sont égales, false sinon
     */
    public boolean equals(Position pos) {
        return this.x == pos.getX() && this.y == pos.getY();
    }   

}
