package fr.team92.serpents.snake.model;

public interface SegmentBehavior {
    
    void applyEffect(Snake snake, Segment segment);
    boolean isInCollision(Segment segment, Segment otherSegment);

}
