package maze.squares;

import maze.squares.interfaces.Square;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class MazeSquare implements Square {

    private Type squareType;
    private Set<Mark> marks = ConcurrentHashMap.newKeySet();
    private Object visitor = null;
    private final Coordinate coordinate;
    private Square nextGold = null;

    @Override
    public Square getNextGold() {
        return nextGold;
    }

    public void setNextGold(Square nextGold) {
        this.nextGold = nextGold;
    }

    enum Type {
        SPACE,
        WALL
    }

    MazeSquare(Type squareType, Coordinate coordinate) {
        this.squareType = squareType;
        this.coordinate = coordinate;
    }

    @Override
    public boolean isWall() {
        return squareType == Type.WALL;
    }

    @Override
    public void mark(Mark type) {
        if(isMarked(Mark.GOLD)) {
            return;
        }
        marks.add(type);
    }

    @Override
    public boolean isMarked(Mark type) {
        return marks.contains(type);
    }

    @Override
    public synchronized Object setVisitor(Object o) {
        if(visitor != null) {
            return visitor;
        }
        visitor = o;
        return visitor;
    }

    @Override
    public synchronized void removeVisitor() {
        visitor = null;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
