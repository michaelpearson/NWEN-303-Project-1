package maze.solver;

import maze.squares.Coordinate;
import maze.squares.interfaces.Square;

import java.util.List;

public class MazeSolution {
    private final List<Square> path;


    public MazeSolution(List<Square> path) {
        this.path = path;
    }

    public List<Square> getPath() {
        return path;
    }

    public boolean isCoordinateInPath(Coordinate c) {
        for(Square s : path) {
            if(c.equals(s.getCoordinate())) {
                return true;
            }
        }
        return false;
    }

}
