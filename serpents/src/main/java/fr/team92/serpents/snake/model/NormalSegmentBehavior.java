package fr.team92.serpents.snake.model;

public class NormalSegmentBehavior implements SegmentBehavior {

    @Override
    public void applyEffect(Snake snake, Segment segment) {
        // Pas d'effet
    }

    @Override
    public boolean isInCollision(Segment segment, Segment otherSegment) {        
        return segment.getPosition().distanceTo(otherSegment.getPosition()) < segment.getDiameter() / 2
                + otherSegment.getDiameter() / 2;
    }

    @Override
    public String getName() {
        return "normal";
    }

}
