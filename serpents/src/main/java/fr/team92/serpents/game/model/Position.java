package fr.team92.serpents.game.model;

public final class Position implements Cloneable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate
     * @return the x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set the x coordinate
     * @param x the x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y coordinate
     * @return the y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the y coordinate
     * @param y the y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Position clone() {
        return new Position(this.x, this.y);
    }

}
