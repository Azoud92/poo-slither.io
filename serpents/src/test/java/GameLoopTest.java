// FILEPATH: /c:/Users/evanj/projet-cpo-2023-2024/serpents/src/test/java/GameLoopTest.java

import org.junit.jupiter.api.Test;
import fr.team92.serpents.game.controller.GameLoop;
import static org.junit.jupiter.api.Assertions.*;

public class GameLoopTest {

    private class GameLoopImpl implements GameLoop {
        private boolean running = false;

        @Override
        public void start() {
            running = true;
        }

        @Override
        public void stop() {
            running = false;
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }

    @Test
    public void testStart() {
        GameLoop gameLoop = new GameLoopImpl();
        gameLoop.start();
        assertTrue(gameLoop.isRunning());
    }

    @Test
    public void testStop() {
        GameLoop gameLoop = new GameLoopImpl();
        gameLoop.start();
        gameLoop.stop();
        assertFalse(gameLoop.isRunning());
    }

    @Test
    public void testIsRunning() {
        GameLoop gameLoop = new GameLoopImpl();
        assertFalse(gameLoop.isRunning());
        gameLoop.start();
        assertTrue(gameLoop.isRunning());
        gameLoop.stop();
        assertFalse(gameLoop.isRunning());
    }
}