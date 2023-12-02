package fr.team92.serpents.game.model;

import fr.team92.serpents.game.controller.SegmentController;

public final class Segment {
    private Position position;
    private SegmentController controller;

    public Segment(Position position) {
        this.position = position.clone();
    }

    /**
     * Move the segment in the given direction
     * @param direction the direction
     */
    public void move(Direction direction) {
        switch (direction) {
            case NORTH:
                position.setY(position.getY() - 1);
                break;
            case SOUTH:
                position.setY(position.getY() + 1);
                break;
            case EAST:
                position.setX(position.getX() + 1);
                break;
            case WEST:
                position.setX(position.getX() - 1);
                break;
        }
    }

    /**
     * Simulate the move of the segment in the given direction
     * @param direction the direction
     * @return the new position
     */
    public Position simulateMove(Direction direction) {
        Position newPosition = position.clone();
        switch (direction) {
            case NORTH:
                newPosition.setY(newPosition.getY() - 1);
                break;
            case SOUTH:
                newPosition.setY(newPosition.getY() + 1);
                break;
            case EAST:
                newPosition.setX(newPosition.getX() + 1);
                break;
            case WEST:
                newPosition.setX(newPosition.getX() - 1);
                break;
        }
        return newPosition;
    }

    /**
     * Get the position of the segment
     * @return the position of the segment
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set the position of the segment
     * @param position the position of the segment
     */
    public SegmentController getController() {
        return controller;
    }

    /**
     * Set the controller of the segment
     * @param controller the controller of the segment
     */
    public void setController(SegmentController controller) {
        this.controller = controller;
    }

}
