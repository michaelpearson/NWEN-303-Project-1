package maze.solver;

import maze.Maze;
import maze.solver.interfaces.*;

public class SolverFactory {
    public static MazeSolver getSolver(Maze maze, int maxNumberOfThreads) {
        return new ThreadedMazeSolver(maze, maxNumberOfThreads);
    }
}
