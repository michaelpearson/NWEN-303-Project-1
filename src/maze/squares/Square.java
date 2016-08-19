package maze.squares;

public interface Square {
    boolean isWall();
    void mark(Mark type);
    boolean isMarked(Mark type);

    default boolean isSpace() {
        return !isWall();
    }
}
