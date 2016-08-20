package maze.squares.interfaces;

import maze.squares.Coordinate;
import maze.squares.Mark;

public interface Square {
    boolean isWall();
    void mark(Mark type);
    boolean isMarked(Mark type);
    Object setVisitor(Object o);
    void removeVisitor();
    Coordinate getCoordinate();

    default boolean isSpace() {
        return !isWall();
    }


    Square getNextGold();
    void setNextGold(Square square);
}
