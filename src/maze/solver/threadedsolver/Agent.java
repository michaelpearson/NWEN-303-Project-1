package maze.solver.threadedsolver;

import maze.Maze;
import maze.solver.MazeSolution;

public class Agent implements Runnable {
    private final int threadsAvaliable;
    private final Maze maze;
    private MazeSolution solution = null;

    public Agent(Maze maze, int threadsAvailable) {
        this.maze = maze;
        this.threadsAvaliable = threadsAvailable;
    }

    @Override
    public void run() {

    }

    public MazeSolution getSolution() {
        return solution;
    }
}
