package maze.squares;

import maze.squares.interfaces.Square;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

class MazeSquare implements Square {

    private Type squareType;
    private Set<Mark> marks = ConcurrentHashMap.newKeySet();
    private final AtomicReference<Object> visitor = new AtomicReference<>(null);
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
    public Object setVisitor(Object o) {
        synchronized (visitor) {
            Object obj = visitor.get();
            if (visitor.compareAndSet(null, o)) {
                return o;
            } else {
                return obj;
            }
        }
    }

    @Override
    public void removeVisitor() {
        synchronized (visitor) {
            visitor.set(null);
        }
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
