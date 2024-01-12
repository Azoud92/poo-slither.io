package fr.team92.serpents.snake.model;

public class BurrowingSegmentBehavior implements SegmentBehavior {

    @Override
    public void applyEffect(Snake snake, Segment segment) {
        // Pas d'effet
    }

    @Override
    public boolean isInCollision(Segment segment, Segment otherSegment) {
        return false;
    }

    @Override
    public String getName() {
        return "burrowing";
    }   

}
