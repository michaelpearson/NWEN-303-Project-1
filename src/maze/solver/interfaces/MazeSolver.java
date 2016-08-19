package maze.solver.interfaces;

import maze.solver.MazeSolution;

public interface MazeSolver {
    MazeSolution solveMaze() throws InterruptedException;
}
