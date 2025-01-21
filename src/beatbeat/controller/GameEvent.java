package beatbeat.controller;

public interface GameEvent {
    void onGameFinished(boolean reachedFinish);
}