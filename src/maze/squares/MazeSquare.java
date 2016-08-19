package maze.squares;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class MazeSquare implements Square {

    private Type squareType;
    private Set<Mark> marks = ConcurrentHashMap.newKeySet();

    enum Type {
        SPACE,
        WALL
    }

    MazeSquare(Type squareType) {
        this.squareType = squareType;
    }

    @Override
    public boolean isWall() {
        return squareType == Type.WALL;
    }

    @Override
    public void mark(Mark type) {
        marks.add(type);
    }

    @Override
    public boolean isMarked(Mark type) {
        return marks.contains(type);
    }
}
