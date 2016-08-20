package maze.squares;

public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("Row: %d, Column %d\n", row, column);
    }

    public Coordinate north() {
        return new Coordinate(row - 1, column);
    }

    public Coordinate south() {
        return new Coordinate(row + 1, column);
    }

    public Coordinate east() {
        return new Coordinate(row, column + 1);
    }

    public Coordinate west() {
        return new Coordinate(row, column - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return c.getRow() == getRow() && c.getColumn() == getColumn();
        }
        return false;
    }
}
