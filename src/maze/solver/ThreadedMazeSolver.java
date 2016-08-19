package maze.solver;

import maze.Maze;
import maze.solver.interfaces.MazeSolver;
import maze.solver.threadedsolver.Agent;

class ThreadedMazeSolver implements MazeSolver {

    private Maze maze;
    private int maxNumberOfThreads;

    ThreadedMazeSolver(Maze maze, int maxNumberOfThreads) {
        this.maze = maze;
        this.maxNumberOfThreads = maxNumberOfThreads;
    }

    @Override
    public MazeSolution solveMaze() throws InterruptedException {
        Agent initialAgent = new Agent(maze, maxNumberOfThreads);
        Thread initialThread = new Thread(initialAgent);
        initialThread.start();
        initialThread.join();
        return initialAgent.getSolution();
    }
}
