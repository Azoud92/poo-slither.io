package fr.team92.serpents.snake.model.segments;

import fr.team92.serpents.snake.model.Snake;

/**
 * Cette interface définit le comportement d'un segment de serpent.
 * Elle définit les méthodes nécessaires pour appliquer un effet, vérifier une collision et obtenir le nom du comportement.
 */
public sealed interface SegmentBehavior permits NormalSegmentBehavior, BurrowingSegmentBehavior {
    
    /**
     * Applique un effet au serpent en fonction du comportement du segment.
     * @param snake le serpent auquel appliquer l'effet.
     * @param segment le segment qui applique l'effet.
     */
    void applyEffect(Snake snake, Segment segment);

    /**
     * Vérifie si le segment est en collision avec un autre segment.
     * @param segment le segment à vérifier.
     * @param otherSegment l'autre segment avec lequel vérifier la collision.
     * @return true si les segments sont en collision, false sinon.
     */
    boolean isInCollision(Segment segment, Segment otherSegment);

    /**
     * Obtient le type du comportement.
     * @return le type du comportement.
     */
    SegmentBehaviorType getBehaviorType();

}
