package fr.team92.serpents.game.network;

import java.util.ArrayList;

import fr.team92.serpents.snake.model.Segment;

public class GameModelNetwork {

    private ArrayList<Segment> segments;

    public GameModelNetwork() {
        this.segments = new ArrayList<Segment>();
    }

    public void addSegment(Segment segment) {
        this.segments.add(segment);
    }

    public void clearSegments() {
        this.segments.clear();
    }

}