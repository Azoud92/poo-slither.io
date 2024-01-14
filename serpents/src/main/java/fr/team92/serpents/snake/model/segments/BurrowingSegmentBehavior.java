package fr.team92.serpents.snake.model.segments;

import fr.team92.serpents.snake.model.Snake;

/**
 * Cette classe représente le comportement de creusement d'un segment de serpent.
 * Elle implémente l'interface SegmentBehavior.
 */
public final class BurrowingSegmentBehavior implements SegmentBehavior {

    @Override
    public void applyEffect(Snake snake, Segment segment) {
        // Pas d'effet
    }

    /**
     * Vérifie si le segment est en collision avec un autre segment. Dans le cas d'un segment fouisseur, il n'y a pas de collision.
     * @param segment le segment à vérifier.
     * @param otherSegment l'autre segment avec lequel vérifier la collision.
     * @return false car il n'y a pas de collision en mode fouisseur
     */
    @Override
    public boolean isInCollision(Segment segment, Segment otherSegment) {
        return false;
    }

    @Override
    public SegmentBehaviorType getBehaviorType() {
        return SegmentBehaviorType.BURROWING;
    }

}
