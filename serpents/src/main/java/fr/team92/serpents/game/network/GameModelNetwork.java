package fr.team92.serpents.game.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import javafx.application.Platform;

public class GameModelNetwork implements Observable {

    private List<Segment> segments;
    private ArrayList<Observer> observers;

    public GameModelNetwork() {
        this.segments = new CopyOnWriteArrayList<Segment>();
        this.observers = new ArrayList<>();
    }

    public void addSegment(Segment seg) {
        synchronized (segments) {
            segments.add(seg);
        }
        Platform.runLater(() -> notifyObservers());
    }

    public void clearSegments() {
        synchronized (segments) {
            segments.clear();
        }
        notifyObservers();
    }

    public List<Segment> getSegments() {
        synchronized (segments) {
            return new CopyOnWriteArrayList<Segment>(segments);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update());
    }

}