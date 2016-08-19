package maze.solver;

import maze.Maze;
import maze.solver.interfaces.MazeSolver;

class ThreadedMazeSolver implements MazeSolver {

    private Maze maze;

    ThreadedMazeSolver(Maze maze) {
        this.maze = maze;
    }

    @Override
    public MazeSolution solveMaze() {
        return null;
    }
}
