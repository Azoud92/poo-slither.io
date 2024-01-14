package fr.team92.serpents.snake.model.segments;

import fr.team92.serpents.snake.model.Snake;

/**
 * Cette classe représente le comportement normal d'un segment de serpent.
 */
public final class NormalSegmentBehavior implements SegmentBehavior {

    @Override
    public void applyEffect(Snake snake, Segment segment) {
        return;
        // Pas d'effet
    }
    
    @Override
    public boolean isInCollision(Segment segment, Segment otherSegment) {
        if (segment == null || otherSegment == null) throw new IllegalArgumentException("Les arguments ne doivent pas être null");
        return segment.getPosition().distanceTo(otherSegment.getPosition()) < segment.getDiameter() / 2
                + otherSegment.getDiameter() / 2;
    }
    
    @Override
    public SegmentBehaviorType getBehaviorType() {
        return SegmentBehaviorType.NORMAL;
    }

}
